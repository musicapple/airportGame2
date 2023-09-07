package airport;

import javax.swing.*;
import java.awt.*;

public class Boom {
    protected Image image1 = new ImageIcon("src/images/boom/boom1.png").getImage();
    protected Image image2 = new ImageIcon("src/images/boom/boom2.png").getImage();
    protected Image image3 = new ImageIcon("src/images/boom/boom3.png").getImage();
    protected Image image4 = new ImageIcon("src/images/boom/boom4.png").getImage();
    protected Image image5 = new ImageIcon("src/images/boom/boom5.png").getImage();
    protected Image image6 = new ImageIcon("src/images/boom/boom6.png").getImage();
    protected Image image7 = new ImageIcon("src/images/boom/boom7.png").getImage();
    protected Image image8 = new ImageIcon("src/images/boom/boom8.png").getImage();
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected int cnt=0;    // 적기체 폭발 카운터 재기위함

    public Boom(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public void show() {    // 적기체 폭발 카운터 재기위함
        cnt++;
    }
}
