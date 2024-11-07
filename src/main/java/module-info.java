module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires io.github.cdimascio.dotenv.java;
    requires me.xdrop.fuzzywuzzy;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires mysql.connector.j;
    requires com.google.gson;
    requires java.sql;
    requires org.apache.commons.lang3;
    requires java.desktop;

    opens org.proview.test to javafx.fxml;
    exports org.proview.test;
    exports org.proview.model;
    opens org.proview.model to javafx.fxml;
}