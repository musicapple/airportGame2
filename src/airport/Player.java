package airport;

import java.awt.*;

public class Player {
    protected int hp;
    protected int posX;
    protected int posY;
    protected Image image;
    protected int width;
    protected int height;
    protected int playerSpeed;
    protected int life;

    public Player(int hp, int posX, int posY, Image image,int playerSpeed){
        this.hp = hp;
        this.posX = posX;
        this.posY = posY;
        this.image = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.playerSpeed = playerSpeed;
        this.life = 3;
    }
}
