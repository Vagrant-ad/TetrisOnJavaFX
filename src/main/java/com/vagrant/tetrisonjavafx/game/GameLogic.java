package com.vagrant.tetrisonjavafx.game;

import com.vagrant.tetrisonjavafx.Config;
import com.vagrant.tetrisonjavafx.ui.GameScreenController;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GameLogic {
    private final Board board;
    private final ScoreManager scoreManager;
    private Tetromino curTetromino;
    private Tetromino nexTetromino;
    private final GameScreenController gameScreenController;

    private AnimationTimer gameLoop;
    private long lastDropTime;
    private double gapTime = Config.LEVEL_SPEED[0];
    private boolean isPaused;
    private boolean isGameOver;

    private int level;
    private int linesClearedThisLevel;

    private final Set<KeyCode> keys = new HashSet<>();

    private long moveStartTime = 0; //对应a和d，用于实现DAS（Delayed Auto Shift）机制
    private long lastMoveTime = 0;
    private KeyCode lastMoveKey = null;

    private boolean sIsPressed = false;
    private long spressTime = 0;
    private double speedMuti = 1.0;

    public GameLogic(GameScreenController gameScreenController) {
        this.gameScreenController = gameScreenController;
        this.board = new Board();
        this.scoreManager = new ScoreManager();
//        buildNewTetromino();  转移至resetGame()中防止重复调用
        startGameLoop();
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused || isGameOver) {
                    return;
                }
                handleInput(now);
                if ((now - lastDropTime) / 1_000_000_000.0 > gapTime / speedMuti) {
                    //除以10亿纳秒得秒，下落间隔除以下落倍速
                    if (!moveTetrominoDown()) {//方块已经无法下移即到底
                        lockTetromino();
                        int linesCleared = board.clearLine();
                        updateLevelAndScore(linesCleared);
                        buildNewTetromino();
                        if (isGameOver) {
                            stopGame();
                            gameScreenController.showGameOverScreen(scoreManager.getCurScore());
                        }
                    }

                    lastDropTime = now;
                }
                gameScreenController.renderGame();
            }
        };
    }

    public void buildNewTetromino() {
        curTetromino = Objects.requireNonNullElseGet(nexTetromino, () -> new Tetromino(TetrominoShape.getRandomShape()));
        nexTetromino = new Tetromino(TetrominoShape.getRandomShape());
        speedMuti = 1.0;//重置速度
        if (!canPlace(curTetromino.getCurX(), curTetromino.getCurY(), curTetromino.getLocations())) {
            isGameOver = true;
        }
    }

    public void handleKeyPressed(KeyCode keyCode) {
        if (isGameOver || isPaused)
            return;
        if (keyCode == KeyCode.ESCAPE) {
            doOrCancelPause();
            return;
        }
        if ((keyCode == KeyCode.A || keyCode == KeyCode.D) && lastMoveKey == null) {
            lastMoveKey = keyCode;
            moveStartTime = System.nanoTime();
            lastMoveTime = System.nanoTime();
            if (keyCode == KeyCode.A) {
                moveTetromino(-1);
            } else {
                moveTetromino(1);
            }
        }
        keys.add(keyCode);
        if (keyCode == KeyCode.Q) {
            rotateTetromino(false);
        } else if (keyCode == KeyCode.E) {
            rotateTetromino(true);
        } else if (keyCode == KeyCode.S) {
            if (!sIsPressed) {
                sIsPressed = true;
                spressTime = System.nanoTime();
                speedMuti = Config.DROP_MULTIPLIER_STAGE1;//加速下落1阶段
                if (moveTetrominoDown()) {//立刻下落一次增强手感
                    lastDropTime = System.nanoTime();
                } else {
                    lockTetromino();
                    if (!isGameOver) {
                        buildNewTetromino();
                    }
                }
            }
        }
    }

    public void handleKeyReleased(KeyCode keyCode) {
        keys.remove(keyCode);
        if (keyCode == KeyCode.S) {
            sIsPressed = false;
            speedMuti = 1.0;
        }
        //正在处理的移动键A和D
        if (keyCode == lastMoveKey) {
            lastMoveKey = null;
            moveStartTime = 0;
        }
    }

    public void handleInput(long now) {
        if (keys.contains(KeyCode.S)) {
            if (now - spressTime > Config.DROP_STAGE2_THRESHOLD) {
                speedMuti = Config.DROP_MULTIPLIER_STAGE2;//加速下落2阶段
            }
        }
        //DAS和ARR逻辑
        if (lastMoveKey != null && keys.contains(lastMoveKey)) {
            if (now - moveStartTime > Config.DAS_DELAY && now - lastMoveTime > Config.ARR_GAPTIME) {
                if (lastMoveKey == KeyCode.A) {
                    moveTetromino(-1);
                } else if (lastMoveKey == KeyCode.D) {
                    moveTetromino(1);
                }
                lastMoveTime = now;
            }
        }
    }

    private void moveTetromino(int dx) {
        int xx = curTetromino.getCurX() + dx;
        int yy = curTetromino.getCurY();
        if (canPlace(xx, yy, curTetromino.getLocations())) {
            if (dx < 0)
                curTetromino.moveLeft();
            if (dx > 0)
                curTetromino.moveRight();
        }
    }

    private void rotateTetromino(boolean isClockwise) {
        int curRotation = curTetromino.getRotationNum();
        if (isClockwise) {
            curTetromino.rotateClockwise();
        } else {
            curTetromino.rotateAntiClockwise();
        }
        if (!canPlace(curTetromino.getCurX(), curTetromino.getCurY(), curTetromino.getLocations())) {
            curTetromino.setRotationNum(curRotation);//若无法放置则还原
        }
    }

    private boolean canPlace(int x, int y, int[][] locs) {
        //判断该位置是否能放置方块
        for (int[] block : locs) {
            int xx = block[0] + x;
            int yy = block[1] + y;
            if (xx < 0 || xx >= Config.BOARD_WIDTH || yy >= Config.BOARD_HEIGHT)
                return false;
            if (y < 0)
                continue;
            if (board.getColor(xx, yy) != null)
                return false;
        }
        return true;
    }

    //自动下落
    private boolean moveTetrominoDown() {
        curTetromino.moveDown();
        if (canPlace(curTetromino.getCurX(), curTetromino.getCurY(), curTetromino.getLocations())) {
            return true;
        }
        curTetromino.moveUp();
        return false;
    }

    private void lockTetromino() {
        var locs = curTetromino.getLocations();
        for (var loc : locs) {
            int x = curTetromino.getCurX() + loc[0];
            int y = curTetromino.getCurY() + loc[1];

            if (y >= 0) {
                board.placeBlock(x, y, curTetromino.getShape().color);
            }
        }
    }

    private void updateLevelAndScore(int linesCleared) {
        if (linesCleared > 0) {
            scoreManager.addScore(linesCleared);
            linesClearedThisLevel += linesCleared;
            if (linesClearedThisLevel >= Config.LINES_PER_LEVEL) {
                level++;
                linesClearedThisLevel -= Config.LINES_PER_LEVEL;
                int speedIndex = Math.min(level, Config.LEVEL_SPEED.length - 1);
                gapTime = Config.LEVEL_SPEED[speedIndex];
            }
        }
    }

    public void startGame() {
        resetGame();
        lastDropTime = System.nanoTime();
        gameLoop.start();
    }

    public void stopGame() {
        gameLoop.stop();
    }

    public void continueGame() {
        gameLoop.start();
    }

    public void resetGame() {
        board.reset();
        scoreManager.resetCurScore();
        isGameOver = false;
        isPaused = false;
        level = 0;
        linesClearedThisLevel = 0;
        gapTime = Config.LEVEL_SPEED[level];
        keys.clear();
        sIsPressed = false;
        speedMuti = 1.0;
        buildNewTetromino();
    }

    public void doOrCancelPause() {
        isPaused = !isPaused;
        if (isPaused) {//暂停
            stopGame();
            gameScreenController.showOrClosePauseMenu(true);
        } else {//继续游戏
            lastDropTime = System.nanoTime();
            continueGame();
            gameScreenController.showOrClosePauseMenu(false);
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Board getBoard() {
        return board;
    }

    public Tetromino getCurTetromino() {
        return curTetromino;
    }

    public Tetromino getNexTetromino() {
        return nexTetromino;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }
}
