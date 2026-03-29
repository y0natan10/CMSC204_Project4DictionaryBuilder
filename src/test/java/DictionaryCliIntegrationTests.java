/**
 * ----------------------------------------------------------------------
 * File: DictionaryCliIntegrationTests.java
 * Author: Montgomery College CMSC204 Staff
 * Course: CMSC204 - Computer Science II
 * Project: DictionaryBuilder
 * Institution: Montgomery College
 * Year: 2025
 *
 * Description:
 *     This file contains public JUnit 5 integration tests designed to validate
 *     the CLI behavior of the DictionaryBuilder shell. These tests are safe
 *     to distribute to students.
 *
 * Notes:
 *     These tests simulate user interaction via System.in and verify output
 *     through System.out, covering commands such as add, delete, list, and stats.
 *
 * License:
 *     This file is provided for educational use in CMSC204 at Montgomery College.
 *     Redistribution outside this course is not permitted.
 * ----------------------------------------------------------------------
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.concurrent.*;

public class DictionaryCliIntegrationTests {

    private String getLineContaining(String fullOutput, String keyword) {
        for (String line : fullOutput.split("\\R")) {
            if (line.toLowerCase().contains(keyword.toLowerCase())) {
                return line.trim();
            }
        }
        return null;
    }

    private String runCliSession(String fullInput) throws Exception {
        String classpath = System.getProperty("java.class.path");
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", classpath, "DictionaryShell", "sample_input.txt");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write(fullInput);
        writer.flush();
        writer.close();

        StringBuilder output = new StringBuilder();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> readerTask = executor.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (IOException e) {
                output.append("ERROR reading output: ").append(e.getMessage()).append("\n");
            }
        });

        boolean finished = process.waitFor(10, TimeUnit.SECONDS);
        if (!finished) {
            process.destroyForcibly();
            readerTask.cancel(true);
            fail("CLI session timed out.");
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        return output.toString();
    }

    @Test
    public void testAddAndStatsCommands() throws Exception {
        String input = String.join("\n",
                "add banana",
                "list",
                "add apple",
                "add apple",
                "search apple",
                "stats",
                "exit"
        ) + "\n";

        String output = runCliSession(input);

        // Validate that banana appears in list
        assertTrue(output.contains("banana"), "Expected word 'banana' in list output.");

        // Collect all lines mentioning 'apple'
        String[] lines = output.split("\\R");
        boolean foundFrequency2 = false;

        for (String line : lines) {
            if (line.toLowerCase().contains("apple") && line.contains("2")) {
                foundFrequency2 = true;
                break;
            }
        }

        assertTrue(foundFrequency2, "Expected at least one line mentioning 'apple' with frequency 2.");
    }


    @Test
    public void testDeleteCommandAndStatsUpdate() throws Exception {
        String input = String.join("\n",
                "add carrot",
                "add carrot",
                "add turnip",
                "delete carrot",
                "list",
                "stats",
                "exit"
        ) + "\n";

        String output = runCliSession(input);
        assertFalse(output.contains("\ncarrot\n"), "Expected 'carrot' to be gone after deletion.");
        assertTrue(output.contains("\nturnip\n"), "Expected 'turnip' to remain.");

        String uniqueLine = getLineContaining(output, "unique words");
        assertNotNull(uniqueLine, "Expected a line containing 'unique words'");

        String[] tokens = uniqueLine.split("\\D+"); // Split on non-digits
        String lastNumber = tokens[tokens.length - 1];

        assertEquals("13", lastNumber, "Expected total word count to be exactly 13");
    }

    @Test
    public void testListCommand() throws Exception {
        String input = String.join("\n",
                "add peach",
                "add apple",
                "add grape",
                "list",
                "exit"
        ) + "\n";

        String output = runCliSession(input);
        assertTrue(output.indexOf("apple") < output.indexOf("grape"), "Expected alphabetical order.");
        assertTrue(output.contains("peach"), "Expected 'peach' in the list.");
    }
}
