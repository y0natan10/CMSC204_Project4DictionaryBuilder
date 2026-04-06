// Yonatan Rubin
// M21105076

/*
 * search <word> – report whether a word exists. 
 * add <word> – insert a new word into the table. 
 * delete <word> – remove a word from the table 
 * list – print all stored words in alphabetical order. 
 * stats – print the following statistics about the dictionary. 
 * * total words the total number of words inserted into the dictionary including duplicates. 
 * * unique words the number of different words inserted into the dictionary. 
 * * estimated load factor the load factor for the underlying hash table. 
 * exit - quit the program.
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides a command line interface for interacting with the DictionaryBuilder.
 */
public class DictionaryShell {

	/**
	 * main method that allows a user to search, add, delete words to a dictionary.
	 * it also allows users to find various statistics about the dictionary such as
	 * total words, total unique words, and the load factor
	 * 
	 * @param arghs
	 */
	public static void main(String[] args) {
		DictionaryBuilder db;

		// If a filename is passed via command line, load it
		if (args.length > 0) {
			try {
				db = new DictionaryBuilder(args[0]);
			} catch (FileNotFoundException e) {
				System.out.println("File not found. Initializing empty dictionary.");
				db = new DictionaryBuilder(100);
			}
		} else {
			db = new DictionaryBuilder(100);
		}

		Scanner input = new Scanner(System.in);
		boolean running = true;

		while (running) {
			System.out.print("> ");
			String fullLine = input.nextLine().trim();
			if (fullLine.isEmpty())
				continue;

			String[] parts = fullLine.split("\\s+", 2);
			String command = parts[0].toLowerCase();
			String argument = (parts.length > 1) ? parts[1] : "";

			switch (command) {
			case "add":
				db.addWord(argument);
				System.out.println("\"" + argument + "\" added");
				break;

			case "search":
				int freq = db.getFrequency(argument);
				if (freq == 0) {
					System.out.println("\"" + argument + "\" not found");
				} else {
					// needed to use printf here because trying to use
					// System.out.println(freq + " instance(s) of \"" + argument + "\" found");
					// resulting in there being no space in between 'freq'
					// and the word instances
					System.out.printf("%d instance(s) of \"%s\" found%n", freq, argument);
				}
				break;

			case "delete":
				try {
					db.removeWord(argument);
					System.out.println("\"" + argument + "\" deleted");
				} catch (DictionaryEntryNotFoundException e) {
					System.out.println(e.getMessage());
				}
				break;

			case "list":
				ArrayList<String> list = db.getAllWords();
				for (String s : list)
					System.out.println(s);
				break;

			case "stats":
				System.out.println("Total words: " + db.getTotalWords());
				System.out.println("Total unique words: " + db.getTable().getUniqueCount());
				System.out.printf("Estimated load factor: %.2f\n", db.getTable().getActualLoadFactor());
				break;

			case "exit":
				running = false;
				System.out.println("Quitting...");
				break;
			default:
				System.out.println("Unknown command: " + command);
			}

			// new line for spacing as seen in the example
			System.out.println();
		}
		input.close();
	}
}