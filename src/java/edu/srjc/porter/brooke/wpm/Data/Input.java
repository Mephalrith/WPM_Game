/*
Brooke Porter
xsadrithx@yahoo.com
5/10/18

Final_Porter_Brooke

CS 17.11, Section 6991: Sean Kirkpatrick

This class handles input during the game. It takes all key presses,
compares them to the given quote, and decides whether to move the player
or not. It calculates and updates the wpm count asynchronously, and whether or not to
change the level.
*/

package edu.srjc.porter.brooke.wpm.Data;

import edu.srjc.porter.brooke.wpm.Controller;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static javafx.scene.input.KeyCode.*;

class Input
{
    private Timer timer = new Timer();
    private Start start = null;
    private Date time = null;
    private Controller controller = null;
    private Scene scene = null;
    private Stage primaryStage = null;
    private Double wpm = 0.0;
    private Boolean isRunning = true;
    private ArrayList<Character> errors = new ArrayList<>();
    private ArrayList<Character> typedEntries = new ArrayList<>();
    private ArrayList<Character> charList = new ArrayList<>();
    private Double threshold = 20.0;


    Input(Scene scene, Stage primaryStage, Controller controller, Double threshold)
    {
        this.scene = scene;
        this.primaryStage = primaryStage;
        this.controller = controller;
        this.threshold = threshold;
        this.charList = controller.charList;
        controller.threshold = threshold;

        this.time = new Date();
        if (isRunning)
        {
            timer.schedule(new Task(), 1000);
        }

        scene.setOnKeyPressed(event ->
        {
            Character typedKey = '\0';

            if (event.getCode().isModifierKey() ||
                    event.getCode().isFunctionKey() ||
                    event.getCode().isNavigationKey() ||
                    event.getCode().equals(ENTER) ||
                    event.getCode().equals(CAPS) ||
                    event.getCode().equals(DELETE) ||
                    event.getCode().equals(BACK_SPACE))
            {
                return;
            }
            else
            {
                if (event.getCode().equals(ESCAPE))
                {
                    Platform.exit();
                    System.exit(0);
                }

                try
                {
                    typedKey = event.getText().charAt(0);
                }
                catch (Exception ex)
                {
                    System.out.printf("Bad key: %s\n", event.getCode());
                }
            }

            if (charList.size() > 0 && typedKey.equals(charList.get(0)))
            {
                charList.remove(0);
                typedEntries.add(typedKey);

                controller.movePlayer(wpm);
            }
            else
            {
                if (charList.size() > 0 && !typedKey.equals(charList.get(0)))
                {
                    errors.add(typedKey);
                    if (controller.cthulhuCanvas.getLayoutX() >= (controller.playerCanvas.getLayoutX() - 90))
                    {
                        controller.playerCanvas.setOpacity(0.4);
                        controller.playerAlive = false;
                    }
                    deadCheck();
                }
            }

            deadCheck();
            controller.moveNPC();
            controller.moveCthulhu();
            controller.setQuoteText(charList);
        });
    }



    private void handleWPM()
    {
        Date currentTime = new Date();
        Integer numSeconds = (int) ((currentTime.getTime() - time.getTime()) / 1000);
        Double grossWPM = typedEntries.size() / 5.0;

        if (numSeconds != 0)
        {
            Double numMinutes = numSeconds / 60.0;
            Double netWPM = (grossWPM - (double) errors.size()) / numMinutes;

            if (netWPM >= 0)
            {
                this.wpm = netWPM;
            }
            else
            {
                this.wpm = 0.0;
            }

            String result = String.format("WPM: %.1f", netWPM);
            controller.setWpmLabel(result);
        }

        if (charList.size() == 0 && controller.playerAlive)
        {
            stopTimer();

            try
            {
                clearState();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            nextLevel();
        }
        else
        {
            deadCheck();
            controller.moveNPC();
            controller.moveCthulhu();
        }
    }





    public class Task extends TimerTask
    {
        @Override
        public void run()
        {
            Platform.runLater(() ->
            {
                handleWPM();
                if (isRunning)
                {
                    try
                    {
                        timer.schedule(new Task(), 1000);
                    }
                    catch (Exception ex)
                    {
                        System.out.printf("Error with new timer: %s\n", ex);
                    }
                }
            });
        }
    }

    private void stopTimer()
    {
        isRunning = false;
        timer.cancel();
    }

    private void deadCheck()
    {
        if (!controller.playerAlive)
        {
            if (isRunning)
            {
                stopTimer();
            }

            try
            {
                clearState();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            lostLevel();
        }
    }





    private void clearState() throws IOException
    {
        FXMLLoader initialLoader = new FXMLLoader(getClass().getResource("../UI/InitialLayout.fxml"));
        Parent root = initialLoader.load();
        this.start = initialLoader.getController();
        scene.setRoot(root);
    }

    private void lostLevel()
    {
        String result = String.format("%.1f", wpm);
        String text = "Cthulhu ate you!\n" +
                "You typed at " + result + " wpm\n" +
                "Cthulhu moved at " + threshold + " wpm\n\n" +
                "Press ENTER to try again, or ESCAPE to quit";
        start.handleStart(scene, primaryStage, text, threshold);
    }

    private void nextLevel()
    {
        String result = String.format("%.1f", wpm);
        String text = "You escaped Cthulhu!\n" +
                "You typed at " + result + " wpm\n\n" +
                "Press ENTER to move on, or ESCAPE to quit";
        start.handleStart(scene, primaryStage, text, this.threshold + 20.0);
    }
}
