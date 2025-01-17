package org.proview.model.User;
import javafx.scene.control.Button;
import org.proview.test.AppMain;
import org.proview.utils.SQLUtils;

import java.sql.*;

public class UserManagement {
    Connection connection = AppMain.connection;

    public static User currentUser = null;
    public static Button currentViewButton = null;
    public static Button currentAdminViewButton = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserManagement.currentUser = currentUser;
    }

    public static Button getCurrentViewButton() {
        return currentViewButton;
    }

    public static void setCurrentViewButton(Button currentViewButton) {
        UserManagement.currentViewButton = currentViewButton;
    }

    public static void addNormalUser(User user) throws SQLException {
        SQLUtils.addUser(user);
    }

    public static Button getCurrentAdminViewButton() {
        return currentAdminViewButton;
    }

    public static void setCurrentAdminViewButton(Button currentAdminViewButton) {
        UserManagement.currentAdminViewButton = currentAdminViewButton;
    }
}
