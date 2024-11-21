module org.proview.test {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires io.github.cdimascio.dotenv.java;
    requires me.xdrop.fuzzywuzzy;
    requires java.net.http;
    requires mysql.connector.j;
    requires com.google.gson;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.desktop;
    requires timeago;
    requires org.kordamp.ikonli.javafx;
    requires annotations;

    exports org.proview.api;
    opens org.proview.api to javafx.fxml;
    exports org.proview.test;
    opens org.proview.test to javafx.fxml;
    exports org.proview.modal.User;
    opens org.proview.modal.User to javafx.fxml;
    exports org.proview.modal.Activity;
    opens org.proview.modal.Activity to javafx.fxml;
    exports org.proview.modal.Book;
    opens org.proview.modal.Book to javafx.fxml;
    exports org.proview.modal.Review;
    opens org.proview.modal.Review to javafx.fxml;
    exports org.proview.modal.Issue;
    opens org.proview.modal.Issue to javafx.fxml;
    exports org.proview.modal.Tag;
    opens org.proview.modal.Tag to javafx.fxml;
    exports org.proview.test.Container;
    opens org.proview.test.Container to javafx.fxml;
    exports org.proview.test.Scene;
    opens org.proview.test.Scene to javafx.fxml;
    exports org.proview.utils;
    opens org.proview.utils to javafx.fxml;
}