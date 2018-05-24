/*
Brooke Porter
xsadrithx@yahoo.com
4/29/18

Final_Porter_Brooke

CS 17.11, Section 6991: Sean Kirkpatrick

This is the FXML controller for the game layout. It handles the visual aspects
and creates sprites for the two boats and cthulhu.
*/

package edu.srjc.porter.brooke.wpm;

import edu.srjc.porter.brooke.wpm.Data.Quote;
import edu.srjc.porter.brooke.wpm.Data.Sprite;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    public Canvas npcCanvas;
    public Canvas playerCanvas;
    public Canvas cthulhuCanvas;
    public ImageView frontWave;
    public ImageView midWave;
    public ImageView backWave;
    public Label label;
    public Label wpmLabel;

    private Quote quote = new Quote();
    private Sprite npc = new Sprite(100, 300, "/edu/srjc/porter/brooke/wpm/UI/Images/boatNPC.png");
    private Sprite cthulhu = new Sprite(0, 0, "/edu/srjc/porter/brooke/wpm/UI/Images/cthulhu.png");
    private Sprite player = new Sprite(150, 380, "/edu/srjc/porter/brooke/wpm/UI/Images/boatPlayer.png");

    public ArrayList<Character> charList = quote.charList();
    public Double threshold = 20.0;
    public Boolean playerAlive = true;

    public void initialize(URL url, ResourceBundle rb)
    {
        final GraphicsContext npcGC = npcCanvas.getGraphicsContext2D();
        npcGC.clearRect(0, 0, npcCanvas.getWidth(), npcCanvas.getHeight());
        final GraphicsContext cthulhuGC = cthulhuCanvas.getGraphicsContext2D();
        cthulhuGC.clearRect(0, 0, cthulhuCanvas.getWidth(), cthulhuCanvas.getHeight());
        final GraphicsContext playerGC = playerCanvas.getGraphicsContext2D();
        playerGC.clearRect(0, 0, playerCanvas.getWidth(), playerCanvas.getHeight());

        cthulhuCanvas.setLayoutX(-300.0);
        cthulhuCanvas.setLayoutY(700);

        npc.render(npcGC);
        cthulhu.render(cthulhuGC);
        player.render(playerGC);

        rockingWave(backWave);
        rockingBoat(npcCanvas);
        rockingWave(midWave);
        rockingBoat(playerCanvas);
        rockingWave(frontWave);

        setQuoteText(charList);
        setWpmLabel("0.0");
    }




    private void rockingBoat(Canvas boat)
    {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

        Random randTransform = new Random();
        int trans = randTransform.nextInt(2) + 1;
        Random randRotate = new Random();
        int rot = randRotate.nextInt(3) + 1;

        Random millisRand = new Random();
        int millis = millisRand.nextInt(2000) + 1600;

        final KeyValue floatYValue = new KeyValue(boat.layoutYProperty(), trans, Interpolator.EASE_BOTH);
        final KeyValue rotateValue = new KeyValue(boat.rotateProperty(), -rot, Interpolator.EASE_BOTH);

        final KeyFrame floatYFrame = new KeyFrame(Duration.millis(millis), floatYValue, rotateValue);

        timeline.getKeyFrames().add(floatYFrame);
        timeline.play();
    }

    private void rockingWave(ImageView wave)
    {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

        Random randRotate = new Random();
        int rot = randRotate.nextInt(2) + 1;

        Random millisRand = new Random();
        int millis = millisRand.nextInt(2000) + 1600;

        final KeyValue floatYValue = new KeyValue(wave.yProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue rotateValue = new KeyValue(wave.rotateProperty(), rot, Interpolator.EASE_BOTH);

        final KeyFrame floatYFrame = new KeyFrame(Duration.millis(millis), floatYValue, rotateValue);

        timeline.getKeyFrames().add(floatYFrame);
        timeline.play();
    }





    public void setQuoteText(ArrayList<Character> list)
    {
        StringBuilder text = new StringBuilder();

        list.forEach(letter ->
        {
            text.append(letter.toString());
        });
        setQuoteText(text.toString());
    }

    private void setQuoteText(String quoteText)
    {
        Platform.runLater(() ->
        {
            this.label.setText(quoteText);
        });
    }

    public void setWpmLabel(String text)
    {
        Platform.runLater(() ->
        {
            this.wpmLabel.setText(text);
        });
    }





    public void movePlayer(Double wpm)
    {
        Platform.runLater(() ->
        {
            Double currentX = playerCanvas.getLayoutX();
            if (cthulhuCanvas.getLayoutX() >= (currentX - 90))
            {
                this.playerCanvas.setOpacity(0.4);
                this.playerAlive = false;
            }
            else
            {
                if (wpm != 0)
                {
                    this.playerCanvas.setLayoutX(currentX + Math.log(wpm));
                }
            }
        });
    }

    public void moveNPC()
    {
        Platform.runLater(() ->
        {
            Double currentX = npcCanvas.getLayoutX();
            if (cthulhuCanvas.getLayoutX() >= (currentX))
            {
                this.npcCanvas.setOpacity(0.4);
            }
            else
            {
                this.npcCanvas.setLayoutX(currentX + Math.log(threshold) - 1);
            }
        });
    }

    public void moveCthulhu()
    {
        Platform.runLater(() ->
        {
            Double currentX = cthulhuCanvas.getLayoutX();
            Double currentY = cthulhuCanvas.getLayoutY();
            if (currentY >= 260)
            {
                this.cthulhuCanvas.setLayoutY(currentY - 4);
            }
            this.cthulhuCanvas.setLayoutX(currentX + Math.log(threshold));
        });
    }
}
