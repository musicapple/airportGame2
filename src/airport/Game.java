package airport;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class Game extends Thread{
    private int delay = 30;
    private long pretime;
    private int cnt;
    private long startTime;    // 프로그램 실행 시작 시간
    private long endTime;   // 쓰레드 종료되는 시간

    private int firstEnemyDrawCount = 0;    // firstEnemy 적기 그려지는 횟수
    private int secondEnemyDrawCount = 0;   // secondEnemy 적기 그려지는 횟수
    private int thirdEnemyDrawCount = 0;    // thirdEnemy 적기 그려지는 횟수
    private int speedEnemyDrawCount = 0;    // speedEnemyDrawCount 적기 그려지는 횟수
    private int GameScreenCount = 0;        // GameScreen,GameScreenRed 구별하기위해 카운트

    private Image player = new ImageIcon("src/images/player.png").getImage();  // player 이미지 저장
    private int playerX, playerY;   // player의 위치
    private int playerWidth = player.getWidth(null);    // player 너비 지정
    private int playerHeight = player.getHeight(null);  // player 높이 지정
    private int playerSpeed = 10;   // 키 입력이 한번 인식됐을 때 플레이어가 이동할 거리

    private boolean up, down, right, left, shooting;  // 플레이어의 움직임을 제어할 변수
    private Image gameScreenRed = new ImageIcon("src/images/game_screenRed.png").getImage();
    private boolean isGameScreenRed;
    private boolean movingDrop;
    private boolean movingRight;
    private boolean movingLeft;
    private boolean movingUnder;
    private boolean movingOn;

    //---------------적 공격 선언 ----------------------------------------------------------
    ArrayList<PlayerAttack> playerAttackList = new ArrayList<>(); // 미사일이 하나만 발사되는게 아니라 여러개 발사됨.
    private PlayerAttack playerAttack; // 미사일

    ArrayList<FirstEnemyAttack> firstEnemyAttackList = new ArrayList<>();
    private FirstEnemyAttack firstEnemyAttack;

    ArrayList<SecondEnemyAttack> secondEnemyAttackList = new ArrayList<>(); // 총알이 여러개 발사됨.
    private SecondEnemyAttack secondEnemyAttack;    // secondEnemy 총알

//--------------- 적 기체 선언 ---------------------------------------------------------
    ArrayList<FirstEnemy> firstEnemyList = new ArrayList<>(); // firstEnemy가 여러번 등장함.
    private FirstEnemy firstEnemy; // 처음 등장하는 적기체

    ArrayList<SecondEnemy> secondEnemyList = new ArrayList<>(); // secondEnemy가 여러번 등장함.
    private SecondEnemy secondEnemy; // 두번째 등장하는 적기체

    ArrayList<ThirdEnemy> thirdEnemyList = new ArrayList<>(); // thirdEnemy가 여러번 등장함.
    private ThirdEnemy thirdEnemy;    // 세번째 등장하는 적기체

    ArrayList<SpeedEnemy> speedEnemyList = new ArrayList<>();   // speedEnemy가 여러번 등장함.
    private SpeedEnemy speedEnemy;  // 빠르게 등장해서 요격하고 사라지는 적기체
//-------------------폭발 선언--------------------------------------------------------------
    ArrayList<Boom> boomList = new ArrayList<>(); // 폭발을 여러번함.
    private Boom boom; // 적 격추했을 때 발생하는 폭발 이미지
//--------------------헤비 머신건(아이템) 선언------------------------------------------------
    private Item item;
//---------------------------------------------------------------------------------------
    @Override
    public void run() { // run메소드는 이 쓰레드를 시작할 시 실행될 내용
        cnt = 0;
        playerX = (Main.SCREEN_WIDTH+playerWidth) / 2;
        playerY = (Main.SCREEN_HEIGHT-playerHeight);
        startTime =  System.currentTimeMillis();
        while(true) {   // 쓰레드가 시작하면서 while안의 코드가 무한 반복.
            if(true) {
                try {
                    Thread.sleep(delay); // 30/1000초 동안 스레드는 일시정지 상태
                    keyProcess();
                    playerAttackProcess();
                    firstEnemyAppearProcess();
                    itemAppearProcess();
                    firstEnemyMoveProcess();
                    itemMoveProcess();
                    firstEnemyAttackProcess();
                    secondEnemyAppearProcess();
                    secondEnemyMoveProcess();
                    secondEnemyAttackProcess();
                    thirdEnemyAppearProcess();
                    thirdEnemyMoveProcess();
                    speedEnemyAppearProcess();
                    speedEnemyMoveProcess();

                    endTime=System.currentTimeMillis();
                    cnt++;  // 0.03초마다 증가
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void keyProcess() {
        if(up && playerY - playerSpeed > 0)
            playerY -= playerSpeed;
        if(down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT)
            playerY += playerSpeed;
        if(left && playerX - playerSpeed > 0)
            playerX -= playerSpeed;
        if(right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH)
            playerX += playerSpeed;
        if(shooting && cnt % 8 == 0) { // 0, 0.24, 0.48, 0.72, 0.96 ,... 이 때 눌러야 실행됨.
            playerAttack = new PlayerAttack(playerX+player.getWidth(null)/2, playerY - 10); // 미사일이 player 좌표의 50만큼 위에서부터 생성됨.
            playerAttackList.add(playerAttack);
        }
    }
    //------------- 적 기체 생성 -----------------------------------------------
    private void firstEnemyAppearProcess() {
        if(firstEnemyDrawCount < 10) {  // 적기 10번 등장
            if(endTime - startTime >= 10000 * firstEnemyDrawCount) {    // 0초에 첫번째 기체 등장 10초후 두번째 기체 등장
                firstEnemy = new FirstEnemy(50, 0, 250, 50, 50, new ImageIcon("src/images/firstEnemy.png").getImage());
                firstEnemyList.add(firstEnemy);
                firstEnemyDrawCount++;
            }
        }
    }
    private void firstEnemyMoveProcess() {
        for(int i =0; i<firstEnemyList.size(); i++) {
            firstEnemyList.get(i).move();
            if(firstEnemyList.get(i).posY > Main.SCREEN_HEIGHT) {   // 화면에서 사라진 firstEnemy 적기체 제거하여 메모리 재활용.
                firstEnemyList.remove(i);
            }
        }
    }
    private void secondEnemyAppearProcess() {  // secondEnemy 적 기체 생성
        if(secondEnemyDrawCount < 10) { // 적기 10번 등장
            if(endTime - startTime >= 10000*secondEnemyDrawCount+1000) { // 1초에 첫번째 기체 등장 11초후 두번째 기체 등장
                secondEnemy = new SecondEnemy(50, Main.SCREEN_WIDTH, 50, 50, 50, new ImageIcon("src/images/secondEnemy.png").getImage());
                secondEnemyList.add(secondEnemy);
                secondEnemyDrawCount++;
            }
        }
    }
    private void secondEnemyMoveProcess() {    // 생성된 모든 secondEnemy 적 기체 이동
        for(int i = 0; i<secondEnemyList.size(); i++) {
            secondEnemyList.get(i).move();
            if(secondEnemyList.get(i).posY > Main.SCREEN_HEIGHT) {
                secondEnemyList.remove(i);
            }
        }
    }
    private void thirdEnemyAppearProcess() {  // thirdEnemy 적 기체 생성
        if(thirdEnemyDrawCount < 10){   // 적기 10번 등장
            if(endTime - startTime >= 10000*thirdEnemyDrawCount +1000 ) {   // 1초에 첫번째 기체 등장 11초 후 두번째 기체 등장
                for (int i = 0; i < 6; i++) {   // 한번 등장할 때 6기체 등장
                    if(thirdEnemyDrawCount % 2 ==0) {
                        thirdEnemy = new ThirdEnemy(50, 100 + i * 100, -i * 50, 50, 50, new ImageIcon("src/images/thirdEnemy.png").getImage());
                    } else {
                        thirdEnemy = new ThirdEnemy(50, Main.SCREEN_WIDTH-i*100-200,-i*50, 50, 50, new ImageIcon("src/images/thirdEnemy.png").getImage());
                    }
                    thirdEnemyList.add(thirdEnemy);
                }
                thirdEnemyDrawCount++;
            }
        }
    }
    public void thirdEnemyMoveProcess() { // 생성된 모든 thirdEnemy 적 기체 이동
        for(int i=0; i<thirdEnemyList.size(); i++) {
            thirdEnemyList.get(i).move();
            if(thirdEnemyList.get(i).posY > Main.SCREEN_HEIGHT) {
                thirdEnemyList.remove(i);
            }
        }
    }
    public void speedEnemyAppearProcess() { // speedEnemy 적 기체 생성
        if (speedEnemyDrawCount < 10) {
            /*if (endTime - startTime > 10000 * (GameScreenCount + 1) - 2000) {     // 8초, 18초
                isGameScreenRed = true; // gameScreen -> gameScreenRed
                Timer loadingTimer = new Timer();
                TimerTask loadingTask = new TimerTask() {
                    private int cnt = 0;    // 두번 깜빡이게하기위해 count

                    @Override
                    public void run() {
                        if(cnt<2) {
                            isGameScreenRed = !isGameScreenRed; // 화면 전환
                            cnt++;
                        } else {
                            isGameScreenRed = false;
                            loadingTimer.cancel();
                        }
                    }
                };
                loadingTimer.scheduleAtFixedRate(loadingTask,800,800);  // isGameScreenRed가 true가 된 후 0.8초 후 run()실행. 0.8초마다 반복실행.
                GameScreenCount++;
            }*/
            if (endTime - startTime > 10000 * (speedEnemyDrawCount + 1) + 1000) {    // 11초, 21초, ..., 적기체 생성
                for (int i = 0; i < 2; i++) {
                    if (i % 2 == 0) {
                        speedEnemy = new SpeedEnemy(50, 150, Main.SCREEN_HEIGHT, 130, 130, new ImageIcon("src/images/speedEnemy.png").getImage());
                    } else {
                        speedEnemy = new SpeedEnemy(50, Main.SCREEN_WIDTH - 250, Main.SCREEN_HEIGHT, 130, 130, new ImageIcon("src/images/speedEnemy.png").getImage());
                    }
                    speedEnemyList.add(speedEnemy);
                }
                speedEnemyDrawCount++;
            }
        }
    }
    public void speedEnemyMoveProcess() {   // 생성된 모든 speedEnemy 적 기체 이동
        for(int i =0; i<speedEnemyList.size(); i++) {
            speedEnemyList.get(i).move();
            if(speedEnemyList.get(i).posY < 0-speedEnemyList.get(i).height) {
                speedEnemyList.remove(i);
                isGameScreenRed = false;    // speedEnemy가 화면 밖으로 나가면 GameScreen 정상으로
            }
        }
    }
    //---------------- 적기체 공격 메서드 --------------------------------------------
    private void playerAttackProcess() {    // 공격을 처리해주는 메서드
        for(int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            playerAttack.fire();

            if(playerAttackList.get(i).y < 0-playerAttackList.get(i).height) {  // player 총알 화면에서 나갈시 제거하여 메모리 재활용
                playerAttackList.remove(i);
            }
        }
        for(int i=0; i<firstEnemyList.size(); i++) {    // player 총알과 firstEnemy 충돌시 총알과 firstEnemy 제거
            firstEnemy = firstEnemyList.get(i);
            for(int j=0; j<playerAttackList.size(); j++) {
                playerAttack = playerAttackList.get(j);
            if(Crash(playerAttack.x+playerAttack.width/2,playerAttack.y+playerAttack.height/2, firstEnemy.posX+firstEnemy.width/2, firstEnemy.posY+firstEnemy.height/2,playerAttack.width,playerAttack.height, firstEnemy.width,firstEnemy.height)) {
                firstEnemyList.remove(i);   // firstEnemy 제거
                playerAttackList.remove(j); // 총알 제거
                boomList.add(new Boom(firstEnemy.posX, firstEnemy.posY, 80, 80));
            }
            }
        }
        for(int i=0; i<secondEnemyList.size(); i++) {    // player 총알과 secondEnemy 충돌시 총알과 secondEnemy 제거
            secondEnemy = secondEnemyList.get(i);
            for(int j=0; j<playerAttackList.size(); j++) {
                playerAttack = playerAttackList.get(j);
                if(Crash(playerAttack.x+playerAttack.width/2,playerAttack.y+playerAttack.height/2,secondEnemy.posX+secondEnemy.width/2, secondEnemy.posY+secondEnemy.height/2, playerAttack.width, playerAttack.height, secondEnemy.width, secondEnemy.height)){
                    secondEnemyList.remove(i);  // secondEnemy 제거
                    playerAttackList.remove(j); // 총알 제거
                    boomList.add(new Boom(secondEnemy.posX, secondEnemy.posY, 80, 80));
                }
            }
        }
        for(int i=0; i<thirdEnemyList.size(); i++) {
            thirdEnemy = thirdEnemyList.get(i);
            for(int j=0; j<playerAttackList.size(); j++) {
                playerAttack = playerAttackList.get(j);
                if(Crash(playerAttack.x+playerAttack.width/2, playerAttack.y+ playerAttack.height/2, thirdEnemy.posX+thirdEnemy.width/2, thirdEnemy.posY+thirdEnemy.height/2, playerAttack.width,playerAttack.height, thirdEnemy.width, thirdEnemy.height)){
                    thirdEnemyList.remove(i);
                    playerAttackList.remove(j);
                    boomList.add(new Boom(thirdEnemy.posX, thirdEnemy.posY, 80, 80));
                }
            }
        }
    }
    // -------------- 총알 생성 메서드 -----------------------------------------------
    private void firstEnemyAttackProcess() {   // firstEnemy 총알 생성 메서드
        for(int i=0; i<firstEnemyList.size(); i++) {
            firstEnemy = firstEnemyList.get(i);
            if(cnt % 20 == 0) { // 0, 0.6, 1.2, 1.8, ...초마다 총알 생성
                firstEnemyAttack = new FirstEnemyAttack(firstEnemy.posX, firstEnemy.posY, 10, 10, new ImageIcon("src/images/firstEnemy_bullet.png").getImage());
                firstEnemyAttackList.add(firstEnemyAttack);
            }
        }
        for(int i=0; i<firstEnemyAttackList.size(); i++) {
            firstEnemyAttack = firstEnemyAttackList.get(i);
            firstEnemyAttack.fire();
        }
    }
    private void secondEnemyAttackProcess() {
        for(int i=0; i<secondEnemyList.size(); i++){    // secondEnemy 총알 생성
            secondEnemy = secondEnemyList.get(i);
            if(cnt % 20 == 0) {
                secondEnemyAttack = new SecondEnemyAttack(secondEnemy.posX,secondEnemy.posY,10,10,new ImageIcon("src/images/secondEnemy_bullet.png").getImage());
                secondEnemyAttackList.add(secondEnemyAttack);
            }
        }
        for(int i=0; i<secondEnemyAttackList.size(); i++) {
            secondEnemyAttack=secondEnemyAttackList.get(i);
            secondEnemyAttack.fire();
        }
    }
    //----------------------헤비 머신건(아이템) 생성-------------------------------------
    private void itemAppearProcess(){
        if(item == null){
            item = new Item(0,0);
        }
    }
    //----------------------헤비 머신건(아이템) 움직임-----------------------------------
    private void itemMoveProcess(){
        System.out.println("움직임시작.");
        if(item.posX+item.width>=Main.SCREEN_WIDTH){ // 오른쪽 벽 부딪침
            System.out.println("오른쪽 벽 부딪침");
            movingRight=true;
            movingLeft=false;
            movingUnder=false;
            movingDrop=false;
        }
        if(movingRight) {
            item.rightMove();
        }
        if(item.posY+item.height>Main.SCREEN_HEIGHT){   // 아래쪽 벽 부딪침
            System.out.println("아래쪽 벽 부딪침");
            movingRight=false;
            movingLeft=false;
            movingUnder=true;
            movingDrop=false;
        }
        if(movingUnder){
            item.underMove();
        }
        if(item.posX<0){    // 왼쪽 벽 튕길 때
            System.out.println("왼쪽 벽 부딪침");
            movingRight = false;
            movingLeft = true;
            movingUnder = false;
            movingDrop = false;
        }
        if(movingLeft) {
            item.leftMove();
        }
        if(item.posY<=0){   // 위쪽 벽 튕길 때 및 아이템 생길 때
            System.out.println("위쪽 벽 부딪침");
            movingRight = false;
            movingLeft = false;
            movingUnder = false;
            movingDrop = true;
        }
        if(movingDrop) {
            item.dropMove();
        }
    }
    //--------------------- 충돌 판정 메서드 -------------------------------------------
    public boolean Crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2){
        boolean check = false;
        if (Math.abs((x1 + w1 / 2)  - (x2 + w2 / 2))  <  (w2 / 2 + w1 / 2)
                && Math.abs((y1 + h1 / 2)  - (y2 + h2 / 2))  <  (h2 / 2 + h1/ 2)){
            check = true;//위 값이 true면 check에 true를 전달합니다.
        }else {check = false;}
        return check;
    }
    //--------------------- 그림을 그려주는 메서드 --------------------------------------
    public void gameDraw(Graphics g) {
        if(isGameScreenRed) {   // speedEnemy가 등장할 때 배경화면 색 변하기
            g.drawImage(gameScreenRed,0,0,null);
        }
        playerDraw(g);
        firstEnemyDraw(g);
        secondEnemyDraw(g);
        thirdEnemyDraw(g);
        speedEnemyDraw(g);
        firstEnemyAttackDraw(g);
        secondEnemyAttackDraw(g);
        boomDraw(g);
        itemDraw(g);
    }
    public void playerDraw(Graphics g) {
        g.drawImage(player,playerX,playerY,playerWidth, playerHeight,null);
        for(int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            g.drawImage(playerAttack.image,playerAttack.x,playerAttack.y,playerAttack.width,playerAttack.height,null);
        }
    }
    public void firstEnemyDraw(Graphics g) {
        for (int i = 0; i < firstEnemyList.size(); i++) {
            firstEnemy = firstEnemyList.get(i);
            g.drawImage(firstEnemy.image, firstEnemy.posX, firstEnemy.posY, firstEnemy.width, firstEnemy.height, null);   // 처음 등장하는 적기체 그리기
        }
    }
    public void secondEnemyDraw(Graphics g) {
        for(int i=0; i < secondEnemyList.size(); i++) {
            secondEnemy = secondEnemyList.get(i);
            g.drawImage(secondEnemy.image, secondEnemy.posX, secondEnemy.posY, secondEnemy.width, secondEnemy.height, null);
        }
    }
    public void thirdEnemyDraw(Graphics g) {
        for(int i=0; i<thirdEnemyList.size(); i++) {
            thirdEnemy = thirdEnemyList.get(i);
            g.drawImage(thirdEnemy.image, thirdEnemy.posX, thirdEnemy.posY, thirdEnemy.width, thirdEnemy.height, null);
        }
    }
    public void speedEnemyDraw(Graphics g) {
        for(int i= 0 ; i<speedEnemyList.size(); i++) {
            speedEnemy = speedEnemyList.get(i);
            g.drawImage(speedEnemy.image,speedEnemy.posX,speedEnemy.posY,speedEnemy.width,speedEnemy.height,null);
        }
    }
    public void firstEnemyAttackDraw(Graphics g) {
        for(int i = 0; i<firstEnemyAttackList.size(); i++) {
            firstEnemyAttack = firstEnemyAttackList.get(i);
            g.drawImage(firstEnemyAttack.image,firstEnemyAttack.posX,firstEnemyAttack.posY,firstEnemyAttack.width,firstEnemyAttack.height,null);
        }
    }
    public void secondEnemyAttackDraw(Graphics g) {
        for(int i=0; i<secondEnemyAttackList.size(); i++) {
            secondEnemyAttack = secondEnemyAttackList.get(i);
            g.drawImage(secondEnemyAttack.image,secondEnemyAttack.posX,secondEnemyAttack.posY,secondEnemyAttack.width,secondEnemyAttack.height,null);
        }
    }
    public void boomDraw(Graphics g) {  // 적기체 연쇄폭발 이미지
        for (int i = 0; i < boomList.size(); i++) {
            boom = boomList.get(i);
            //System.out.println(boom.cnt);
            if (boom.cnt <= 8) {
                g.drawImage(boom.image1, boom.posX, boom.posY, null);
            } else if (boom.cnt <= 32) {
                g.drawImage(boom.image2, boom.posX, boom.posY, null);
            } else if (boom.cnt <= 64) {
                g.drawImage(boom.image3, boom.posX, boom.posY, null);
            } else if (boom.cnt <= 96) {
                g.drawImage(boom.image4, boom.posX, boom.posY, null);
            } else if (boom.cnt <= 128) {
                g.drawImage(boom.image5, boom.posX, boom.posY, null);
            } else if (boom.cnt <= 160) {
                g.drawImage(boom.image6, boom.posX, boom.posY, null);
            } else if (boom.cnt <= 192) {
                g.drawImage(boom.image7, boom.posX, boom.posY, null);
            } else if (boom.cnt <= 224) {
                g.drawImage(boom.image8, boom.posX, boom.posY, null);
            }
            boom.show();
            if(boom.cnt==224){
                boomList.remove(i);
            }
        }
    }
    public void itemDraw(Graphics g) {
        if (item != null && item.image != null) {
            System.out.println("그리기 시작");
            g.drawImage(item.image, item.posX, item.posY, null);
        }
    }
    // -----------------------------------------------------------------------------------
    public void setUp(boolean up) {
        this.up = up;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public void setRight(boolean right) {
        this.right = right;
    }
    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
}