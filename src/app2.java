/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.applet.*;
import java.awt.*;

/**
 *
 * @author luffy
 */
public class app2 extends Applet implements Runnable {

    Thread animation;
    int locx, locy;          // location of rectangle  int width, height;      // dimensions of rectangle  static final byte UP = 0; // direction of motion  static final byte DOWN = 1;  static final byte LEFT = 2;  static final byte RIGHT = 3;
    byte state;
    Graphics buff;
    Image img;
    int width, height;
    static final byte UP = 0;
    static final byte DOWN = 1;
    static final byte LEFT = 2;
    static final byte RIGHT = 3;
    static final int REFRESH_RATE = 15; // state the rect is in                            // length of pausing interval  static final int REFRESH_RATE = 100;    // in ms
// applet methods:

    public void init() {
        System.out.println(">> init <<");
        setBackground(Color.black);
        locx = 80;
        locy = 100;
        width = 110;
        height = 90;
        state = UP;
        img = createImage(1000, 1000);
        buff = img.getGraphics();
    }

    public void start() {
        System.out.println(">> start <<");
        animation = new Thread(this);
        if (animation != null) {
            animation.start();
        }
    }

    public void paint(Graphics g) {
        System.out.println(">> paint <<");
        buff.setColor(Color.black);
        buff.fillRect(0, 0, 300, 300);
        buff.setColor(Color.yellow);
        buff.fillRect(0, 0, 90, 90);
        buff.fillRect(250, 0, 40, 190);
        buff.fillRect(80, 110, 100, 20); // hidden rectangle
        buff.setColor(Color.blue);
        buff.fillRect(80, 200, 220, 90);
        buff.fillRect(100, 10, 90, 80);
        buff.setColor(Color.lightGray);
        buff.fillRect(locx, locy, width, height);
        buff.setColor(Color.red);
        buff.fillRect(200, 0, 45, 45);
        buff.fillRect(0, 100, 70, 200);
        buff.setColor(Color.magenta);
        buff.fillRect(200, 55, 60, 135);
        buff.setColor(Color.red);
        buff.fillRect(500, 500, 60, 135);
        //g.setColor(Color.yellow);
        //g.fillRect(locx,locy,width,height);
        g.drawImage(img, 0, 0, this);
    }

    public void update(Graphics g) {
        g.clipRect(70, 90, 130, 130);
        paint(g);
    }
    // update the center rectangle  

    void updateRectangle() {
        switch (state) {
            case DOWN:
                locy += 2;
                if (locy >= 110) {
                    state = UP;
                }
                break;
            case UP:
                locy -= 2;
                if (locy <= 90) {
                    state = RIGHT;
                }
                break;
            case RIGHT:
                locx += 2;
                if (locx >= 90) {
                    state = LEFT;
                }
                break;
            case LEFT:
                locx -= 2;
                if (locx <= 70) {
                    state = DOWN;
                }
                break;
        }
    }

    public void run() {
        while (true) {
            repaint();
            updateRectangle();
            try {
                Thread.sleep(REFRESH_RATE);
            } catch (Exception exc) {
            };
        }
    }

    public void stop() {
        System.out.println(">> stop <<");
        if (animation != null) {
            animation.stop();
            animation = null;
        }
    }

    // TODO overwrite start(), stop() and destroy() methods
}
