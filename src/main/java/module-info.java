module com.vagrant.tetrisonjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.google.gson;
    requires java.desktop;

    opens com.vagrant.tetrisonjavafx to javafx.fxml,javafx.graphics;
    opens com.vagrant.tetrisonjavafx.ui to javafx.fxml;
    exports com.vagrant.tetrisonjavafx;
    exports com.vagrant.tetrisonjavafx.game;
    exports com.vagrant.tetrisonjavafx.ui;
    exports com.vagrant.tetrisonjavafx.model;
    exports com.vagrant.tetrisonjavafx.utils;
    opens com.vagrant.tetrisonjavafx.model to com.google.gson;
    opens com.vagrant.tetrisonjavafx.game to javafx.fxml, javafx.graphics;
}