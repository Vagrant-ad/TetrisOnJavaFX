package com.vagrant.tetrisonjavafx.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController {
    @FXML
    private VBox rootMenuBox;
    @FXML
    private Label titleLabel;
    @FXML
    private Button playButton;
    @FXML
    private Button recordButton;
    @FXML
    private Button exitButton;

    private final SceneManager sceneManager;

    public MainMenuController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        //应用CSS样式
        titleLabel.getStyleClass().add("game-title-label");
        playButton.getStyleClass().add("pixel-button");
        recordButton.getStyleClass().add("pixel-button");
        exitButton.getStyleClass().add("pixel-button");
    }

    @FXML
    public void handlePlayButtonAction(ActionEvent actionEvent) {
        try {
            sceneManager.showGameScreen();
        } catch (IOException e) {
            System.err.println("无法加载游戏界面" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleExitButtonAction(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);//确保程序退出
    }

    @FXML
    public void handleRecordButtonAction(ActionEvent actionEvent) {
        try {
            sceneManager.showGameRecordScreen();
        } catch (IOException e) {
            System.err.println("无法加载历史记录" + e.getMessage());
            e.printStackTrace();
        }
    }
}