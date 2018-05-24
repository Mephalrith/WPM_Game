/*
Brooke Porter
xsadrithx@yahoo.com
5/2/18

Final_Porter_Brooke

CS 17.11, Section 6991: Sean Kirkpatrick

This class takes an image and converts it into a Sprite,
giving it a method to draw the image.
*/

package edu.srjc.porter.brooke.wpm.Data;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite
{
    private Image image;
    private double positionX;
    private double positionY;
    private double width;
    private double height;

    public Sprite(double posX, double posY, String path)
    {
        this.image = new Image(path);

        this.width = image.getWidth();
        this.height = image.getHeight();

        this.positionX = posX;
        this.positionY = posY;
    }

    public void render(GraphicsContext gc)
    {
        gc.drawImage(image, positionX, positionY);
    }
}
