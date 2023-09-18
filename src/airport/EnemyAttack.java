package airport;

import java.awt.*;

public class EnemyAttack {
    protected int attack;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected Image image;

    public EnemyAttack(int attack,int posX,int posY,Image image) {
        this.attack = attack;
        this.posX = posX;
        this.posY = posY;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        this.image = image;
    }
    public void fire(){}
}

class FirstEnemyAttack extends EnemyAttack {

    public FirstEnemyAttack(int posX, int posY, Image image) {
        super(5, posX, posY, image);
    }

    @Override
    public void fire() {
        posY += 6;
    }
}
class SecondEnemyAttack extends EnemyAttack {

    public SecondEnemyAttack(int posX,int posY,Image image){
        super(5,posX,posY,image);
    }

    @Override
    public void fire() {
        posY += 6;
    }
}
class ThirdEnemyAttack extends EnemyAttack {
    public ThirdEnemyAttack(int posX, int posY,Image image) {
        super(5, posX, posY,image);
    }

    @Override
    public void fire() {
        posY += 12;
    }
}
class SpeedEnemyAttack extends EnemyAttack {

    public SpeedEnemyAttack(int posX, int posY, Image image) {
        super(5,posX, posY, image);
    }

    public void fire0() {
        posY += 6;  // 아래방향
    }
    public void fire1() {
        posX += 2;  // 제4사분면
        posY += 5;
    }
    public void fire2() {
        posX += 4;  // 제4사분면
        posY += 4;
    }
    public void fire3() {
        posX += 6;  // 제4사분면
        posY += 3;
    }
    public void fire4() {
        posX += 6;  // 우측방향
    }
    public void fire5() {
        posX += 4;  // 제1사분면
        posY -= 2;
    }
    public void fire6() {
        posX += 4;  // 제1사분면
        posY -= 4;
    }
    public void fire7() {
        posX += 2;  // 제1사분면
        posY -= 6;
    }
    public void fire8() {
        posY -= 6;  // 위에 방향
    }
    public void fire9() {
        posX -= 2;
        posY -= 6;  // 제2사분면
    }
    public void fire10() {
        posX -= 4;
        posY -= 5;  // 제2사분면
    }
    public void fire11() {
        posX -= 6;
        posY -= 4;  // 제2사분면
    }
    public void fire12() {  // 좌측 방향
        posX -= 6;
    }
    public void fire13() {
        posX -= 5;
        posY += 3;
    }
    public void fire14() {
        posX -= 4;
        posY += 4;
    }
    public void fire15() {
        posX -= 2;
        posY += 5;
    }
}