package org.proview.test;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class BookCellView {
    public ImageView coverImageView;
    public Label titleLabel;
    public Label tagLabel;
    public Label infoLabel;
    public Label copiesLabel;

    public void setData(String title, String tags, double rating, int issueCount, String imagePath, int copiesAvailable) throws FileNotFoundException {
        titleLabel.setText(title);
        tagLabel.setText("Tags: " + tags);
        infoLabel.setText(rating + " 🌟 " + issueCount + " 👀 ");
        copiesLabel.setText(copiesAvailable + (copiesAvailable == 1 ? " copy" : " copies") + " available");

        InputStream stream = new FileInputStream(imagePath);
        Image image = new Image(stream);
        coverImageView.setImage(image);
        coverImageView.setFitWidth(112.5);
        coverImageView.setPreserveRatio(true);
        coverImageView.setSmooth(true);
        coverImageView.setCache(true);
    }
}
