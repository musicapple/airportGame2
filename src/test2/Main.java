package test2;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int angle = 1 * 360 / 8;    //45
        int angle2 = 2 * 360 / 8;    //90
        int angle3 = 3 * 360 / 8;    //135

        double radians = Math.toRadians(angle3);
        int bulletSpeed = 5;

        int bulletXSpeed =  (int) (bulletSpeed * Math.cos(radians));
        int bulletYSpeed = (int) (bulletSpeed * Math.sin(radians));
        System.out.println(bulletXSpeed);
        System.out.println(bulletYSpeed);
    }
}
