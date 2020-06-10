package me.drawlin.ember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger("ember");

    private static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        controller = new Controller();

        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Ember by drawlin");
        primaryStage.setScene(new Scene(root, 350, 420));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.getIcons().add(new Image(getClass().getResource("/assets/fire.png").toString()));
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe");
        launch(args);
    }

}