package airport;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import static airport.Main.airportGame;

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
    private int bossDrawCount = 0;          // boss 적기 그려지는 횟수
    private int GameScreenCount = 0;        // GameScreen,GameScreenRed 구별하기위해 카운트
    private int lifeCount = 2;              // player가 가지는 총 생명

    private boolean up, down, right, left, shooting;  // 플레이어의 움직임을 제어할 변수
    private Image gameScreenRed = new ImageIcon("src/images/game_screenRed.png").getImage();
    private boolean isGameScreenRed;
    private boolean movingDrop;
    private boolean movingRight;
    private boolean movingLeft;
    private boolean movingUnder;
    private boolean itemCollisionWithPlayer; // 아이템과 플레이가 충돌했을때
    private boolean isPlayerHidden;
    private boolean isGameClear;
    private boolean isGameOver;
    private boolean isBossAttacked;
    private boolean isPlayerAttacked;
    //---------------player 선언 ----------------------------------------------------------
    private Player player = new Player(150, (Main.SCREEN_WIDTH+25) / 2, (Main.SCREEN_HEIGHT-50),new ImageIcon("src/images/player.png").getImage(),new ImageIcon("src/images/playerAttacked.png").getImage(),10);
    private Image lifeImage = new ImageIcon("src/images/lifeImage.png").getImage();

    //---------------적 공격 선언 ----------------------------------------------------------
    ArrayList<PlayerAttack> playerAttackList = new ArrayList<>(); // 미사일이 하나만 발사되는게 아니라 여러개 발사됨.
    private PlayerAttack playerAttack; // 미사일

    ArrayList<FirstEnemyAttack> firstEnemyAttackList = new ArrayList<>();
    private FirstEnemyAttack firstEnemyAttack;

    ArrayList<SecondEnemyAttack> secondEnemyAttackList = new ArrayList<>(); // 총알이 여러개 발사됨.
    private SecondEnemyAttack secondEnemyAttack;    // secondEnemy 총알

    ArrayList<ThirdEnemyAttack> thirdEnemyAttackList = new ArrayList<>();   // 총알이 여러개 발사됨.
    private ThirdEnemyAttack thirdEnemyAttack;

    ArrayList<SpeedEnemyAttack> speedEnemyAttackList = new ArrayList<>();   // 총알이 여러개 발사됨.
    private SpeedEnemyAttack speedEnemyAttack;  // speedEnemy 총알

    ArrayList<BossAttack> bossAttackList = new ArrayList<>();
    private BossAttack bossAttack;
//--------------- 적 기체 선언 ---------------------------------------------------------
    ArrayList<FirstEnemy> firstEnemyList = new ArrayList<>(); // firstEnemy가 여러번 등장함.
    private FirstEnemy firstEnemy; // 처음 등장하는 적기체

    ArrayList<SecondEnemy> secondEnemyList = new ArrayList<>(); // secondEnemy가 여러번 등장함.
    private SecondEnemy secondEnemy; // 두번째 등장하는 적기체

    ArrayList<ThirdEnemy> thirdEnemyList = new ArrayList<>(); // thirdEnemy가 여러번 등장함.
    private ThirdEnemy thirdEnemy;    // 세번째 등장하는 적기체

    ArrayList<SpeedEnemy> speedEnemyList = new ArrayList<>();   // speedEnemy가 여러번 등장함.
    private SpeedEnemy speedEnemy;  // 빠르게 등장해서 요격하고 사라지는 적기체

    private Boss boss; // 보스기체
    //-------------------폭발 선언--------------------------------------------------------------
    ArrayList<Boom> boomList = new ArrayList<>(); // 폭발을 여러번함.
    private Boom boom; // 적 격추했을 때 발생하는 폭발 이미지

    ArrayList<BossBoom> bossBoomList = new ArrayList<>();   // 폭발을 여러번함.
    private BossBoom bossBoom;  // 보스 격추했을 때 발생하는 폭발 이미지
//--------------------헤비 머신건(아이템) 선언------------------------------------------------
    private Item item;
//---------------------------------------------------------------------------------------
    @Override
    public void run() { // run메소드는 이 쓰레드를 시작할 시 실행될 내용
        cnt = 0;
        startTime =  System.currentTimeMillis();
        while(!isGameClear) {   // 쓰레드가 시작하면서 while안의 코드가 무한 반복.
            if(true) {
                try {
                    Thread.sleep(delay); // 30/1000초 동안 스레드는 일시정지 상태
                    keyProcess();
                    playerAttackProcess();
                    firstEnemyAppearProcess();
                    firstEnemyMoveProcess();
                    firstEnemyAttackProcess();
                    secondEnemyAppearProcess();
                    itemAppearProcess();
                    itemMoveProcess();
                    secondEnemyMoveProcess();
                    secondEnemyAttackProcess();
                    thirdEnemyAppearProcess();
                    thirdEnemyMoveProcess();
                    thirdEnemyAttackProcess();
                    speedEnemyAppearProcess();
                    speedEnemyMoveProcess();
                    speedEnemyAttackProcess();
                    bossAppearProcess();
                    bossMoveProcess();
                    bossAttackProcess();

                    endTime=System.currentTimeMillis();
                    cnt++;  // 0.03초마다 증가
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void keyProcess() {
        if (!isPlayerHidden) {  // player 이미지가 사라진 상태에서 posX, posY를 못움직이게 하기위함.
            if (up && player.posY - player.playerSpeed > 0)
                player.posY -= player.playerSpeed;
            if (down && player.posY + player.height + player.playerSpeed < Main.SCREEN_HEIGHT)
                player.posY += player.playerSpeed;
            if (left && player.posX - player.playerSpeed > 0)
                player.posX -= player.playerSpeed;
            if (right && player.posX + player.width + player.playerSpeed < Main.SCREEN_WIDTH)
                player.posX += player.playerSpeed;
            if (shooting && cnt % 8 == 0) { // 0, 0.24, 0.48, 0.72, 0.96 ,... 이 때 눌러야 실행됨.
                if (!itemCollisionWithPlayer) {  // 기본공격
                    playerAttack = new PlayerAttack(new ImageIcon("src/images/player_attack.png").getImage(), player.posX + player.width / 2, player.posY - 10); // 미사일이 player 좌표의 50만큼 위에서부터 생성됨.
                    playerAttackList.add(playerAttack);
                } else if (itemCollisionWithPlayer) { // 헤비머신건 공격
                    playerAttack = new PlayerAttack(new ImageIcon("src/images/bullet2.png").getImage(), player.posX + player.width / 2, player.posY - 10); // 미사일이 player 좌표의 50만큼 위에서부터 생성됨.
                    playerAttackList.add(playerAttack);
                }
            }
        }
    }
    //------------- 적 기체 생성 -----------------------------------------------
    private void firstEnemyAppearProcess() {
        if(firstEnemyDrawCount < 10) {  // 적기 10번 등장
            if(endTime - startTime >= 10000 * firstEnemyDrawCount) {    // 0초에 첫번째 기체 등장 10초후 두번째 기체 등장
                firstEnemy = new FirstEnemy(50, 0, 250, new ImageIcon("src/images/firstEnemy.png").getImage());
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
                secondEnemy = new SecondEnemy(50, Main.SCREEN_WIDTH, 50, new ImageIcon("src/images/secondEnemy.png").getImage());
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
                        thirdEnemy = new ThirdEnemy(50, 100 + i * 100, -i * 50,new ImageIcon("src/images/thirdEnemy.png").getImage());
                    } else {
                        thirdEnemy = new ThirdEnemy(50, Main.SCREEN_WIDTH-i*100-200,-i*50, new ImageIcon("src/images/thirdEnemy.png").getImage());
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
            if (endTime - startTime > 4000 * (speedEnemyDrawCount + 1) + 1000) {    // 11초, 21초, ..., 적기체 생성
                for (int i = 0; i < 2; i++) {
                    if (i % 2 == 0) {
                        speedEnemy = new SpeedEnemy(50, 150, Main.SCREEN_HEIGHT, new ImageIcon("src/images/speedEnemy.png").getImage());
                    } else {
                        speedEnemy = new SpeedEnemy(50, Main.SCREEN_WIDTH - 250, Main.SCREEN_HEIGHT, new ImageIcon("src/images/speedEnemy.png").getImage());
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
    public void bossAppearProcess() {
        if(bossDrawCount < 1) {
            if(endTime - startTime >= 4000 ) {   // 1초에 첫번째 기체 등장 11초 후 두번째 기체 등장
                boss = new Boss(20000,Main.SCREEN_WIDTH/2-319,-250, new ImageIcon("src/images/boss.png").getImage(), new ImageIcon("src/images/bossAttacked.png").getImage());
                bossDrawCount++;
            }
        }
    }
    public void bossMoveProcess() {
        if (boss != null) {
            if (boss.posY < -1) {
                boss.move();
            }
        }
    }
    //---------------- 적기체 공격 메서드 --------------------------------------------
    private void playerAttackProcess() {    // 공격을 처리해주는 메서드
        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            playerAttack.fire();

            if (playerAttackList.get(i).y < 0 - playerAttackList.get(i).height) {  // player 총알 화면에서 나갈시 제거하여 메모리 재활용
                playerAttackList.remove(i);
            }
        }
        for(int i=0; i<firstEnemyList.size(); i++) {    // player 총알과 firstEnemy 충돌시 총알과 firstEnemy 제거
            firstEnemy = firstEnemyList.get(i);
            for(int j=0; j<playerAttackList.size(); j++) {
                playerAttack = playerAttackList.get(j);
            if(Crash(playerAttack.x,playerAttack.y, firstEnemy.posX, firstEnemy.posY,playerAttack.width,playerAttack.height, firstEnemy.width,firstEnemy.height)) {
                firstEnemy.hp = firstEnemy.hp - 50; // 적 격추시 hp 차감
                playerAttackList.remove(j); // 총알 제거
                if(firstEnemy.hp <= 0) {    // 적 hp 0이하로 떨어지면 if문 true
                    firstEnemyList.remove(i);   // firstEnemy 제거
                    boomList.add(new Boom(firstEnemy.posX, firstEnemy.posY, 80, 80));
                }
                }
            }
        }
        for(int i=0; i<secondEnemyList.size(); i++) {    // player 총알과 secondEnemy 충돌시 총알과 secondEnemy 제거
            secondEnemy = secondEnemyList.get(i);
            for(int j=0; j<playerAttackList.size(); j++) {
                playerAttack = playerAttackList.get(j);
                if(Crash(playerAttack.x,playerAttack.y,secondEnemy.posX, secondEnemy.posY, playerAttack.width, playerAttack.height, secondEnemy.width, secondEnemy.height)){
                    secondEnemy.hp = secondEnemy.hp - 50;   // 적 격추시 hp 차감
                    playerAttackList.remove(j); // 총알 제거
                    if(secondEnemy.hp <= 0) {   // 적 hp 0이하로 떨어지면 if문 true
                        secondEnemyList.remove(i);  // secondEnemy 제거
                            boomList.add(new Boom(secondEnemy.posX, secondEnemy.posY, 80, 80));
                    }
                }
            }
        }
        for(int i=0; i<thirdEnemyList.size(); i++) {
            thirdEnemy = thirdEnemyList.get(i);
            for(int j=0; j<playerAttackList.size(); j++) {
                playerAttack = playerAttackList.get(j);
                if(Crash(playerAttack.x, playerAttack.y, thirdEnemy.posX, thirdEnemy.posY, playerAttack.width,playerAttack.height, thirdEnemy.width, thirdEnemy.height)){
                    thirdEnemy.hp = thirdEnemy.hp - 50; // 적 격추시 hp 차감
                    playerAttackList.remove(j); // 총알 제거
                    if(thirdEnemy.hp <= 0) {    // 적 hp 0이하로 떨어지면 if문 true
                        thirdEnemyList.remove(i);   // thirdEnemy 제거
                        boomList.add(new Boom(thirdEnemy.posX, thirdEnemy.posY, 80, 80));
                    }
                }
            }
        }

        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            if (boss != null && Crash(playerAttack.x, playerAttack.y, boss.posX, boss.posY, playerAttack.width, playerAttack.height, boss.width, boss.height)) {
                boss.hp = boss.hp - 50;  // boss 피격시 hp 50 차감
                isBossAttacked = true;  // boss 이미지 attack 당했을때로 바꿔주기위함
                Timer loadingTimer2 = new Timer();
                TimerTask loadingTask2 = new TimerTask() {
                    @Override
                    public void run() {
                        isBossAttacked = false;
                    }
                };
                loadingTimer2.schedule(loadingTask2,100);
                playerAttackList.remove(i); // 총알 제거
                if (boss.hp <= 0) {
                    bossBoomList.add(new BossBoom(boss.posX+boss.width/2-200,0,450,250));
                    boss = null;
                    Timer loadingTimer = new Timer();
                    TimerTask loadingTask = new TimerTask(){
                        @Override
                        public void run() {
                            airportGame.isGameScreen = false;
                            airportGame.isEndgameScreen = true;
                            isGameClear = true;
                        }
                    };
                    loadingTimer.schedule(loadingTask, 1500);
                }
            }
        }
    }

    // -------------- 총알 생성 메서드 -----------------------------------------------
    private void firstEnemyAttackProcess() {   // firstEnemy 총알 생성 메서드
        for(int i=0; i<firstEnemyList.size(); i++) {
            firstEnemy = firstEnemyList.get(i);
            if (cnt % 20 == 0) { // 0, 0.6, 1.2, 1.8, ...초마다 총알 생성
                firstEnemyAttack = new FirstEnemyAttack(firstEnemy.posX, firstEnemy.posY, new ImageIcon("src/images/firstEnemy_bullet.png").getImage());
                firstEnemyAttackList.add(firstEnemyAttack);
            }
            for (int j = 0; j < firstEnemyAttackList.size(); j++) {
                firstEnemyAttack = firstEnemyAttackList.get(j);
                if(!isPlayerHidden) {
                    if (Crash(firstEnemyAttack.posX, firstEnemyAttack.posY, player.posX, player.posY, firstEnemyAttack.width, firstEnemyAttack.height, player.width, player.height)) {
                        isPlayerAttacked = true;
                        firstEnemyAttackList.remove(j);   //총알 제거
                        player.hp -= 50;
                        System.out.println("firstEnemy 피격");

                        Timer loadingTimer3 = new Timer();  // player 공격당하면 반짝이는 효과
                        TimerTask loadingTask3 = new TimerTask() {
                            @Override
                            public void run() {
                                isPlayerAttacked = false;
                            }
                        };
                        loadingTimer3.schedule(loadingTask3, 100);

                        if (player.hp <= 0) {
                            boomList.add(new Boom(player.posX, player.posY, 120, 120));
                            isPlayerHidden = true; // player 숨기기
                            if (player.life <= 0) {
                                Timer loadingTimer2 = new Timer();
                                TimerTask loadingTask2 = new TimerTask() {
                                    @Override
                                    public void run() { // player 사망시 GameOver화면으로 전환
                                        airportGame.isGameScreen = false;
                                        airportGame.isGameOverScreen = true;
                                    }
                                };
                                loadingTimer2.schedule(loadingTask2, 1500);
                            }
                            Timer loadingTimer = new Timer();
                            TimerTask loadingTask = new TimerTask() {
                                @Override
                                public void run() {
                                    isPlayerHidden = false; // player 다시 보이게 하기
                                    player.posX = (Main.SCREEN_WIDTH + 25) / 2;
                                    player.posY = (Main.SCREEN_HEIGHT - 50);
                                }
                            };
                            loadingTimer.schedule(loadingTask, 5500);
                            player.life--;
                            lifeCount--;

                            player.hp = 150;
                            System.out.println("남은목숨: " + player.life);
                        }
                    }
                }
            }
        }
        for(int i=0; i<firstEnemyAttackList.size(); i++) {
            firstEnemyAttack = firstEnemyAttackList.get(i);
            firstEnemyAttack.fire();
            if(firstEnemyAttack.posY > Main.SCREEN_HEIGHT) {    // firstEnemy의 총알이 화면 맨밑으로 빠져나가면 메모리에서 제거됨
                firstEnemyAttackList.remove(i);
            }
        }
    }
    private void secondEnemyAttackProcess() {
        for (int i = 0; i < secondEnemyList.size(); i++) {    // secondEnemy 총알 생성
            secondEnemy = secondEnemyList.get(i);
            if (cnt % 20 == 0) {
                secondEnemyAttack = new SecondEnemyAttack(secondEnemy.posX, secondEnemy.posY, new ImageIcon("src/images/secondEnemy_bullet.png").getImage());
                secondEnemyAttackList.add(secondEnemyAttack);
            }
            for (int j = 0; j < secondEnemyAttackList.size(); j++) {
                secondEnemyAttack = secondEnemyAttackList.get(j);
                if(!isPlayerHidden) {
                    if (Crash(secondEnemyAttack.posX, secondEnemyAttack.posY, player.posX, player.posY, secondEnemyAttack.width, secondEnemyAttack.height, player.width, player.height)) {
                        isPlayerAttacked = true;
                        secondEnemyAttackList.remove(j);    // 총알제거
                        player.hp -= 50;
                        System.out.println("secondEnemy 피격");

                        Timer loadingTimer3 = new Timer();  // player 공격당하면 반짝이는 효과
                        TimerTask loadingTask3 = new TimerTask() {
                            @Override
                            public void run() {
                                isPlayerAttacked = false;
                            }
                        };
                        loadingTimer3.schedule(loadingTask3, 100);

                        if (player.hp <= 0) {
                            boomList.add(new Boom(player.posX, player.posY, 120, 120));
                            isPlayerHidden = true;  // player 숨기기
                            if (player.life <= 0) {
                                Timer loadingTimer2 = new Timer();
                                TimerTask loadingTask2 = new TimerTask() {
                                    @Override
                                    public void run() { // player 사망시 GameOver화면으로 전환
                                        airportGame.isGameScreen = false;
                                        airportGame.isGameOverScreen = true;
                                    }
                                };
                                loadingTimer2.schedule(loadingTask2, 1500);
                            }
                            Timer loadingTimer = new Timer();
                            TimerTask loadingTask = new TimerTask() {
                                @Override
                                public void run() {
                                    isPlayerHidden = false; // player 다시 보이게 하기
                                    player.posX = (Main.SCREEN_WIDTH + 25) / 2;
                                    player.posY = (Main.SCREEN_HEIGHT - 50);
                                }
                            };
                            loadingTimer.schedule(loadingTask, 5500);
                            player.life--;
                            lifeCount--;

                            player.hp = 150;
                            System.out.println("남은목숨: " + player.life);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < secondEnemyAttackList.size(); i++) {
            secondEnemyAttack = secondEnemyAttackList.get(i);
            secondEnemyAttack.fire();
            if (secondEnemyAttack.posY > Main.SCREEN_HEIGHT) {    // secondEnemy의 총알이 화면 맨밑으로 빠져나가면 메모리에서 제거됨
                secondEnemyAttackList.remove(i);
            }
        }
    }
    private void thirdEnemyAttackProcess() {
        for(int i=0; i<thirdEnemyList.size(); i++) {
            thirdEnemy = thirdEnemyList.get(i);
            if(cnt % 50 == 0) { // 1.5, 3.0, 4.5, ....
                thirdEnemyAttack = new ThirdEnemyAttack(thirdEnemy.posX+thirdEnemy.width/2,thirdEnemy.posY+thirdEnemy.height,new ImageIcon("src/images/thirdEnemy_bullet.png").getImage());
                thirdEnemyAttackList.add(thirdEnemyAttack);
            }
            for(int j =0; j<thirdEnemyAttackList.size(); j++) {
                thirdEnemyAttack = thirdEnemyAttackList.get(j);
                if(!isPlayerHidden) {
                    if (Crash(thirdEnemyAttack.posX, thirdEnemyAttack.posY, player.posX, player.posY, thirdEnemyAttack.width, thirdEnemyAttack.height, player.width, player.height)) {
                        isPlayerAttacked = true;
                        thirdEnemyAttackList.remove(j);
                        player.hp -= 50;
                        System.out.println("thirdEnemy 피격");

                        Timer loadingTimer3 = new Timer();  // player 공격당하면 반짝이는 효과
                        TimerTask loadingTask3 = new TimerTask() {
                            @Override
                            public void run() {
                                isPlayerAttacked = false;
                            }
                        };
                        loadingTimer3.schedule(loadingTask3, 100);

                        if (player.hp <= 0) {
                            boomList.add(new Boom(player.posX, player.posY, 120, 120));
                            isPlayerHidden = true;  // player 숨기기
                            if (player.life <= 0) {
                                Timer loadingTimer2 = new Timer();
                                TimerTask loadingTask2 = new TimerTask() {
                                    @Override
                                    public void run() { // player 사망시 GameOver화면으로 전환
                                        airportGame.isGameScreen = false;
                                        airportGame.isGameOverScreen = true;
                                    }
                                };
                                loadingTimer2.schedule(loadingTask2, 1500);
                            }
                            Timer loadingTimer = new Timer();
                            TimerTask loadingTask = new TimerTask() {
                                @Override
                                public void run() {
                                    isPlayerHidden = false; // player 다시 보이게 하기
                                    player.posX = (Main.SCREEN_WIDTH + 25) / 2;
                                    player.posY = (Main.SCREEN_HEIGHT - 50);
                                }
                            };
                            loadingTimer.schedule(loadingTask, 5500);
                            player.life--;
                            lifeCount--;

                            player.hp = 150;
                            System.out.println("남은목숨: " + player.life);
                        }
                    }
                }
            }
        }
        for(int i=0; i<thirdEnemyAttackList.size(); i++) {
            thirdEnemyAttack = thirdEnemyAttackList.get(i);
            thirdEnemyAttack.fire();
            if(thirdEnemyAttack.posY > Main.SCREEN_HEIGHT) {  // thirdEnemy의 총알이 화면 맨밑으로 빠져나가면 메모리에서 제거됨
                thirdEnemyAttackList.remove(i);
            }
        }
    }
    private void speedEnemyAttackProcess() {
        if (speedEnemy!=null && speedEnemy.posY + speedEnemy.height / 2 == 404) {
            for (int i = 0; i < speedEnemyList.size(); i++) {
                SpeedEnemy speedEnemy = speedEnemyList.get(i);
                for (int j = 0; j < 16; j++) {
                    speedEnemyAttack = new SpeedEnemyAttack(speedEnemy.posX + speedEnemy.width / 2, speedEnemy.posY + speedEnemy.height / 2, new ImageIcon("src/images/speedEnemy_bullet.png").getImage());
                    speedEnemyAttackList.add(speedEnemyAttack);
                }
            }
        }
        for (int k = 0; k < speedEnemyAttackList.size(); k++) {
            speedEnemyAttack = speedEnemyAttackList.get(k);
            int methodIndex = k % 16;   // 0부터 15까지의 값을 반복
            String methodName = "fire" + methodIndex;   // SpeedEnemyAttack 클래스에서 fire1~fire16 메서드 이름과 같게 만들어주기
            try {
                Method fireMethod = SpeedEnemyAttack.class.getMethod(methodName);
                fireMethod.invoke(speedEnemyAttack);  // speedEnemy 객체에서 해당 메서드를 호출
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<speedEnemyAttackList.size(); i++) {  // speedEnemy가 player 공격시 충돌감지 및 생명갯수
            speedEnemyAttack = speedEnemyAttackList.get(i);
            if(!isPlayerHidden) {
                if (Crash(speedEnemyAttack.posX, speedEnemyAttack.posY, player.posX, player.posY, speedEnemyAttack.width, speedEnemyAttack.height, player.width, player.height)) {
                    isPlayerAttacked = true;
                    speedEnemyAttackList.remove(i);
                    player.hp -= 50;
                    System.out.println("speedEnemy 피격");

                    Timer loadingTimer3 = new Timer();  // player 공격당하면 반짝이는 효과
                    TimerTask loadingTask3 = new TimerTask() {
                        @Override
                        public void run() {
                            isPlayerAttacked = false;
                        }
                    };
                    loadingTimer3.schedule(loadingTask3, 100);

                    if (player.hp <= 0) {
                        boomList.add(new Boom(player.posX, player.posY, 120, 120));
                        isPlayerHidden = true;  // player 숨기기
                        if (player.life <= 0) {
                            Timer loadingTimer2 = new Timer();
                            TimerTask loadingTask2 = new TimerTask() {
                                @Override
                                public void run() { // player 사망시 GameOver화면으로 전환
                                    airportGame.isGameScreen = false;
                                    airportGame.isGameOverScreen = true;
                                }
                            };
                            loadingTimer2.schedule(loadingTask2, 1500);
                        }
                        Timer loadingTimer = new Timer();
                        TimerTask loadingTask = new TimerTask() {
                            @Override
                            public void run() {
                                isPlayerHidden = false; // player 다시 보이게 하기
                                player.posX = (Main.SCREEN_WIDTH + 25) / 2;
                                player.posY = (Main.SCREEN_HEIGHT - 50);
                            }
                        };
                        loadingTimer.schedule(loadingTask, 5500);
                        player.life--;
                        lifeCount--;

                        player.hp = 150;
                        System.out.println("남은목숨: " + player.life);
                    }
                }
            }
        }
    }
    private void bossAttackProcess() {
        if (boss != null && cnt % 180 == 0) {   // 5.4초마다 공격
            for (int i = 0; i < 9; i++) {
                bossAttack = new BossAttack(10, (Main.SCREEN_WIDTH - 10) / 2, boss.height - 20, new ImageIcon("src/images/boss_bullet.png").getImage());
                bossAttackList.add(bossAttack);
            }
        }
        for (int i = 0; i < bossAttackList.size(); i++) {
            bossAttack = bossAttackList.get(i);
            int methodIndex = i % 9;
            String methodName = "fire" + methodIndex;
            try {
                Method fireMethod = BossAttack.class.getMethod(methodName);
                fireMethod.invoke(bossAttack);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < bossAttackList.size(); i++) {
            bossAttack = bossAttackList.get(i);
            if (!isPlayerHidden) {
                if (Crash(bossAttack.posX, bossAttack.posY, player.posX, player.posY, bossAttack.width, bossAttack.height, player.width, player.height)) {
                    isPlayerAttacked = true;
                    bossAttackList.remove(i);
                    player.hp -= 50;
                    System.out.println("boss 피격");

                    Timer loadingTimer3 = new Timer();  // player 공격당하면 반짝이는 효과
                    TimerTask loadingTask3 = new TimerTask() {
                        @Override
                        public void run() {
                            isPlayerAttacked = false;
                        }
                    };
                    loadingTimer3.schedule(loadingTask3, 100);

                    if (player.hp <= 0) {
                        boomList.add(new Boom(player.posX, player.posY, 120, 120));
                        isPlayerHidden = true;  // player 숨기기
                        if (player.life <= 0) {
                            Timer loadingTimer2 = new Timer();
                            TimerTask loadingTask2 = new TimerTask() {
                                @Override
                                public void run() { // player 사망시 GameOver화면으로 전환
                                    airportGame.isGameScreen = false;
                                    airportGame.isGameOverScreen = true;
                                }
                            };
                            loadingTimer2.schedule(loadingTask2, 1500);
                        }
                        Timer loadingTimer = new Timer();
                        TimerTask loadingTask = new TimerTask() {
                            @Override
                            public void run() {
                                isPlayerHidden = false; // player 다시 보이게 하기
                                player.posX = (Main.SCREEN_WIDTH + 25) / 2;
                                player.posY = (Main.SCREEN_HEIGHT - 50);
                            }
                        };
                        loadingTimer.schedule(loadingTask, 5500);
                        player.life--;
                        lifeCount--;

                        player.hp = 150;
                        System.out.println("남은목숨: " + player.life);
                    }
                }
            }
        }
    }
    //----------------------헤비 머신건(아이템) 생성-------------------------------------
    private void itemAppearProcess(){
        if(item == null && !itemCollisionWithPlayer){
            int randomPosX = (int) (Math.random() * (Main.SCREEN_WIDTH - 60+1));
            item = new Item(randomPosX,0);
        }
    }
    //----------------------헤비 머신건(아이템) 움직임-----------------------------------
    private void itemMoveProcess(){
        if(item != null) {
            if (item.posX + item.width >= Main.SCREEN_WIDTH) { // 오른쪽 벽 부딪침
                movingRight = true;
                movingLeft = false;
                movingUnder = false;
                movingDrop = false;
            }
            if (movingRight) {
                item.rightMove();
            }
            if (item.posY + item.height > Main.SCREEN_HEIGHT) {   // 아래쪽 벽 부딪침
                movingRight = false;
                movingLeft = false;
                movingUnder = true;
                movingDrop = false;
            }
            if (movingUnder) {
                item.underMove();
            }
            if (item.posX < 0) {    // 왼쪽 벽 튕길 때
                movingRight = false;
                movingLeft = true;
                movingUnder = false;
                movingDrop = false;
            }
            if (movingLeft) {
                item.leftMove();
            }
            if (item.posY <= 0) {   // 위쪽 벽 튕길 때 및 아이템 생길 때
                movingRight = false;
                movingLeft = false;
                movingUnder = false;
                movingDrop = true;
            }
            if (movingDrop) {
                item.dropMove();
            }
            if (Crash(item.posX + item.width / 2, item.posY + item.height / 2, player.posX + player.width / 2, player.posY + player.height / 2, item.width, item.height, player.width, player.height)) {
                item = null;
                itemCollisionWithPlayer = true;
            }
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
        bossDraw(g);    // boss를 맨 먼저 그려야 다른 객체들이 겹쳐졌을때 (다른 객체가)무시되지않음.
        playerDraw(g);
        firstEnemyDraw(g);
        secondEnemyDraw(g);
        thirdEnemyDraw(g);
        speedEnemyDraw(g);
        firstEnemyAttackDraw(g);
        secondEnemyAttackDraw(g);
        thirdEnemyAttackDraw(g);
        speedEnemyAttackDraw(g);
        bossAttackDraw(g);
        boomDraw(g);
        bossBoomDraw(g);
        itemDraw(g);
        lifeImageDraw(g);
    }

    public void playerDraw(Graphics g) {
        if(!isPlayerAttacked) {
            if (!isPlayerHidden) {
                g.drawImage(player.image, player.posX, player.posY, player.width, player.height, null);
            }
        } else if(isPlayerAttacked) {
            g.drawImage(player.imageAttacked, player.posX, player.posY, player.width, player.height, null);
        }
        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, playerAttack.width, playerAttack.height, null);
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
    public void bossDraw(Graphics g) {
        if (boss != null) {
            if (boss.image != null && isBossAttacked == false) {
                g.drawImage(boss.image, boss.posX, boss.posY, boss.width, boss.height, null);
            } else if(boss.image != null && isBossAttacked == true){
                g.drawImage(boss.imageAttacked, boss.posX, boss.posY, boss.width, boss.height, null);
            }
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
    public void thirdEnemyAttackDraw(Graphics g) {
        for(int i=0; i<thirdEnemyAttackList.size(); i++) {
            thirdEnemyAttack = thirdEnemyAttackList.get(i);
            g.drawImage(thirdEnemyAttack.image, thirdEnemyAttack.posX, thirdEnemyAttack.posY, thirdEnemyAttack.width, thirdEnemyAttack.height, null);
        }
    }
    public void speedEnemyAttackDraw(Graphics g) {
        for(int i=0;i<speedEnemyAttackList.size(); i++) {
            speedEnemyAttack = speedEnemyAttackList.get(i);
            g.drawImage(speedEnemyAttack.image, speedEnemyAttack.posX,speedEnemyAttack.posY, speedEnemyAttack.width, speedEnemyAttack.height, null);
        }
    }
    public void bossAttackDraw(Graphics g) {
        for(int i=0; i<bossAttackList.size(); i++) {
            bossAttack = bossAttackList.get(i);
            g.drawImage(bossAttack.image, bossAttack.posX, bossAttack.posY,bossAttack.width, bossAttack.height, null);
        }
    }
    public void boomDraw(Graphics g) {  // 적기체 연쇄폭발 이미지
        for (int i = 0; i < boomList.size(); i++) {
            boom = boomList.get(i);
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
    public void bossBoomDraw(Graphics g) {  // 적기체 연쇄폭발 이미지
        for (int i = 0; i < bossBoomList.size(); i++) {
            bossBoom = bossBoomList.get(i);
            if (bossBoom.cnt <= 8) {
                g.drawImage(bossBoom.image1, bossBoom.posX, bossBoom.posY, null);
            } else if (bossBoom.cnt <= 32) {
                g.drawImage(bossBoom.image2, bossBoom.posX, bossBoom.posY, null);
            } else if (bossBoom.cnt <= 64) {
                g.drawImage(bossBoom.image3, bossBoom.posX, bossBoom.posY, null);
            } else if (bossBoom.cnt <= 96) {
                g.drawImage(bossBoom.image4, bossBoom.posX, bossBoom.posY, null);
            } else if (bossBoom.cnt <= 128) {
                g.drawImage(bossBoom.image5, bossBoom.posX, bossBoom.posY, null);
            } else if (bossBoom.cnt <= 160) {
                g.drawImage(bossBoom.image6, bossBoom.posX, bossBoom.posY, null);
            }
            bossBoom.show();
            if(bossBoom.cnt==160){
                bossBoomList.remove(i);
            }
        }
    }
    public void itemDraw(Graphics g) {
        if (item != null && item.image != null) {
            g.drawImage(item.image, item.posX, item.posY, null);
        }
    }
    public void lifeImageDraw(Graphics g) {
        if(lifeCount == 2) {
            g.drawImage(lifeImage, 0, 0, null);
            g.drawImage(lifeImage, 70, 0, null);
        } else if(lifeCount == 1) {
            g.drawImage(lifeImage, 0, 0, null);
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