/**
 * ----------------------------------------------------------------------
 * File: DictionaryBuilderPublicTests.java
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
 *     These tests ensure key methods work as described in the project specification.
 *     Students should pass these tests before submitting their project.
 *
 * License:
 *     This file is provided for educational use in CMSC204 at Montgomery College.
 *     Redistribution outside this course is not permitted.
 * ----------------------------------------------------------------------
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full functional test suite for DictionaryBuilder.
 * Covers all required public methods.
 */
public class DictionaryBuilderPublicTests {

    private DictionaryBuilder db;

    @BeforeEach
    public void setUp() {
        db = new DictionaryBuilder(11); // small estimate
    }

    // === Constructor ===

    @Test
    public void test01_constructor_initializesDictionary() {
        assertNotNull(db);
    }

    // === addWord ===

    @Test
    public void test02_addWord_once_shouldHaveFrequencyOne() {
        db.addWord("apple");
        assertEquals(1, db.getFrequency("apple"));
    }

    @Test
    public void test03_addWord_twice_shouldHaveFrequencyTwo() {
        db.addWord("apple");
        db.addWord("apple");
        assertEquals(2, db.getFrequency("apple"));
    }

    @Test
    public void test04_addTwoDifferentWords_shouldBeDistinct() {
        db.addWord("apple");
        db.addWord("banana");
        assertEquals(1, db.getFrequency("apple"));
        assertEquals(1, db.getFrequency("banana"));
    }

    @Test
    public void test05_addWord_ignoresCase() {
        db.addWord("Apple");
        db.addWord("apple");
        assertEquals(2, db.getFrequency("apple"));
    }

    @Test
    public void test06_addWord_stripsPunctuation() {
        db.addWord("apple!");
        db.addWord("apple?");
        db.addWord("apple");
        assertEquals(3, db.getFrequency("apple"));
    }

    // === getFrequency ===

    @Test
    public void test07_getFrequency_wordNotPresent_shouldBeZero() {
        assertEquals(0, db.getFrequency("missing"));
    }

    @Test
    public void test08_getFrequency_afterAddingAndRemoving() throws Exception {
        db.addWord("pear");
        db.addWord("pear");
        db.removeWord("pear");
        assertEquals(0, db.getFrequency("pear"));
    }

    // === getAllWords ===

    @Test
    public void test09_getAllWords_shouldReturnSortedList() {
        db.addWord("banana");
        db.addWord("apple");
        db.addWord("cherry");
        List<String> result = db.getAllWords();
        assertEquals(List.of("apple", "banana", "cherry"), result);
    }

    @Test
    public void test10_getAllWords_emptyDictionary_shouldReturnEmptyList() {
        List<String> result = db.getAllWords();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // === removeWord ===

    @Test
    public void test11_removeWord_shouldRemoveCompletely() throws Exception {
        db.addWord("kiwi");
        db.removeWord("kiwi");
        assertEquals(0, db.getFrequency("kiwi"));
    }

    @Test
    public void test12_removeWord_nonexistentWord_shouldThrow() {
        assertThrows(DictionaryEntryNotFoundException.class, () -> db.removeWord("ghost"));
    }
}
