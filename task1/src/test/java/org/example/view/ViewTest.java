package org.example.view;

import org.example.models.file.File;
import org.example.models.folder.Folder;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ViewTest {

    @Test
    public void testPrintInputSection() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        View.printInputSection();
        System.setOut(System.out);
        assertEquals("$>", outputStream.toString());
    }

    @Test
    public void testPrintRoot_NullParameter() {
        assertThrows(IllegalArgumentException.class, () -> View.printRoot(null));
    }


    @Test
    public void testPrintRoot_ValidFolder() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Folder folder = new Folder("Folder");
        folder.addItem(new File("file1.txt"));
        folder.addItem(new Folder("Subfolder"));
        View.printRoot(folder);

        System.setOut(System.out);
        assertEquals("Folder/\n  Subfolder/\n  file1.txt\n", outputStream.toString());
    }
}
