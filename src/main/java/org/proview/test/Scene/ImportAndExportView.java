package org.proview.test.Scene;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.proview.test.AppMain;
import org.proview.utils.PopUpWindowUtils;
import org.proview.utils.SQLUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if (table.equals("book")) {
            return new String[]{"name", "author", "description", "copies", "coverUrl"};
        } else if (table.equals("user")){
            return new String[]{"username", "password", "firstName", "lastName", "email"};
        } else if (table.equals("questions")) {
            return new String[]{"difficulty", "question", "correct_answer", "incr_ans1", "incr_ans2", "incr_ans3"};
        }
        return new String[0];
    }

    private String importSql (String table) throws SQLException {
        String query = "";
        if (table.equals("book")) {
            query = """
                INSERT INTO book (name, author, description, copies, time_added)
                VALUE (?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;
        } else if (table.equals("user")) {
            query = """
                    INSERT INTO user
                    (username, password, firstname, lastname, email, type, registration_date, card_view)
                    VALUE (?, ?, ?, ?, ?, 1, CURRENT_TIMESTAMP, 0)
                    """;
        } else if (table.equals("questions")) {
            query = """
                    INSERT INTO questions (difficulty, question, correct_answer, incr_ans1, incr_ans2, incr_ans3, type)
                    VALUE (?, ?, ?, ?, ?, ?, 'multiple')
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
                if (line.trim().isEmpty()) {
                    continue;
                }
                System.out.println(line);
                String[] datas = line.split(",");
                if (datas.length != columns.length) {
                    importResultLabel.setText("Data in rows (Column doesn't match)");
                    return false;
                }
                if (!table.equals("book")) {
                    for (int i = 0; i < datas.length; i++) {
                        preparedStatement.setString(i + 1, datas[i].trim());
                    }
                } else {
                    for (int i = 0; i < datas.length - 1; i++) {
                        preparedStatement.setString(i + 1, datas[i].trim());
                    }
                    if (!downloadImageToPng(datas[datas.length - 1].trim(),
                            "./assets/covers", "cover%d.png".formatted(SQLUtils.getBookList().getLast().getId() + 1))) {
                        return false;
                    }
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

    public boolean downloadImageToPng(String imageUrl, String outputFolderPath, String outputFileName) {
        try {
            // Tải ảnh từ URL
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            if (image != null) {
                // Thư mục đích và tên file PNG
                File outputFolder = new File(outputFolderPath);
                if (!outputFolder.exists()) {
                    outputFolder.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                File outputFile = new File(outputFolder, outputFileName); // Tên tệp đầu ra

                // Lưu ảnh dưới định dạng PNG
                ImageIO.write(image, "PNG", outputFile);

                this.importResultLabel.setText("Image stored at: " + outputFile.getAbsolutePath());
                System.out.println("Image stored at: " + outputFile.getAbsolutePath());
            } else {
                this.importResultLabel.setText("Cannot download from URL.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.importResultLabel.setText("Can't get Image from URL!");
            return false;
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

    public void onDownloadTemplateFileClicked(ActionEvent actionEvent) throws SQLException {
        String table = exportTableComboBox.getValue();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Directory!");
        fileChooser.setInitialFileName(table + "template.csv");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
        csvFile = fileChooser.showSaveDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        // Kiểm tra nếu người dùng đã chọn thư mục
        if (csvFile != null) {

            // Tạo mảng dữ liệu từ hàm getColumnsInTable
            String[] columns = getColumnsInTable(importTableComboBox.getValue());
            importResultLabel.setText("Download successfully.");
            // Ghi dữ liệu vào file CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
                // Viết các cột vào file, phân cách bằng dấu phẩy
                writer.write(String.join(",", columns));
                writer.newLine(); // Thêm dòng mới nếu cần

            } catch (IOException e) {
                e.printStackTrace();
                importResultLabel.setText("Download failed.");
            }
        } else {
            // Thông báo nếu không chọn thư mục
            importResultLabel.setText("No directory choosen.");
        }
    }
}
