package com.vagrant.tetrisonjavafx;

import javafx.scene.paint.Color;


import java.nio.file.Path;
import java.nio.file.Paths;

//常量储存类Config
public class Config {
    public static final int BOARD_WIDTH = 10; // 游戏区域宽度（列数）
    public static final int BOARD_HEIGHT = 20; // 游戏区域高度（行数）
    public static final int BLOCK_SIZE = 30; // 每个方块的像素大小

    public static final int NEXT_BLOCK_AREA_WIDTH = 4 * BLOCK_SIZE; // 下一个方块显示区域宽度
    public static final int NEXT_BLOCK_AREA_HEIGHT = 4 * BLOCK_SIZE; // 下一个方块显示区域高度

    public static final double DROP_MULTIPLIER_STAGE1 = 5.0;  // S键一段加速倍率
    public static final double DROP_MULTIPLIER_STAGE2 = 15.0; // S键二段加速倍率
    public static final long DROP_STAGE2_THRESHOLD = 300_000_000L; // S键长按进入二段加速的阈值 (0.3秒)

    public static final long DAS_DELAY = 150_000_000L; //DAS延迟自动平移时间
    public static final long ARR_GAPTIME = 30_000_000L; //自动重复率ARR

    public static final int LINES_PER_LEVEL = 10; //每消除10行升一级
    public static final double[] LEVEL_SPEED = {
            //共20级
            0.9, 0.8, 0.65, 0.5, 0.4, 0.3, 0.2, 0.15, 0.1, 0.08,
            0.07, 0.06, 0.05, 0.05, 0.04, 0.04, 0.03, 0.03, 0.02, 0.02
    };

    public static final Color BOARD_BACKGROUND_COLOR = Color.rgb(20, 20, 30); // 游戏背景色
    public static final Color GRID_LINE_COLOR = Color.rgb(50, 50, 70); // 网格线颜色

    public static final String PIXEL_FONT_NAME = "LanaPixel"; // CSS中定义的像素字体名
    public static final String CALCULATOR_FONT_NAME = "Digital-7 Italic"; // CSS中定义的计算器字体名

    //存档文件地址与文件名
    public static final Path GAME_RECORD_PATH_NAME = Paths.get(System.getProperty("user.home"), ".TetrisOnJavaFX");
    public static final Path GAME_RECORD_FILE_NAME = GAME_RECORD_PATH_NAME.resolve("TetrisGameOnJavaFXData.json");
}
