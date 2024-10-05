package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class DocumentAPI {
    static ObservableList<Document> documentList = FXCollections.observableArrayList();

    private static void printTest() {
        System.out.println("-----------------");
        for (Document d : documentList) {
            System.out.println(d.getId() + " - " + d.getName() + " - " + d.getAuthor());
        }
    }
    public static Document addDocument(int id, String name, String author) {
        Document newDocument = new Document(id, name, author);
        documentList.add(newDocument);
        printTest();
        return newDocument;
    }

    public static void removeDocument(int id) {
        for (int i = documentList.size() - 1; i >= 0; i--) {
            if (documentList.get(i).getId() == id) {
                documentList.remove(i);
            }
        }
        printTest();
    }

    public static ObservableList<Document> getDocumentList() {
        return documentList;
    }
}
