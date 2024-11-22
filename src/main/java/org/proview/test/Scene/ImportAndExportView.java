package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.proview.test.AppMain;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ImportAndExportView {
    private static final String[] tables = {"book", "book_tag", "favourite", "game_history",
            "issue", "questions", "rating", "review", "tag", "user"};


    private void initImportTab() {
        importTableComboBox.getItems().addAll(tables);
        importTableComboBox.setValue(tables[0]);
        importConfirmButton.setDisable(true);
    }

    private void initExportTab() {
        exportTableComboBox.getItems().addAll(tables);
        exportTableComboBox.setValue(tables[0]);
        exportConfirmButton.setDisable(true);
    }

    private String[] getColumnsInTable(String table) throws SQLException {
        String sql = String.format("SELECT * FROM %s", table);
        PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        int numberOfColumns = resultSet.getMetaData().getColumnCount();

        String[] respond = new String[numberOfColumns];
        for (int i = 1; i <= numberOfColumns; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i);
            respond[i-1] = columnName;
        }
        return respond;
    }

    private String importSql (String table, String[] columns) {
        StringBuilder queryBuilder = new StringBuilder("INSERT IGNORE INTO ").append(table).append(" (");
        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append(columns[i].trim());
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(") VALUES (");

        for (int i = 0; i < columns.length; i++) {
            queryBuilder.append("?");
            if (i < columns.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(");");
        String query = queryBuilder.toString();
        return query;
    }

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
            exportResultLabel.setText("Export successfully!");
            exportConfirmButton.setDisable(false);
        } catch (IOException e) {
            e.printStackTrace();
            exportResultLabel.setText("Fail to write data to CSV file!");
        }
    }

    private boolean importCsvFileToDatabase(String table, File csvFile) throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String metaDataLine = reader.readLine();
            if (metaDataLine == null) {
                importResultLabel.setText("File empty");
                return false;
            }
            String[] columns = metaDataLine.split(",");
            if (columns.length != getColumnsInTable(table).length) {
                importResultLabel.setText("Columns count doesn't match with database");
                return false;
            }
            String sql = importSql(table, getColumnsInTable(table));
            PreparedStatement preparedStatement = AppMain.connection.prepareStatement(sql);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] datas = line.split(",");
                System.out.println(Arrays.asList(datas).toString());
                if (datas.length != columns.length) {
                    System.out.println(datas.length + " " + columns.length);
                    importResultLabel.setText("Data in rows (Column doesn't match)");
                    return false;
                }
                for (int i = 0; i < datas.length; i++) {
                    preparedStatement.setString(i + 1, datas[i].trim());
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (IOException | SQLException e) {
            importResultLabel.setText("Exception. Import failed");
            throw new RuntimeException(e);
        }
        return true;
    }

    public ComboBox<String> importTableComboBox;
    public ComboBox<String> exportTableComboBox;
    public Label importResultLabel;
    public Label exportResultLabel;
    public VBox importTabVbox;
    public VBox exportTabVbox;
    public Button importConfirmButton;
    public Button exportConfirmButton;

    public void initialize() {
        initImportTab();
        initExportTab();
    }

    public void onChooseCSVFileButtonClicked(ActionEvent actionEvent) throws FileNotFoundException {
        String table = importTableComboBox.getValue();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose book cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv")
        );
        File csvFile = fileChooser.showOpenDialog(AppMain.window);
        if (csvFile != null) {
            if (importCsvFileToDatabase(table, csvFile)) {
                importResultLabel.setText("Import successfully");
            }
        }
    }

    public void onChooseDirectoryButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        String table = exportTableComboBox.getValue();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Directory!");
        fileChooser.setInitialFileName(table + ".csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        File file = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            exportDatabaseToCsvFile(table, file);
        } else {
            exportResultLabel.setText("No directory chosen!");
        }
    }

    public void onImportConfirmButtonClicked(ActionEvent actionEvent) {
    }

    public void onExportConfirmButtonClicked(ActionEvent actionEvent) {
    }
}
