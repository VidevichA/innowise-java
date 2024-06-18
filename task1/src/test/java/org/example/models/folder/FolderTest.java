package org.example.models.folder;

import static org.junit.jupiter.api.Assertions.*;

import org.example.models.ExplorerItem;
import org.example.models.Nameable;
import org.example.models.file.File;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Queue;
import java.util.PriorityQueue;

public class FolderTest {

    @Test
    public void testFolderCreation_ValidName() {
        String folderName = "TestFolder";
        Folder folder = new Folder(folderName);
        assertEquals(folderName, folder.getName());
    }

    @Test
    public void testFolderCreation_NullName() {
        assertThrows(IllegalArgumentException.class, () -> new Folder(null));
    }

    @Test
    public void testAddItem_Valid() {
        Folder folder = new Folder("TestFolder");
        ExplorerItem item1 = new File("File1");
        ExplorerItem item2 = new Folder("Folder1");

        folder.addItem(item1);
        folder.addItem(item2);

        Queue<ExplorerItem> items = folder.getFilesAndFolders();
        assertEquals(2, items.size());
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));
    }

    @Test
    public void testAddItem_Null() {
        Folder folder = new Folder("TestFolder");
        ExplorerItem item1 = null;
        assertThrows(IllegalArgumentException.class, () -> folder.addItem(item1));
    }

    @Test
    public void testItemsSortedAlphabetically() {
        Folder folder = new Folder("TestFolder");
        ExplorerItem item1 = new File("FileB");
        ExplorerItem item2 = new File("FileA");
        ExplorerItem item3 = new File("FileC");

        folder.addItem(item1);
        folder.addItem(item2);
        folder.addItem(item3);

        Queue<ExplorerItem> items = new PriorityQueue<>(Comparator.comparing(Nameable::getName));
        items.add(item2);
        items.add(item1);
        items.add(item3);

        assertEquals(items.size(), folder.getFilesAndFolders().size());

        for (ExplorerItem item : items) {
            assertEquals(item.getName(), folder.getFilesAndFolders().poll().getName());
        }
    }
}
