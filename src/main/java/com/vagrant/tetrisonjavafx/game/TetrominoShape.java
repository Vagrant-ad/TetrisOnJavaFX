package com.vagrant.tetrisonjavafx.game;

import javafx.scene.paint.Color;

import java.util.Random;

public enum TetrominoShape {
    //使用三维数组枚举俄罗斯方块的所有位置与排列
    I(new int[][][]{
            {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
            {{1, 0}, {1, 1}, {1, 2}, {1, 3}},
    }, Color.CYAN.deriveColor(0, 1, 0.8, 1)),

    J(new int[][][]{
            {{0, 0}, {0, 1}, {1, 1}, {2, 1}},
            {{1, 0}, {2, 0}, {1, 1}, {1, 2}},
            {{0, 1}, {1, 1}, {2, 1}, {2, 2}},
            {{1, 0}, {1, 1}, {0, 2}, {1, 2}}
    }, Color.BLUE.deriveColor(0, 1, 0.8, 1)),

    L(new int[][][]{
            {{2, 0}, {0, 1}, {1, 1}, {2, 1}},
            {{1, 0}, {1, 1}, {1, 2}, {2, 2}},
            {{0, 1}, {1, 1}, {2, 1}, {0, 2}},
            {{0, 0}, {1, 0}, {1, 1}, {1, 2}}
    }, Color.ORANGE.deriveColor(0, 1, 0.8, 1)),

    O(new int[][][]{
            {{1, 0}, {2, 0}, {1, 1}, {2, 1}}
    }, Color.YELLOW.deriveColor(0, 1, 0.8, 1)),

    S(new int[][][]{
            {{1, 0}, {2, 0}, {0, 1}, {1, 1}},
            {{1, 0}, {1, 1}, {2, 1}, {2, 2}},
            {{1, 1}, {2, 1}, {0, 2}, {1, 2}},
            {{0, 0}, {0, 1}, {1, 1}, {1, 2}}
    }, Color.GREEN.deriveColor(0, 1, 0.8, 1)),

    T(new int[][][]{
            {{1, 0}, {0, 1}, {1, 1}, {2, 1}},
            {{1, 0}, {1, 1}, {2, 1}, {1, 2}},
            {{0, 1}, {1, 1}, {2, 1}, {1, 2}},
            {{1, 0}, {0, 1}, {1, 1}, {1, 2}}
    }, Color.PURPLE.deriveColor(0, 1, 0.8, 1)),

    Z(new int[][][]{
            {{0, 0}, {1, 0}, {1, 1}, {2, 1}},
            {{2, 0}, {1, 1}, {2, 1}, {1, 2}},
            {{0, 1}, {1, 1}, {1, 2}, {2, 2}},
            {{1, 0}, {0, 1}, {1, 1}, {0, 2}}
    }, Color.RED.deriveColor(0, 1, 0.8, 1));

    public final int[][][] states;
    public final Color color;
    private static final Random RANDOM = new Random();
    private static final TetrominoShape[] VALUES = values();

    TetrominoShape(int[][][] states, Color color) {
        this.states = states;
        this.color = color;
    }

    public int[][] getRotation(int rotationNum) {
        return states[rotationNum % states.length];
    }

    public static TetrominoShape getRandomShape() {
        return VALUES[RANDOM.nextInt(VALUES.length)];
    }

    public int getNumOfRotation() {
        return states.length;
    }
}
