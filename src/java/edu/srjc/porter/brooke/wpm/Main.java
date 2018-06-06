/*
Brooke Porter
xsadrithx@yahoo.com
4/28/18

Final_Porter_Brooke

CS 17.11, Section 6991: Sean Kirkpatrick

This program is a word per minute (wpm) game in which you must
type faster than Cthulhu to progress through each level.
*/

package edu.srjc.porter.brooke.wpm;

import edu.srjc.porter.brooke.wpm.Data.Start;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InitialLayout.fxml"));
        Parent root = fxmlLoader.load();
        Start start = fxmlLoader.getController();

        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Final Project - WPM Game");
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event ->
        {
            Platform.exit();
            System.exit(0);
        });

        String text = "Press ENTER to start\n\nPress ESCAPE to quit";
        start.handleStart(scene, primaryStage, text, 20.0);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
