package com.vagrant.tetrisonjavafx.game;

import com.vagrant.tetrisonjavafx.Config;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
public class Tetromino {
    private TetrominoShape shape;
    private int curX;
    private int curY;
    private int rotationNum;

    public Tetromino(TetrominoShape shape) {
        this.shape = shape;
        this.curX = Config.BOARD_WIDTH / 2 - 2;
        this.curY = 0;
        this.rotationNum = 0;
        if (shape == TetrominoShape.O) {
            this.curY--;
        }
    }

    public void moveLeft() {
        curX--;
    }

    public void moveRight() {
        curX++;
    }

    public void moveDown() {
        curY++;
    }

    public void moveUp() {
        curY--;
    }

    public void rotateClockwise() {
        rotationNum = (rotationNum + 1) % shape.getNumOfRotation();
    }

    public void rotateAntiClockwise() {
        rotationNum--;
        if(rotationNum < 0) {
            rotationNum = shape.getNumOfRotation() - 1;
        }
    }

    public void render(GraphicsContext gc) {
        gc.setFill(shape.color);
        int[][] locations = getLocations();
        for(int[] loc : locations){
            int x = curX + loc[0];
            int y = curY + loc[1];
            double px = x * Config.BLOCK_SIZE;
            double py = y * Config.BLOCK_SIZE;
            if(x >=0 && x <=Config.BOARD_WIDTH&& y >=0 && y <=Config.BOARD_HEIGHT){
                gc.fillRect(px,py,Config.BLOCK_SIZE,Config.BLOCK_SIZE);
                gc.setStroke(Color.GRAY);
                gc.setLineWidth(1);
                gc.strokeRect(px,py,Config.BLOCK_SIZE,Config.BLOCK_SIZE);
            }
        }
    }

    public void renderPreview(GraphicsContext gc) {
        gc.setFill(shape.color);
        int[][] locations = getLocations();
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        //以下为将下一个Tetromino在画布正中间显示的方式
        for(int[] loc : locations){
            minX = Math.min(minX,loc[0]);
            minY = Math.min(minY,loc[1]);
            maxX = Math.max(maxX,loc[0]);
            maxY = Math.max(maxY,loc[1]);
        }
        double previewSize = gc.getCanvas().getWidth()/4.0;
        double boardWidth = (maxX - minX +1) * previewSize;//方块整体边界宽高
        double boardHeight = (maxY - minY +1) * previewSize;
        double startX =(gc.getCanvas().getWidth()-boardWidth)/2.0;
        double startY =(gc.getCanvas().getHeight()-boardHeight)/2.0;
        for(int[] loc : locations){
            double x = startX + (loc[0] - minX) * previewSize;
            double y = startY + (loc[1] - minY) * previewSize;
            gc.fillRect(x,y,previewSize,previewSize);
            gc.setStroke(Color.GRAY);
            gc.strokeRect(x,y,previewSize,previewSize);
        }
    }

    public int getCurX() {
        return curX;
    }

    public void setCurX(int curX) {
        this.curX = curX;
    }

    public int getCurY() {
        return curY;
    }

    public void setCurY(int curY) {
        this.curY = curY;
    }

    public TetrominoShape getShape() {
        return shape;
    }
    public int getRotationNum(){
        return rotationNum;
    }
    public void setRotationNum(int rotationNum) {
        this.rotationNum = rotationNum;
    }
    public int[][] getLocations(){
        return shape.getRotation(rotationNum);
    }
}
