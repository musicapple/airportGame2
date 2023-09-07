package airport;

import javax.swing.*;
import java.awt.*;

public class PlayerAttack {
    protected Image image = new ImageIcon("src/images/player_attack.png").getImage();
    protected int x, y;
    protected int width = image.getWidth(null);
    protected int height = image.getHeight(null);
    protected int attack = 5; // 공격력

    public PlayerAttack(int x,int y){   // player로부터 미사일이 얼마나 떨어져있는지
        this.x = x;
        this.y = y;
    }
    public void fire() {    // 발사 메서드
        this.y -= 15;
    }
}
