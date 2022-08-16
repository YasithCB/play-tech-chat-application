package lk.playtech.chatApp.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * author  Yasith C Bandara
 * created 8/15/2022 - 9:38 PM
 * project Playtech
 */

public class NavigateUtil {

    private static double x,y;
    public static void setUi(AnchorPane apn, String location, String title) throws IOException {
        Stage stage = (Stage) apn.getScene().getWindow();
        Parent root = FXMLLoader.load(NavigateUtil.class.getResource("../view/" + location + ".fxml"));
        stage.setScene(new Scene(root));

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.setTitle(title);
        stage.centerOnScreen();
        stage.show();
    }
}
