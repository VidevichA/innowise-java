package org.example.models.file;

import org.example.models.ExplorerItem;

public class File implements ExplorerItem {
    private final String name;

    public File(String name) {
        if (name == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}