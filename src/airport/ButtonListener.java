package airport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import static airport.AirportGame.game;
import static airport.Main.airportGame;

public class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        airportGame.isMainScreen = false;
        airportGame.isEndgameScreen = false;
        airportGame.isGameScreen = true;
        game.start();   // game클래스의 스레드 시작.
        airportGame.gameStart();
    }
}
