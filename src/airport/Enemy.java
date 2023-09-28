package airport;

import java.awt.*;

public class Enemy {    // 적기의 공통되는 요소를 가진 class
    protected int hp;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected Image image;
    protected int cnt=0;

    public Enemy(int hp,int posX,int posY, Image image) {
        this.hp = hp;
        this.posX = posX;
        this.posY = posY;
        this.width = image.getWidth(null);    // 적기체 너비
        this.height = image.getHeight(null);   // 적기체 높이
        this.image = image;
    }
    public void move() {}
    public void show() {
        cnt++;
    }
}

class FirstEnemy extends Enemy {

    public FirstEnemy(int hp,int posX,int posY,Image image) {
        super(hp,posX,posY,image);
    }

    @Override
    public void move() {
        posX += 5;  // 적기 좌측에서 우측으로 이동
        posY += 5;  // 적기 위에서 아래로 이동
    }
}
class SecondEnemy extends Enemy {

    public SecondEnemy(int hp,int posX, int posY, Image image){
        super(hp,posX,posY,image);
    }

    @Override
    public void move() {
        posX -= 5;  // 적기 우측에서 좌측으로 이동
        posY += 5;  // 적기 위에서 아래로 이동
    }
}
class ThirdEnemy extends Enemy {

    public ThirdEnemy(int hp,int posX, int posY,Image image) {
        super(hp,posX,posY,image);
    }

    @Override
    public void move() {
        posY += 5;  // 적기 위에서 아래로 이동
    }
}
class SpeedEnemy extends Enemy {

    public SpeedEnemy(int hp,int posX,int posY,Image image) {
        super(hp,posX,posY,image);
    }
    @Override
    public void move() {
        posY -= 10;  // 적기 아래에서 위로 이동
    }
}
class Boss extends Enemy {
    protected Image imageAttacked;

    public Boss(int hp, int posX, int posY, Image image, Image imageAttacked) {
        super(hp, posX, posY, image);
        this.imageAttacked = imageAttacked;
    }
    public void move() {
        posY += 3;  // 적기 위에서 아래로 이동
    }
}
