package com.vagrant.tetrisonjavafx.game;

import com.vagrant.tetrisonjavafx.Config;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board {
    private final Color[][] grid;

    public Board() {
        grid = new Color[Config.BOARD_WIDTH][Config.BOARD_HEIGHT];
        for (int i = 0; i < Config.BOARD_WIDTH; i++) {
            for (int j = 0; j < Config.BOARD_HEIGHT; j++) {
                grid[i][j] = null;
            }
        }
    }

    public boolean isValidPosition(int x, int y) {
        if (y < 0) {
            return x >= 0 && x < Config.BOARD_WIDTH;
        }
        return x >= 0 && x < Config.BOARD_WIDTH && y < Config.BOARD_HEIGHT && grid[x][y] == null;
    }

    public void placeBlock(int x, int y, Color color) {
        if (y >= 0 && y < Config.BOARD_HEIGHT && x < Config.BOARD_WIDTH)
            grid[x][y] = color;
    }

    public int clearLine() {
        int linesCleared = 0;
        for (int y = Config.BOARD_HEIGHT - 1; y >= 0; y--) {
            boolean lineFull = true;
            for (int x = 0; x < Config.BOARD_WIDTH; x++) {
                if (grid[x][y] == null) {
                    lineFull = false;
                    break;
                }
            }
            if (lineFull) {
                linesCleared++;
                for (int row = y; row > 0; row--) {
                    for (int col = 0; col < Config.BOARD_WIDTH; col++) {
                        grid[col][row] = grid[col][row - 1];
                    }
                }
                //首行重新设置为null
                for (int col = 0; col < Config.BOARD_WIDTH; col++) {
                    grid[col][0] = null;
                }
                y++;//当前行消去后上一行下移
            }
        }
        return linesCleared;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Config.BOARD_BACKGROUND_COLOR);
        gc.fillRect(0, 0, Config.BLOCK_SIZE * Config.BOARD_WIDTH, Config.BLOCK_SIZE * Config.BOARD_HEIGHT);

        gc.setStroke(Config.GRID_LINE_COLOR);
        gc.setLineWidth(1);
        for (int i = 0; i < Config.BOARD_WIDTH; i++) {
            gc.strokeLine(i * Config.BLOCK_SIZE, 0, i * Config.BLOCK_SIZE, Config.BLOCK_SIZE * Config.BOARD_HEIGHT);
        }
        for (int j = 0; j < Config.BOARD_HEIGHT; j++) {
            gc.strokeLine(0, j * Config.BLOCK_SIZE, Config.BLOCK_SIZE * Config.BOARD_WIDTH, j * Config.BLOCK_SIZE);
        }
        //渲染方块颜色
        for (int i = 0; i < Config.BOARD_WIDTH; i++) {
            for (int j = 0; j < Config.BOARD_HEIGHT; j++) {
                if (grid[i][j] != null) {
                    gc.setFill(grid[i][j]);
                    gc.fillRect(i * Config.BLOCK_SIZE, j * Config.BLOCK_SIZE, Config.BLOCK_SIZE, Config.BLOCK_SIZE);
                    gc.setStroke(Color.GRAY);
                    gc.strokeRect(i * Config.BLOCK_SIZE, j * Config.BLOCK_SIZE, Config.BLOCK_SIZE, Config.BLOCK_SIZE);
                }
            }
        }
    }

    public void reset() {
        for (int i = 0; i < Config.BOARD_WIDTH; i++) {
            for (int j = 0; j < Config.BOARD_HEIGHT; j++) {
                grid[i][j] = null;
            }
        }
    }

    public Color getColor(int x, int y) {
        if (x >= 0 && x < Config.BOARD_WIDTH && y >= 0 && y < Config.BOARD_HEIGHT)
            return grid[x][y];
        return null;
    }

    public boolean isGameOver() {
        for (int j = 0; j < Config.BOARD_HEIGHT; j++) {
            if (grid[0][j] != null)
                return true;
        }
        return false;
    }
}
