package com.vagrant.tetrisonjavafx;

import com.vagrant.tetrisonjavafx.ui.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/vagrant/tetrisonjavafx/fxml/MainMenu.fxml"));
        Path data = Paths.get("data");
        if(!Files.exists(data)){
            Files.createDirectories(data);
        }
        SceneManager sceneManager = new SceneManager(primaryStage);
        primaryStage.setTitle("俄罗斯方块");
        primaryStage.setResizable(false);
        sceneManager.showMainMenu();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}