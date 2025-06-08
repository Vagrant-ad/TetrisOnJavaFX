package com.vagrant.tetrisonjavafx.ui;

import com.vagrant.tetrisonjavafx.Config;
import com.vagrant.tetrisonjavafx.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    private final Stage primaryStage;
    private Scene mainMenuScene;
    private Scene gameScene;
    private Scene gameRecordScene;
    private MainMenuController mainMenuController;
    private GameScreenController gameScreenController;
    private GameRecordScreenController gameRecordScreenController;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createScene(String fxmlFile, Object controller) throws IOException {
        //加载FXML
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        if (controller != null) {
            fxmlLoader.setController(controller);
        }
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        //加载CSS
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/vagrant/tetrisonjavafx/css/style.css")).toExternalForm());
        return scene;
    }

    public void showMainMenu() throws IOException {
        if (mainMenuScene == null || mainMenuController == null) {
            mainMenuController = new MainMenuController(this);
            mainMenuScene = createScene("/com/vagrant/tetrisonjavafx/fxml/MainMenu.fxml", mainMenuController);
        }
        primaryStage.setScene(mainMenuScene);
        primaryStage.setWidth(550);
        primaryStage.setHeight(800);
    }

    public void showGameRecordScreen() throws IOException {
        if (gameRecordScene == null || gameRecordScreenController == null) {
            //不存在则create
            gameRecordScreenController = new GameRecordScreenController(this);
            gameRecordScene = createScene("/com/vagrant/tetrisonjavafx/fxml/GameRecordScreen.fxml", gameRecordScreenController);
        } else {
            //存在则刷新
            gameRecordScreenController.refresh();
        }
        primaryStage.setScene(gameRecordScene);
        primaryStage.setWidth(550);
        primaryStage.setHeight(800);
    }

    public void showGameScreen() throws IOException {
        if (gameScene == null || gameScreenController == null) {
            //不存在则create
            gameScreenController = new GameScreenController(this);
            gameScene = createScene("/com/vagrant/tetrisonjavafx/fxml/GameScreen.fxml", gameScreenController);
        } else {
            //存在则重新加载
            gameScreenController.resetGame();
        }
        primaryStage.setScene(gameScene);
        primaryStage.setWidth(550);
        primaryStage.setHeight(800);
        gameScreenController.startGame();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
