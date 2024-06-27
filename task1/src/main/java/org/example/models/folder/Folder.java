package org.example.models.folder;

import org.example.models.ExplorerItem;
import org.example.models.Nameable;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Folder implements ExplorerItem {
    private final String name;
    private final Queue<ExplorerItem> filesAndFolders = new PriorityQueue<>(Comparator.comparing(Nameable::getName));

    public Folder(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Folder name cannot be null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Queue<ExplorerItem> getFilesAndFolders() {
        return this.filesAndFolders;
    }

    public void addItem(ExplorerItem obj) {
        if (obj == null) {
            throw new IllegalArgumentException("null object");
        }
        this.filesAndFolders.add(obj);
    }
}