package airport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static airport.Main.airportGame;

public class AirportGame extends JFrame {

    private Image bufferImage;
    private Graphics screenGraphic;
    private int backgroundY = 0;

    private Image mainScreen = new ImageIcon("src/images/mainScreen.jpg").getImage(); // ImageIcon안에는 이미지 파일의 경로를 넣어준다.
    private Image backgroundScreen1 = new ImageIcon("src/images/background1.jpg").getImage();
    private Image backgroundScreen2 = new ImageIcon("src/images/background2.jpg").getImage();
    private Image backgroundScreen3 = new ImageIcon("src/images/background3.jpg").getImage();
    private Image backgroundScreen4 = new ImageIcon("src/images/background4.jpg").getImage();
    private Image backgroundScreen5 = new ImageIcon("src/images/background5.jpg").getImage();
    private Image backgroundScreen6 = new ImageIcon("src/images/background6.jpg").getImage();
    private ImageIcon startButtonBasic = new ImageIcon("src/images/startButtonBasic.png");
    private ImageIcon startButtonEntered = new ImageIcon("src/images/startButtonEntered.png");
    private ImageIcon exitButtonBasic = new ImageIcon("src/images/exitButtonBasic.png");
    private ImageIcon exitButtonEntered = new ImageIcon("src/images/exitButtonEntered.png");
    private Image endGameScreen = new ImageIcon("src/images/endGameScreen.png").getImage();
    private Image gameOverScreen = new ImageIcon("src/images/gameOverScreen.png").getImage();

    boolean isMainScreen;
    boolean isGameScreen;
    boolean isEndgameScreen;
    boolean isGameOverScreen;
    public static Game game = new Game();

    private JButton startButton = new JButton(startButtonBasic);
    private JButton exitButton = new JButton(exitButtonBasic);

    public AirportGame() {
        setUndecorated(true);
        setTitle("비행기 게임");
        setUndecorated(true);
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setBackground(new Color(0,0,0,0));  // paintcomponent 했을때 배경이 회색이 아니라 흰색으로 됨
        setLayout(null);
        init(); // 초기화

        startButton.setBounds(90,400,150,80);
        exitButton.setBounds(90,500,150,80);

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                startButton.setIcon(startButtonEntered);
            }
            @Override
            public void mouseExited(MouseEvent e){
                startButton.setIcon(startButtonBasic);
            };
        });
        add(startButton);

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                exitButton.setIcon(exitButtonEntered);
            }
            @Override
            public void mouseExited(MouseEvent e){
                exitButton.setIcon(exitButtonBasic);
            };
            public void mouseClicked(MouseEvent e){
                System.exit(0);
            };
        });
        add(exitButton);
    }
    private void init() {
        isMainScreen = true;
        isGameScreen = false;
        isEndgameScreen = false;
        startButton.addActionListener(new ButtonListener());
    }
   public void gameStart(){
        if(isGameScreen) {
            remove(startButton);
            remove(exitButton);
            addKeyListener(new KeyListener());
            requestFocus();
        }
    }
    // JFrame 객체에 그림을 그리는 메소드
    // GUI 프로그램이 실행될 경우 JVM에 의해 자동으로 호출
    // Graphics 객체: 그림을 그리는 객체
    public void paint(Graphics g) {
        if (backgroundY >= 4800) {  // 무한반복 테스트용
            backgroundY = 4000;
        }
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT); // 이미지 타입의 bufferImage변수에 createImage(폭,넓이)메소드로 이미지를 저장.
        screenGraphic = bufferImage.getGraphics(); // bufferImage의 그래픽 반환
        screenDraw(screenGraphic);
        g.drawImage(bufferImage, 0, 0, null); // 메모리상에서 그려진 그림을 화면에 그려줌
    }
    public void screenDraw(Graphics g) { // 메모리상(screenGraphic)에서 그림을 그림
        if (isMainScreen) {
            g.drawImage(mainScreen, 0, 0, null);
        }
        if (isGameScreen) {
            backgroundY++;
            g.drawImage(backgroundScreen1,0, backgroundY,null);
            g.drawImage(backgroundScreen2,0,backgroundY-backgroundScreen1.getHeight(null),null);
            g.drawImage(backgroundScreen3,0,backgroundY-backgroundScreen1.getHeight(null)-backgroundScreen2.getHeight(null),null);
            g.drawImage(backgroundScreen4,0,backgroundY-backgroundScreen1.getHeight(null)-backgroundScreen2.getHeight(null)-backgroundScreen3.getHeight(null),null);
            g.drawImage(backgroundScreen5,0,backgroundY-backgroundScreen1.getHeight(null)-backgroundScreen2.getHeight(null)-backgroundScreen3.getHeight(null)-backgroundScreen4.getHeight(null),null);
            g.drawImage(backgroundScreen6,0,backgroundY-backgroundScreen1.getHeight(null)-backgroundScreen2.getHeight(null)-backgroundScreen3.getHeight(null)-backgroundScreen4.getHeight(null)-backgroundScreen5.getHeight(null),null);
            g.drawImage(backgroundScreen6,0,backgroundY-backgroundScreen1.getHeight(null)-backgroundScreen2.getHeight(null)-backgroundScreen3.getHeight(null)-backgroundScreen4.getHeight(null)-backgroundScreen5.getHeight(null)-backgroundScreen5.getHeight(null),null);// 무한반복 테스트용
            game.gameDraw(g);
        }
        if(isEndgameScreen) {
            g.drawImage(endGameScreen,0,0,null);
        }
        if(isGameOverScreen) {
            g.drawImage(gameOverScreen,0,0,null);
        }
        paintComponents(g);
        this.repaint(); // paint() 다시 그리기
    }

    //-----------------------------------------------------------
    // 키보드를 눌렀을 때 각 기능
    class KeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    game.setDown(true);
                    break;
                case KeyEvent.VK_UP:
                    game.setUp(true);
                    break;
                case KeyEvent.VK_LEFT:
                    game.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.setRight(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    if(isMainScreen) {
                        System.exit(0);
                        break;
                    }
                case KeyEvent.VK_CONTROL:
                    game.setShooting(true);
                    break;
            }
        }
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    game.setDown(false);
                    break;
                case KeyEvent.VK_UP:
                    game.setUp(false);
                    break;
                case KeyEvent.VK_LEFT:
                    game.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.setRight(false);
                    break;
                case KeyEvent.VK_CONTROL:
                    game.setShooting(false);
                    break;
            }
        }
    }
}