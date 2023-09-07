package airport;

import java.awt.*;

public class EnemyAttack {
    protected int attack;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected Image image;

    public EnemyAttack(int attack,int posX,int posY,int width,int height,Image image) {
        this.attack = attack;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.image = image;
    }
    public void fire(){}
}

class FirstEnemyAttack extends EnemyAttack {

    public FirstEnemyAttack(int posX, int posY, int width, int height, Image image) {
        super(5, posX, posY, width, height, image);
    }

    @Override
    public void fire() {
        posY += 3;
    }
}
class SecondEnemyAttack extends EnemyAttack {

    public SecondEnemyAttack(int posX,int posY,int width,int height,Image image){
        super(5,posX,posY,width,height,image);
    }

    @Override
    public void fire() {
        posY += 3;
    }
}