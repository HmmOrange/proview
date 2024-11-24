package org.proview.test.Scene;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.net.URL;
import java.util.Random;

public class FooterView {
    public FontIcon heartIcon;

    public void onGithubIconClicked(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/HmmOrange/proview").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onHeartIconHovered(MouseEvent mouseEvent) {
        heartIcon.setIconColor(getRandomColor());
    }

    public void onHeartIconExited(MouseEvent mouseEvent) {
        heartIcon.setIconColor(Color.WHITE);
    }

    private Color getRandomColor() {
        Random random = new Random();

        double hue = random.nextDouble();
        double saturation = 0.7 + (random.nextDouble() * 0.3); // Range: 0.7 to 1.0
        double brightness = 0.8 + (random.nextDouble() * 0.2); // Range: 0.8 to 1.0

        return Color.hsb(hue * 360, saturation, brightness);

    }
}
