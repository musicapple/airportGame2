package airport;

import javax.swing.*;
import java.awt.*;

public class BossBoom {
    protected Image image1 = new ImageIcon("src/images/bossBoom/bossBoom1.png").getImage();
    protected Image image2 = new ImageIcon("src/images/bossBoom/bossBoom2.png").getImage();
    protected Image image3 = new ImageIcon("src/images/bossBoom/bossBoom3.png").getImage();
    protected Image image4 = new ImageIcon("src/images/bossBoom/bossBoom4.png").getImage();
    protected Image image5 = new ImageIcon("src/images/bossBoom/bossBoom5.png").getImage();
    protected Image image6 = new ImageIcon("src/images/bossBoom/bossBoom6.png").getImage();
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected int cnt=0;

    public BossBoom(int posX,int posY, int width, int height){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void show() {    // 적기체 폭발 카운터 재기위함
        cnt++;
    }
}
