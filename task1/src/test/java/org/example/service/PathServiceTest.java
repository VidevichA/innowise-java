package org.example.service;

import org.example.exceptions.IncorrectRootNameException;
import org.example.exceptions.ItemsAfterFileException;
import org.example.models.file.File;
import org.example.models.folder.Folder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathServiceTest {

    @Test
    public void testCreateFromPath() throws Exception {
        Folder root = new Folder("root");
        PathService.createFromPath("root/folder1/folder2/file.txt", root);

        assertEquals(1, root.getFilesAndFolders().size());
        Folder folder1 = (Folder) root.getFilesAndFolders().poll();
        assertEquals("folder1", folder1.getName());

        assertEquals(1, folder1.getFilesAndFolders().size());
        Folder folder2 = (Folder) folder1.getFilesAndFolders().poll();
        assertEquals("folder2", folder2.getName());

        assertEquals(1, folder2.getFilesAndFolders().size());
        File file = (File) folder2.getFilesAndFolders().poll();
        assertEquals("file.txt", file.getName());
    }

    @Test
    public void testCreateFromPath_IncorrectRootName() {
        Folder root = new Folder("root");
        assertThrows(IncorrectRootNameException.class, () -> PathService.createFromPath("wrongroot/folder/file.txt", root));
    }

    @Test
    public void testCreateFromPath_ItemsAfterFile() {
        Folder root = new Folder("root");
        assertThrows(ItemsAfterFileException.class, () -> PathService.createFromPath("root/file.txt/folder", root));
    }

    @Test
    public void testCreateFromPath_NullPath() {
        Folder root = new Folder("root");
        assertThrows(IllegalArgumentException.class, () -> PathService.createFromPath(null, root));
    }

    @Test
    public void testCreateFromPath_NullRoot() {
        assertThrows(IllegalArgumentException.class, () -> PathService.createFromPath("path", null));
    }
}
