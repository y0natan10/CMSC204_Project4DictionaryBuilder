/**
 * ----------------------------------------------------------------------
 * File: DictionaryBuilderGFATests.java
 * Author: Montgomery College CMSC204 Staff
 * Course: CMSC204 - Computer Science II
 * Project: DictionaryBuilder
 * Institution: Montgomery College
 * Year: 2025
 *
 * Description:
 *     This file contains public JUnit 5 tests used to validate basic functionality
 *     for Project 4: DictionaryBuilder. These tests are safe to distribute to students.
 *
 * Notes:
 *     These tests ensure key minimal functionality as described in the project specification.
 *     Students should pass these tests before submitting their project.
 *
 * License:
 *     This file is provided for educational use in CMSC204 at Montgomery College.
 *     Redistribution outside this course is not permitted.
 * ----------------------------------------------------------------------
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Good Faith Attempt (GFA) tests for DictionaryBuilder.
 * Only tests the required constructor and addWord method.
 */
public class DictionaryBuilderGFATests {

    private DictionaryBuilder db;

    @BeforeEach
    public void setUp() {
        db = new DictionaryBuilder(11); // Small table estimate
    }

    @Test
    public void test1_constructorRunsWithoutError() {
        assertNotNull(db, "DictionaryBuilder instance should not be null.");
    }

    @Test
    public void test2_addSingleWord_runsWithoutError() {
        assertDoesNotThrow(() -> db.addWord("apple"));
    }

    @Test
    public void test3_addMultipleWords_runsWithoutError() {
        assertDoesNotThrow(() -> {
            db.addWord("apple");
            db.addWord("banana");
            db.addWord("cherry");
        });
    }
}
