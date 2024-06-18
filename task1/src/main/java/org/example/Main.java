package org.example;

import org.example.models.folder.Folder;
import org.example.service.PathService;
import org.example.view.View;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Folder root = new Folder("root");
        try (Scanner sc = new Scanner(System.in)) {
            String input;
            while (true) {
                View.printInputSection();
                input = sc.nextLine();
                switch (input) {
                    case "print":
                        View.printRoot(root);
                        break;
                    case "exit":
                        return;
                    default:
                        PathService.createFromPath(input, root);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}