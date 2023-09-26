package test2;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;
    private Image mainScreen= new ImageIcon("src/images/main_screen.png").getImage();
    private JButton button = new JButton("버튼");
    Main(){
     //   setUndecorated(true);   // 기본 메뉴바 삭제
        setTitle("JButton EX");
        setSize(700,800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setBackground(new Color(0,0,0,0));  // paintcomponent 했을때 배경이 회색이 아니라 흰색으로 됨
        setLayout(null);    // 버튼이나 JLabel 을 넣었을 때 그 위치 그대로 넣어짐

        button.setBounds(100,100,100,100);
        add(button);

    }
    public static void main(String[] args) {
        Main m = new Main();
    }

    public void paint(Graphics g) {
        bufferImage = createImage(700, 800); // 이미지 타입의 bufferImage변수에 createImage(폭,넓이)메소드로 이미지를 저장.
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage, 0, 0, null); // 메모리상에서 그려진 그림을 화면에 그려줌
    }
    public void screenDraw(Graphics g){
        g.drawImage(mainScreen, 0, 0, null);
        paintComponents(g);
        this.repaint();
    }
}