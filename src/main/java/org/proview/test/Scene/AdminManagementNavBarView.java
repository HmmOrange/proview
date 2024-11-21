package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AdminManagementNavBarView {
    public VBox root;
    private ComboBox<String> tableComboBox = new ComboBox<>();

    private void exportDatabaseToCsvFile(String table, File file) throws SQLException, IOException {
        String sql = String.format("SELECT * FROM %s", table);
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        try (FileWriter writer = new FileWriter(file)) {
            int numberOfColumns = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                columnName = columnName.replace(",", ";");
                writer.append(columnName);
                if (i < numberOfColumns) writer.append(",");
            }
            writer.append("\n");

            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    String data = resultSet.getString(i);
                    if (data == null) {
                        data = "";
                    }
                    data = data.replace(",", ";");
                    writer.append(data);
                    if (i < numberOfColumns) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
            Label exportResultLabel = new Label("Export successfully!");
            root.getChildren().add(exportResultLabel);
        } catch (IOException e) {
            e.printStackTrace();
            Label exportResultLabel = new Label("Fail to write data to CSV file!");
            root.getChildren().add(exportResultLabel);
        }
    }

    public void onIssuesButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("IssueListView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onAddDeleteButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("EditBookView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onUsersButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("UserManagementForAdminView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onBooksButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("BookManagForAdminView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        AppMain.window.setTitle("Hello!");
        AppMain.window.setScene(scene);
        AppMain.window.centerOnScreen();
    }

    public void onExportButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        if (root.getChildren().stream().noneMatch(node -> node instanceof ComboBox)) {
            String[] tables = {"book", "book_tag", "favourite", "game_history", "issue", "questions", "rating", "review", "tag", "user"};
            List<String> tablesList = Arrays.asList(tables);
            tableComboBox.getItems().addAll(tablesList);
            tableComboBox.setValue(tablesList.getFirst());
            root.getChildren().add(tableComboBox);
        } else {
            String table = tableComboBox.getValue();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Directory!");
            fileChooser.setInitialFileName(table + ".csv");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
            File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());
            if (file != null) {
                exportDatabaseToCsvFile(table, file);
            } else {
                Label exportResultLabel = new Label("Fail to export!");
                root.getChildren().add(exportResultLabel);
            }
        }
    }
}
