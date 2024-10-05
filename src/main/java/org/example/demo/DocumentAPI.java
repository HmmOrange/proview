package org.example.demo;

import java.util.ArrayList;
import java.util.List;

public class DocumentAPI {
    static List<Document> documentList = new ArrayList<>();

    private static void printTest() {
        System.out.println("-----------------");
        for (Document d : documentList) {
            System.out.println(d.getId() + " - " + d.getName() + " - " + d.getAuthor());
        }
    }
    public static void addDocument(int id, String name, String author) {
        Document newDocument = new Document(id, name, author);
        documentList.add(newDocument);
        printTest();
    }
}
