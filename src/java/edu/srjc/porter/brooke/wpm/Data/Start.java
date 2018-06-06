/*
Brooke Porter
xsadrithx@yahoo.com
5/18/18

Final_Porter_Brooke

CS 17.11, Section 6991: Sean Kirkpatrick

This class handles the initial state of game, and the methods
for starting a new level. It is used by Input.java to change levels.
It is also the controller for the initial layout FXML.
*/

package edu.srjc.porter.brooke.wpm.Data;

import edu.srjc.porter.brooke.wpm.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.ESCAPE;

public class Start
{
    @FXML
    public Label stateLabel;

    private Scene scene = null;
    private Stage primaryStage = null;

    public Start()
    {
    }

    public void handleStart(Scene scene, Stage primaryStage, String text, Double threshold)
    {
        this.scene = scene;
        this.primaryStage = primaryStage;

        stateLabel.setText(text);

        scene.setOnKeyPressed(event ->
        {
            if (event.getCode().equals(ENTER))
            {
                try
                {
                    startGame(threshold);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Platform.exit();
                    System.exit(1);
                }
            }
            else
            {
                if (event.getCode().equals(ESCAPE))
                {
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
    }

    private void startGame(Double threshold) throws IOException
    {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/GameLayout.fxml"));
        Parent root = gameLoader.load();
        Controller controller = gameLoader.getController();
        scene.setRoot(root);

        new Input(scene, primaryStage, controller, threshold);
    }
}
