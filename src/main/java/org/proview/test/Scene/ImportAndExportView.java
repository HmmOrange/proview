package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.proview.test.AppMain;
import org.proview.utils.PopUpWindowUtils;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ImportAndExportView {
    private static final String[] exportTables = {"book", "book_tag", "favourite", "game_history",
            "issue", "questions", "rating", "review", "tag", "user"};
    private static final String[] importTables = {"book", "questions", "user"};
    private File csvFile;


    private void initImportTab() {
        importTableComboBox.getItems().addAll(importTables);
        importTableComboBox.setValue(importTables[0]);
        importConfirmButton.setDisable(true);
    }

    private void initExportTab() {
        exportTableComboBox.getItems().addAll(exportTables);
        exportTableComboBox.setValue(exportTables[0]);
        exportConfirmButton.setDisable(true);
    }

    private String[] getColumnsInTable(String table) throws SQLException {
        String[] respond = new String[1];
        if (table.equals("book")) {

        }
        return respond;
    }

    private String importSql (String table) throws SQLException {
        String query = "";
        if (table.equals("book")) {
            query = """
                INSERT INTO book (name, author, description, time_added, copies)
                VALUE (?, ?, ?, CURRENT_TIMESTAMP, ?)
                """;
        } else if (table.equals("user")) {
            query = """
                    INSERT INTO user
                    (username, password, type, firstname, lastname, email, registration_date, card_view)
                    VALUE (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, 0)
                    """;
        } else if (table.equals("questions")) {
            query = """
                    INSERT INTO questions (type, difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3)
                    VALUE ('multiple', ?, ?, ?, ?, ?, ?)
                    """;
        }
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
            String sql = importSql(table);
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose book cover");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv")
        );
        csvFile = fileChooser.showOpenDialog(AppMain.window);
        if (csvFile != null) {
            importResultLabel.setText(csvFile.getAbsolutePath());
        } else {
            importResultLabel.setText("Cannot find your file!");
        }
        importConfirmButton.setDisable(false);
    }

    public void onChooseDirectoryButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        String table = exportTableComboBox.getValue();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Directory!");
        fileChooser.setInitialFileName(table + ".csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        csvFile = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        exportResultLabel.setText(csvFile.getAbsolutePath());
        exportConfirmButton.setDisable(false);
    }

    public void onImportConfirmButtonClicked(ActionEvent actionEvent) throws FileNotFoundException {
        if (PopUpWindowUtils.showConfirmation("Warning!", "Are you sure to import to database?")) {
            String table = importTableComboBox.getValue();
            if (importCsvFileToDatabase(table, csvFile)) {
                importResultLabel.setText("Import successfully!");
                csvFile = null;
                importConfirmButton.setDisable(true);
            }
        }
    }

    public void onExportConfirmButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        String table = exportTableComboBox.getValue();
        if (csvFile != null) {
            exportDatabaseToCsvFile(table, csvFile);
            csvFile = null;
            exportConfirmButton.setDisable(true);
        } else {
            exportResultLabel.setText("No directory chosen!");
        }
    }
}
