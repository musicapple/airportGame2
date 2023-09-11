package airport;

import javax.swing.*;
import java.awt.*;

public class Item {
    protected Image image = new ImageIcon("src/images/item.png").getImage();
    protected int posX;
    protected int posY;
    protected int width = image.getWidth(null);
    protected int height = image.getHeight(null);

    public Item(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
    public void dropMove(){
        posX += 15;
        posY += 10;
    }
    public void rightMove(){
        posX -= 7;
        posY += 10;
    }
    public void underMove(){
        posX -= 20;
        posY -= 13;
    }
    public void leftMove(){
        posX += 13;
        posY -= 14;
    }
    public void onMove(){
        posX += 25;
        posY -= 5;
    }
}

