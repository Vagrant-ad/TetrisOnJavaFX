package com.vagrant.tetrisonjavafx.ui;

import com.vagrant.tetrisonjavafx.model.GameRecorder;
import com.vagrant.tetrisonjavafx.utils.GameDataService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class GameRecordScreenController {
    @FXML
    public Label highestScoreDisplayLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label highestScoreLabel;
    @FXML
    private TableView<GameRecorder> recordTable;
    @FXML
    private TableColumn<GameRecorder,String> rankColumn;
    @FXML
    private TableColumn<GameRecorder,Integer> scoreColumn;
    @FXML
    private TableColumn<GameRecorder,String> dataColumn;
    @FXML
    private Button backButton;

    private final SceneManager sceneManager;

    public GameRecordScreenController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        recordTable.getStyleClass().add("pixel-table");
        titleLabel.getStyleClass().add("page-title-label");
        highestScoreLabel.getStyleClass().add("info-panel-label");
        backButton.getStyleClass().add("pixel-button");
        recordTable.getStyleClass().add("pixel-table");

        /*使用AtomicInteger应对Lambda表达式要求捕获变量实际为final的要求，
        AtomicInteger对象本身为final，但可以通过方法改变封装整数值*/
        AtomicInteger rank=new AtomicInteger(1);
        rankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(rank.getAndIncrement())));
        recordTable.itemsProperty().addListener((obs,oldval,newval) ->rank.set(1));
        recordTable.sortPolicyProperty().set(tv ->{//tv即tableview
            FXCollections.sort(tv.getItems());
            rank.set(1);//排序后重置
            return true;
        });
        //先排序再显示信息
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    public void refresh() {
        List<GameRecorder> records = GameDataService.loadGameRecord();
        ObservableList<GameRecorder> observableRecords = FXCollections.observableList(records);
        if(records !=null){
            highestScoreDisplayLabel.setText(String.valueOf(records.getFirst().getScore()));
        }else{
            highestScoreDisplayLabel.setText("0");
        }
        AtomicInteger rank = new AtomicInteger(1);
        rankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(rank.getAndIncrement())));
        recordTable.setItems(observableRecords);
    }

    @FXML
    public void handleBackButtonAction(ActionEvent actionEvent) {
        try {
            sceneManager.showMainMenu();
        } catch (IOException e) {
            System.out.println("无法返回主菜单" + e.getMessage());
            e.printStackTrace();
        }
    }
}
