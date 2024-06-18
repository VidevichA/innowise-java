package org.example.service;

import org.example.exceptions.IncorrectRootNameException;
import org.example.exceptions.ItemsAfterFileException;
import org.example.models.ExplorerItem;
import org.example.models.file.File;
import org.example.models.folder.Folder;

import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;

public class PathService {
    private PathService() {
    }

    public static void createFromPath(String path, Folder root) throws Exception {
        if (path == null || root == null) {
            throw new IllegalArgumentException();
        }
        String[] itemNames = Arrays.stream(path.split("/")).map(String::trim).toArray(String[]::new);
        if (!root.getName().equals(itemNames[0])) {
            throw new IncorrectRootNameException();
        }
        Folder currentFolder = root;
        int lastExistingItemIndex = 0;
        for (int i = 1; i < itemNames.length; ++i) {
            Optional<ExplorerItem> foundItem = findItemByName(currentFolder.getFilesAndFolders(), itemNames[i]);
            if (foundItem.isEmpty()) {
                break;
            }
            lastExistingItemIndex = i;
            ExplorerItem item = foundItem.get();
            if (isFileName(itemNames[i]) && i != itemNames.length - 1) {
                throw new ItemsAfterFileException();
            }
            if (item instanceof Folder) {
                currentFolder = (Folder) item;
            }
        }
        addItemsFromIndex(currentFolder, itemNames, lastExistingItemIndex);
    }

    private static Optional<ExplorerItem> findItemByName(Queue<ExplorerItem> items, String itemName) {
        return items.stream()
                .filter(item -> item.getName().equals(itemName))
                .findFirst();
    }

    private static void addItemsFromIndex(Folder root, String[] itemNames, int startIndex) throws Exception {
        for (int i = startIndex + 1; i < itemNames.length; ++i) {
            ExplorerItem item = isFileName(itemNames[i]) ? new File(itemNames[i]) : new Folder(itemNames[i]);
            root.addItem(item);
            if (item instanceof File && i != itemNames.length - 1) {
                throw new ItemsAfterFileException();
            }
            if (item instanceof Folder) {
                root = (Folder) item;
            }
        }
    }

    private static boolean isFileName(String name) {
        return name.matches("^[a-zA-Z0-9]+\\.[a-zA-Z]+$");
    }
}
