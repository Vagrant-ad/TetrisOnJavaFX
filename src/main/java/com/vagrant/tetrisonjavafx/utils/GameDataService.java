package com.vagrant.tetrisonjavafx.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vagrant.tetrisonjavafx.Config;
import com.vagrant.tetrisonjavafx.model.GameRecorder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameDataService {
    //使用Gson解析.json文件储存的数据
    //所有成员均为static
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static List<GameRecorder> loadGameRecord() {
        if (!Files.exists(Config.GAME_RECORD_FILE_NAME)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(Config.GAME_RECORD_FILE_NAME)) {

//            File file = new File(Config.GAMERECORD_FILENAME);
//            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//            优化代码，使用更现代的Files.newBufferedReader方法构建reader

            Type ListOfGameRecorderType = new TypeToken<List<GameRecorder>>() {
            }.getType();
            List<GameRecorder> records = gson.fromJson(reader, ListOfGameRecorderType);//反序列化
            reader.close();
            if (records == null) {
                return new ArrayList<>();
            }
            Collections.sort(records);
            return records;
        } catch (IOException e) {
            System.err.println("存档文件读取失败" + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } catch (com.google.gson.JsonSyntaxException e) {
            System.out.println("JSON格式错误" + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private static void saveRecord(List<GameRecorder> records) {
        ensurePathExists();
        try (Writer writer = Files.newBufferedWriter(Config.GAME_RECORD_FILE_NAME)) {
            gson.toJson(records, writer);
        } catch (IOException e) {
            System.err.println("保存失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addAndSaveRecord(GameRecorder newRecord) {
        List<GameRecorder> records = loadGameRecord();
        records.add(newRecord);
        Collections.sort(records);
        saveRecord(records);
    }

    private static void ensurePathExists() {
        try {
            if (!Files.exists(Config.GAME_RECORD_PATH_NAME)) {
                Files.createDirectories(Config.GAME_RECORD_PATH_NAME);
            }
        } catch (IOException e) {
            System.err.println("无法创建存档目录: " + Config.GAME_RECORD_PATH_NAME.toString());
            e.printStackTrace();
        }
    }
}
