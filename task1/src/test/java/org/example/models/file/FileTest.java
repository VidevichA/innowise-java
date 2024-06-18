package org.example.models.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    @Test
    public void testFileCreation_ValidName() {
        String fileName = "test.txt";
        File file = new File(fileName);
        assertEquals(fileName, file.getName());
    }

    @Test
    public void testFileCreation_NullName() {
        assertThrows(IllegalArgumentException.class, () -> new File(null));
    }
}