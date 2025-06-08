package com.vagrant.tetrisonjavafx.ui;

import com.vagrant.tetrisonjavafx.Config;
import com.vagrant.tetrisonjavafx.game.GameLogic;
import com.vagrant.tetrisonjavafx.model.GameRecorder;
import com.vagrant.tetrisonjavafx.utils.GameDataService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;


public class GameScreenController {
    @FXML
    private BorderPane rootGamePane;
    @FXML
    public Canvas gameCanvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label scoreDisplayLabel;
    @FXML
    private Label nextBlockLabel;
    @FXML
    private Canvas nextBlockCanvas;
    @FXML
    private Button restartButton;
    @FXML
    private Button pauseButton;

    private final SceneManager sceneManager;
    private GraphicsContext gameGc;
    private GraphicsContext nextBlockGc;
    private GameLogic gameLogic;
    private Stage pauseStage;
    private Stage gameOverStage;

    public GameScreenController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        gameGc = gameCanvas.getGraphicsContext2D();
        nextBlockGc = nextBlockCanvas.getGraphicsContext2D();
        //设置尺寸
        gameCanvas.setWidth(Config.BLOCK_SIZE * Config.BOARD_WIDTH);
        gameCanvas.setHeight(Config.BLOCK_SIZE * Config.BOARD_HEIGHT);
        nextBlockCanvas.setWidth(Config.NEXT_BLOCK_AREA_WIDTH);
        nextBlockCanvas.setHeight(Config.NEXT_BLOCK_AREA_HEIGHT);
        //应用CSS样式
        scoreLabel.getStyleClass().add("info-panel-label");
        nextBlockLabel.getStyleClass().add("info-panel-label");
        scoreDisplayLabel.getStyleClass().add("score-display-label");
        restartButton.getStyleClass().add("pixel-button");
        pauseButton.getStyleClass().add("pixel-button");

        gameLogic = new GameLogic(this);
        //与scoreManager的curScore绑定
        scoreDisplayLabel.textProperty().bind(gameLogic.getScoreManager().getCurScoreProperty().asString());
        //在scene构建完成后再执行
        Platform.runLater(() -> {
            if (gameCanvas.getScene() != null) {
                gameCanvas.getScene().setOnKeyPressed(this::handleKeyPressed);
                gameCanvas.getScene().setOnKeyReleased(this::handleKeyReleased);
                gameCanvas.requestFocus();
            } else {
                System.err.println("gameScene不存在");
            }
        });
    }

    public void renderGame() {
        //该方法将被AnimationTimer示例gameLoop实时调用
        if (gameLogic == null || gameGc == null || nextBlockGc == null) {
            return;
        }
        gameGc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        nextBlockGc.clearRect(0, 0, nextBlockCanvas.getWidth(), nextBlockCanvas.getHeight());

        gameLogic.getBoard().render(gameGc);
        if (!gameLogic.isGameOver() && gameLogic.getCurTetromino() != null) {
            gameLogic.getCurTetromino().render(gameGc);
        }
        if (gameLogic.getNexTetromino() != null) {
            nextBlockGc.setFill(Config.BOARD_BACKGROUND_COLOR.darker());
            nextBlockGc.fillRect(0, 0, nextBlockCanvas.getWidth(), nextBlockCanvas.getHeight());
            gameLogic.getNexTetromino().renderPreview(nextBlockGc);
        }
    }


    public void resetGame() {
        if (gameLogic != null) {
            gameLogic.resetGame();
            if (gameOverStage != null && gameOverStage.isShowing()) {
                gameOverStage.hide();
            }
            renderGame();
        }
    }

    public void startGame() {
        if (gameLogic != null) {
            gameLogic.startGame();
            renderGame();
        }
    }

    private void handleKeyPressed(KeyEvent keyEvent) {
        //将keyCode传给gameLogic处理
        if (gameLogic != null) {
            gameLogic.handleKeyPressed(keyEvent.getCode());
        }
        keyEvent.consume();
    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        if (gameLogic != null) {
            gameLogic.handleKeyReleased(keyEvent.getCode());
        }
        keyEvent.consume();
    }

    @FXML
    public void handleRestartButton(ActionEvent actionEvent) {
        startGame();
    }

    @FXML
    public void handlePauseButton(ActionEvent actionEvent) {
        if (gameLogic != null && !gameLogic.isGameOver()) {
            gameLogic.doOrCancelPause();
        }
    }

    public void showGameOverScreen(int curScore) {
        //由于没有单独设置fxml，部分css样式在此单独设置
        if (gameOverStage == null) {
            //若第一次gameover,创建gameOverStage初始化
            gameOverStage = new Stage(StageStyle.TRANSPARENT);//隐藏顶部状态栏
            gameOverStage.initModality(Modality.APPLICATION_MODAL);//模态窗口
            gameOverStage.initOwner(sceneManager.getPrimaryStage());
            VBox gameOverBox = new VBox();
            gameOverBox.setAlignment(Pos.CENTER);
            //半透明效果
            gameOverBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 50px; -fx-background-radius: 10;");
            Label gameOverLabel = new Label("游戏结束");
            gameOverLabel.getStyleClass().add("info-panel-label");
            //设置一个HBox，放置得分显示
            HBox scoreBox = new HBox(10);
            scoreBox.setAlignment(Pos.CENTER);
            Label scoreTextLabel = new Label("最终得分:");
            scoreTextLabel.getStyleClass().add("info-panel-label");
            Label scoreLabel = new Label(String.valueOf(curScore));
            scoreLabel.setId("score-label");
            scoreLabel.setStyle("-fx-font-family: '" + Config.CALCULATOR_FONT_NAME + "'; -fx-font-size: 28px; -fx-text-fill: #00FF00;");
            scoreBox.getChildren().addAll(scoreTextLabel, scoreLabel);

            Button restartButton = new Button("重新开始");
            restartButton.getStyleClass().add("pixel-button");
            restartButton.setOnAction(event -> {
                gameOverStage.hide();
                sceneManager.getPrimaryStage().getScene().getRoot().setEffect(null);
                startGame();
            });
            Button mainMenuButton = new Button("返回主菜单");
            mainMenuButton.getStyleClass().add("pixel-button");
            mainMenuButton.setOnAction(event -> {
                gameOverStage.hide();
                sceneManager.getPrimaryStage().getScene().getRoot().setEffect(null);
                try {
                    sceneManager.showMainMenu();
                } catch (IOException e) {
                    System.err.println("无法返回主菜单" + e.getMessage());
                    e.printStackTrace();
                }
            });
            gameOverBox.getChildren().addAll(gameOverLabel, scoreBox, restartButton, mainMenuButton);
            Scene gameOverScene = new Scene(gameOverBox);
            gameOverScene.setFill(Color.TRANSPARENT);
            gameOverScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/vagrant/tetrisonjavafx/css/style.css")).toExternalForm());
            gameOverStage.setScene(gameOverScene);
        } else {
            //玩家已经进入过gameover界面，gameOverStage已经存在
            Label scoreUpdateLabel = (Label) gameOverStage.getScene().getRoot().lookup("#score-label");
            scoreUpdateLabel.setText(String.valueOf(curScore));
        }
        //存档
        gameLogic.getScoreManager().getCurScoreProperty().set(curScore);
        GameDataService.addAndSaveRecord(new GameRecorder(curScore));
        //为主面板设置高斯模糊
        sceneManager.getPrimaryStage().getScene().getRoot().setEffect(new GaussianBlur(10));
        gameOverStage.show();
    }

    public void showOrClosePauseMenu(boolean doPause) {
        if (doPause) {//暂停
            if (pauseStage == null) {
                pauseStage = new Stage(StageStyle.TRANSPARENT);
                pauseStage.initModality(Modality.APPLICATION_MODAL);
                pauseStage.initOwner(sceneManager.getPrimaryStage());

                VBox pauseBox = new VBox();
                pauseBox.setAlignment(Pos.CENTER);
                pauseBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 50px; -fx-background-radius: 10;");

                Label pauseTitleLabel = new Label("游戏暂停");
                pauseTitleLabel.getStyleClass().add("info-panel-label");
                Button continueGame = new Button("继续游戏");
                continueGame.getStyleClass().add("pixel-button");
                continueGame.setOnAction(event -> gameLogic.doOrCancelPause());
                Button mainMenuButton = new Button("返回主菜单");
                mainMenuButton.getStyleClass().add("pixel-button");
                mainMenuButton.setOnAction(event -> {
                    gameLogic.stopGame();
                    pauseStage.hide();
                    sceneManager.getPrimaryStage().getScene().getRoot().setEffect(null);
                    try {
                        sceneManager.showMainMenu();
                    } catch (IOException e) {
                        System.err.println("无法返回主菜单" + e.getMessage());
                        e.printStackTrace();
                    }
                });
                Button exitGameButton = new Button("退出游戏");
                exitGameButton.getStyleClass().add("pixel-button");
                exitGameButton.setOnAction(event -> {
                    Platform.exit();
                    System.exit(0);
                });
                pauseBox.getChildren().addAll(pauseTitleLabel, continueGame, mainMenuButton, exitGameButton);
                pauseBox.setSpacing(10);
                Scene pauseScene = new Scene(pauseBox);
                pauseScene.setFill(Color.TRANSPARENT);
                pauseScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/vagrant/tetrisonjavafx/css/style.css")).toExternalForm());
                pauseStage.setScene(pauseScene);
            }
            sceneManager.getPrimaryStage().getScene().getRoot().setEffect(new GaussianBlur(10));
            pauseStage.show();
        } else {//退出暂停
            pauseStage.hide();
            sceneManager.getPrimaryStage().getScene().getRoot().setEffect(null);
        }
    }


    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public GraphicsContext getGameGc() {
        return gameGc;
    }

    public GraphicsContext getNextBlockGc() {
        return nextBlockGc;
    }

    public BorderPane getRootGamePane() {
        return rootGamePane;
    }
}
