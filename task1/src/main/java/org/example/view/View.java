package org.example.view;

import org.example.models.ExplorerItem;
import org.example.models.file.File;
import org.example.models.folder.Folder;

public class View {
    private View() {
    }

    public static void printInputSection() {
        System.out.print("$>");
    }

    public static void printRoot(ExplorerItem current) {
        if (current == null) {
            throw new IllegalArgumentException();
        }

        printRoot(current, 0);
    }

    private static void printRoot(ExplorerItem current, int tabSize) {
        for (int i = 0; i < tabSize; ++i) {
            System.out.print(" ");
        }
        if (current instanceof File) {
            System.out.println(current.getName());
            return;
        }
        System.out.println(current.getName() + "/");
        Folder folder = (Folder) current;
        for (ExplorerItem item : folder.getFilesAndFolders()) {
            printRoot(item, tabSize + 2);
        }
    }
}
