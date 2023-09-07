package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfiniteBackgroundScrolling extends JPanel implements ActionListener {
    private Image backgroundImage;
    private Image backgroundImage2;
    private int backgroundY = 0;
    private Timer timer;

    public InfiniteBackgroundScrolling() {
        // 배경 이미지를 불러옵니다. 배경 이미지 파일의 경로를 지정해야 합니다.
        backgroundImage = new ImageIcon("src/images/game_screen.png").getImage();
        backgroundImage2 = new ImageIcon("src/images/game_screenRed.png").getImage();

        // 타이머를 설정하여 배경 스크롤링을 처리합니다.
        timer = new Timer(10, this);    // 10밀리초마다 작업(actionPerformed 메서드)을 반복적으로 실행하도록 생성됩니다.
        timer.start();  // Timer 객체를 시작하려면 timer.start() 메서드가 필요합니다\
        System.out.println("1. 타이머시작.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 배경 이미지를 아래쪽으로 스크롤합니다.
        backgroundY++;
        System.out.println("2. actionPerformed() 실행");
        if (backgroundY >= 1440) {  // 두번째 사진이 아래로 내려가면서 화면에서 사라지면(조건값:backgroundY == 1440) backgroundY = 720로 바꿔서 두번째 사진이 무한반복 나오도록함.
            backgroundY = 720;
        }
        repaint();  //  JPanel의 모양이 변경되어 다시 그려야 함을 Swing에 알립니다.
        System.out.println("3. repaint() 호출");
    }

    @Override
    protected void paintComponent(Graphics g) { //  코드에서 직접 호출되지 않습니다. repaint() 호출받고 Swing 프레임워크에 의해 호출됩니다.
        // 배경 이미지를 그립니다.
        System.out.println("4. paintComponent 실행");
        g.drawImage(backgroundImage, 0, backgroundY, null);
        // 아래쪽으로 스크롤된 이미지를 그립니다.
        g.drawImage(backgroundImage2, 0, backgroundY - backgroundImage2.getHeight(null), null);  // 첫 번째 이미지 바로 위쪽에 위치하는 배경 이미지의 두 번째 복사본을 그립니다
        g.drawImage(backgroundImage2, 0, backgroundY - backgroundImage2.getHeight(null) - backgroundImage2.getHeight(null), null);  // 두 번째 이미지 바로 위쪽에 위치하는 배경 이미지의 세 번째 복사본을 그립니다

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Infinite Background Scrolling");
        InfiniteBackgroundScrolling backgroundPanel = new InfiniteBackgroundScrolling();
        frame.add(backgroundPanel);
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}