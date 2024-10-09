module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}