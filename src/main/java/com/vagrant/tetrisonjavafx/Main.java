package com.vagrant.tetrisonjavafx;

import com.vagrant.tetrisonjavafx.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
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