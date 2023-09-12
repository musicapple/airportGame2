package airport;

import javax.swing.*;
import java.awt.*;

public class PlayerAttack {
    //protected Image image = new ImageIcon("src/images/player_attack.png").getImage();
    protected Image image;
    protected int x, y;
    protected int width;
    protected int height;
    protected int attack = 5; // 공격력

    public PlayerAttack(Image image,int x,int y){   // player로부터 미사일이 얼마나 떨어져있는지
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }
    public void fire() {    // 발사 메서드
        this.y -= 15;
    }
}
