package org.proview.test.Scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;
import org.proview.model.Book.BookGoogle;
import org.proview.model.Book.BookLib;
import org.proview.model.Book.BookManagement;
import org.proview.model.Tag.Tag;
import org.proview.utils.MaxSizedContextMenu;
import org.proview.utils.SQLUtils;
import org.proview.utils.SearchUtils;

import java.io.IOException;
import java.sql.SQLException;
import javafx.concurrent.Task;

public class SearchResultView {
    public VBox topResultListVBox;
    public VBox googleBookListVBox;
    public ImageView loadingImageView;

    public MenuButton tagIncludedSelectDropdown;
    public FlowPane tagIncludedSelectedFlowPane;
    private ObservableList<Tag> tagIncludedSelectedList = FXCollections.observableArrayList();

    public MenuButton tagExcludedSelectDropdown;
    public FlowPane tagExcludedSelectedFlowPane;
    private ObservableList<Tag> tagExcludedSelectedList = FXCollections.observableArrayList();

    public RangeSlider ratingSlider;
    public Label lowRatingLabel;
    public Label highRatingLabel;

    public Button applyFiltersButton;

    private void addSelectedTag(Tag tag, ObservableList<Tag> tagList, FlowPane tagFlowPane) {
        // Making a cloned tag because a label can only have 1 parent.
        Tag newTag = new Tag(tag.getTagName(), tag.getBgColorHex(), tag.getTextColorHex());
        tagList.add(newTag);
        tagFlowPane.getChildren().add(newTag.getLabel());
    }

    private void removeSelectedTag(Tag tag, ObservableList<Tag> tagList, FlowPane tagFlowPane) {
        tagList.removeIf(tagInList ->
                tag.getTagName().equals((tagInList.getTagName()))
        );
        tagFlowPane.getChildren().removeIf(node ->
                tag.getTagName().equals(((Label) node).getText())
        );
    }

    private void loadFilters() throws SQLException {
        ObservableList<Tag> tagIncludedList = SQLUtils.getTagList();
        ObservableList<Tag> tagExcludedList = SQLUtils.getTagList();

        // Tag included dropdown
        MaxSizedContextMenu maxSizedIncludedContextMenu = new MaxSizedContextMenu();
        maxSizedIncludedContextMenu.setMaxHeight(200);
        ObservableList<Tag> oldIncludedList = SearchUtils.getTagIncludedList();
        for (Tag tag : tagIncludedList) {
            CheckBox checkBox = new CheckBox();

            boolean ifOldTag = oldIncludedList.contains(tag);

            checkBox.setSelected(ifOldTag);
            if (ifOldTag) {
                addSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
            }

            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    addSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                }
                else {
                    removeSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                }
            });

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPrefWidth(150);
            hBox.getChildren().add(checkBox);
            hBox.getChildren().add(tag.getLabel());

            hBox.setOnMouseClicked(event -> {
                if (!checkBox.isSelected()) {
                    addSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                    checkBox.setSelected(true);
                }
                else {
                    removeSelectedTag(tag, tagIncludedSelectedList, tagIncludedSelectedFlowPane);
                    checkBox.setSelected(false);
                }
            });

            CustomMenuItem customMenuItem = new CustomMenuItem(hBox);
            customMenuItem.setHideOnClick(false);
            maxSizedIncludedContextMenu.getItems().add(customMenuItem);
        }
        tagIncludedSelectDropdown.setOnMousePressed(event -> {
            if (!maxSizedIncludedContextMenu.isShowing()) {
                Bounds bounds = tagIncludedSelectDropdown.localToScreen(tagIncludedSelectDropdown.getBoundsInLocal());
                double x = bounds.getMinX();
                double y = bounds.getMaxY() + 5;
                maxSizedIncludedContextMenu.show(tagIncludedSelectDropdown, x, y);
            } else {
                maxSizedIncludedContextMenu.hide();
            }
        });

        // Tag excluded dropdown
        MaxSizedContextMenu maxSizedExcludedContextMenu = new MaxSizedContextMenu();
        maxSizedExcludedContextMenu.setMaxHeight(200);
        ObservableList<Tag> oldExcludedList = SearchUtils.getTagExcludedList();

        for (Tag tag : tagExcludedList) {
            CheckBox checkBox = new CheckBox();

            boolean ifOldTag = oldExcludedList.contains(tag);

            checkBox.setSelected(ifOldTag);
            if (ifOldTag) {
                addSelectedTag(tag, tagExcludedSelectedList, tagExcludedSelectedFlowPane);
            }

            checkBox.setOnAction(event -> {
                if (checkBox.isSelected()) {
                    addSelectedTag(tag, tagExcludedSelectedList, tagExcludedSelectedFlowPane);
                }
                else {
                    removeSelectedTag(tag, tagExcludedSelectedList, tagExcludedSelectedFlowPane);
                }
            });

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPrefWidth(150);
            hBox.getChildren().add(checkBox);
            hBox.getChildren().add(tag.getLabel());

            hBox.setOnMouseClicked(event -> {
                if (!checkBox.isSelected()) {
                    addSelectedTag(tag, tagExcludedSelectedList, tagExcludedSelectedFlowPane);
                    checkBox.setSelected(true);
                }
                else {
                    removeSelectedTag(tag, tagExcludedSelectedList, tagExcludedSelectedFlowPane);
                    checkBox.setSelected(false);
                }
            });

            CustomMenuItem customMenuItem = new CustomMenuItem(hBox);
            customMenuItem.setHideOnClick(false);
            maxSizedExcludedContextMenu.getItems().add(customMenuItem);
        }
        tagExcludedSelectDropdown.setOnMousePressed(event -> {
            if (!maxSizedExcludedContextMenu.isShowing()) {
                Bounds bounds = tagExcludedSelectDropdown.localToScreen(tagExcludedSelectDropdown.getBoundsInLocal());
                double x = bounds.getMinX();
                double y = bounds.getMaxY() + 5;
                maxSizedExcludedContextMenu.show(tagExcludedSelectDropdown, x, y);
            } else {
                maxSizedExcludedContextMenu.hide();
            }
        });

        // Rating slider
        ratingSlider.adjustLowValue(SearchUtils.getLowRating());
        ratingSlider.adjustHighValue(SearchUtils.getHighRating());
        lowRatingLabel.setText(String.format("%.2f", SearchUtils.getLowRating()));
        highRatingLabel.setText(String.format("%.2f", SearchUtils.getHighRating()));

        ratingSlider.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            lowRatingLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });

        // Update label values and positions when the high thumb moves
        ratingSlider.highValueProperty().addListener((obs, oldVal, newVal) -> {
            highRatingLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });
    }

    private void reloadLibSearch() throws SQLException {
        ObservableList<BookLib> bookLibList = BookManagement.getTopRatedBookList();
        ObservableList<BookLib> filteredBookList = SearchUtils.filterBookList(bookLibList);
        if (filteredBookList.size() > 20)
            filteredBookList.remove(20, filteredBookList.size());

        BookManagement.initBookList(topResultListVBox, filteredBookList, false, true, false);
    }

    private void reloadGoogleSearch() throws SQLException, IOException {
        Task<ObservableList<BookGoogle>> task = new Task<>() {
            @Override
            protected ObservableList<BookGoogle> call() throws Exception {
                return BookManagement.getGoogleBookList(SearchUtils.getCurQuery());
            }
        };

        task.setOnSucceeded(event -> {
            ObservableList<BookGoogle> bookGoogleList = task.getValue();
            if (bookGoogleList != null) {
                BookManagement.initBookList(googleBookListVBox, bookGoogleList, false, false, false);
            }
        });

        // Handle errors
        task.setOnFailed(event -> {
            googleBookListVBox.getChildren().clear();
            Label errorLabel = new Label("Failed to load from Google books.");
            googleBookListVBox.getChildren().add(errorLabel);
            task.getException().printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void initialize() throws SQLException, IOException {
        loadFilters();
        reloadLibSearch();
        reloadGoogleSearch();
    }

    public void onApplyFiltersButtonClicked(ActionEvent actionEvent) throws SQLException {
        SearchUtils.setHighRating(ratingSlider.getHighValue());
        SearchUtils.setLowRating(ratingSlider.getLowValue());
        SearchUtils.setTagIncludedList(tagIncludedSelectedList);
        SearchUtils.setTagExcludedList(tagExcludedSelectedList);

        reloadLibSearch();
    }
}
