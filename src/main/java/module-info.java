module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires java.desktop;
    requires me.xdrop.fuzzywuzzy;

    opens org.proview.test to javafx.fxml;
    exports org.proview.test;
    exports org.proview.model;
    opens org.proview.model to javafx.fxml;
}