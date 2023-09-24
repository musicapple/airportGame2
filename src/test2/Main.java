package test2;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;
    JButton btn = new JButton("클릭");
    private Image mainScreen = new ImageIcon("src/images/main_screen.png").getImage();

    Main(){

        setTitle("JButton EX");
        setSize(500,400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        btn.setBounds(100,100,100,100);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        add(btn);
        System.out.println("버튼추가");
    }
    public static void main(String[] args) {
        Main m = new Main();
    }

    public void paint(Graphics g) {
        bufferImage = createImage(airport.Main.SCREEN_WIDTH, airport.Main.SCREEN_HEIGHT); // 이미지 타입의 bufferImage변수에 createImage(폭,넓이)메소드로 이미지를 저장.
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage, 0, 0, null); // 메모리상에서 그려진 그림을 화면에 그려줌
    }
    public void screenDraw(Graphics g){
        g.drawImage(mainScreen, 0, 0, null);
        System.out.println("배경그림");
    }
}
