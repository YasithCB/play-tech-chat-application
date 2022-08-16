package lk.playtech.chatApp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * author  Yasith C Bandara
 * created 8/14/2022 - 10:43 AM
 * project Playtech
 */

public class ClientFormController extends Thread {
    public AnchorPane apnMain;
    public JFXTextField txtMsg;
    public JFXButton btnSend;
    public Label lblUserName;
    public VBox vbox;
    public JFXButton btnClose;
    //private
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private FileChooser fileChooser;
    private File filePath;

    public void initialize() {
        String userName = LoginFormController.userName;
        lblUserName.setText(String.valueOf(userName));
        try {
            socket = new Socket("localhost", 5001);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnSend.setOnMouseClicked(event -> {
            sendMsg();
        });
        btnClose.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }

    private void sendMsg() {
        String msg = txtMsg.getText();
        writer.println(lblUserName.getText() + ": " + txtMsg.getText());

        txtMsg.clear();

        if (msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {

                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];


                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]);
                }

                String[] msgToAr = msg.split(" ");
                String st = "";
                for (int i = 0; i < msgToAr.length - 1; i++) {
                    st += msgToAr[i + 1] + " ";
                }

                Text text = new Text(st);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);
                }

                if (firstChars.equalsIgnoreCase("img")) {
                    //for the Images

                    st = st.substring(3, st.length() - 1);


                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitHeight(150);
                    imageView.setFitWidth(200);


                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);


                    if (!cmd.equalsIgnoreCase(lblUserName.getText())) {

                        vbox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);


                        Text text1 = new Text("  " + cmd + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);

                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text(": Me ");
                        hBox.getChildren().add(text1);

                    }

                    Platform.runLater(() -> vbox.getChildren().addAll(hBox));


                } else {

                    TextFlow tempFlow = new TextFlow();

                    if (!cmd.equalsIgnoreCase(lblUserName.getText() + ":")) {
                        Text txtName = new Text(cmd + " ");
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);
                    }

                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200); //200

                    TextFlow flow = new TextFlow(tempFlow);

                    HBox hBox = new HBox(12); //12


                    if (!cmd.equalsIgnoreCase(lblUserName.getText() + ":")) {


                        vbox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);

                    } else {

                        Text text2 = new Text(fullMsg + ": Me");
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                    }

                    Platform.runLater(() -> vbox.getChildren().addAll(hBox));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addImgOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        writer.println(lblUserName.getText() + " " + "img" + filePath.getPath());
    }

    public void txtSendOnKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            btnSend.fire();
        }
    }
}
