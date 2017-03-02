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
public class app8_game_alien_game extends Applet implements Runnable {

    Thread animation;
    Graphics offscreen;
    Image image;
    static final int REFRESH_RATE = 80;
    Image ufoImages[] = new Image[6];
    Image gunImage;
    gunmanager gm;
    ufomanager um;
    int width, height;

    /**
     * *******************ABSTRACT
     * CLASSES**************************************************************
     */
    abstract class sprite {                                           //sprite begins

        protected boolean visible, active;

        abstract public void paint(Graphics g);

        abstract public void update();

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean a) {
            visible = a;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean a) {
            active = a;
        }

        void suspend() {
            setVisible(false);
            setActive(false);
        }

        void restore() {
            setVisible(true);
            setActive(true);
        }
    }

    abstract class sprite2d extends sprite {

        protected int lx, ly;
        protected Color r;
        protected boolean fill;

        public boolean isFill() {
            return fill;
        }

        public void setFill(boolean a) {
            fill = a;
        }

        public Color getColor() {
            return r;
        }

        public void setColor(Color a) {
            r = a;
        }

    }

    /**
     * *********************************END***************************************
     */
    public class bitmap extends sprite {

        protected int lx, ly, w, h;
        Image img;
        Applet app;

        public bitmap(int x, int y, Image i, Applet a) {
            lx = x;
            ly = y;
            img = i;
            app = a;
            w = i.getWidth(a);
            h = i.getHeight(a);
            restore();
        }

        public bitmap(Image i, Applet a) {
            lx = 0;
            ly = 0;
            img = i;
            app = a;
            w = i.getWidth(a);
            h = i.getHeight(a);
            restore();
        }

        public bitmap(int x, int y, int ww, int hh, Image i, Applet a) {
            lx = x;
            ly = y;
            img = i;
            app = a;
            w = ww;
            h = hh;
            restore();
        }

        public void setSize(int ww, int hh) {
            w = ww;
            h = hh;
        }

        public void paint(Graphics g) {
            g.drawImage(img, lx, ly, w, h, app);
        }

        public void update() {
        }
    }

    public class rectsprite extends sprite2d {

        protected int w, h;

        public rectsprite(int ww, int hh, Color c) {
            lx = 0;
            ly = 0;
            w = ww;
            h = hh;
            fill = false;
            restore();
        }

        public rectsprite(int x, int y, int ww, int hh, Color c) {
            lx = x;
            ly = y;
            w = ww;
            h = hh;
            r = c;
            fill = false;
            restore();
        }

        public void paint(Graphics g) {
            if (visible) {
                g.setColor(r);
                if (fill) {
                    g.fillRect(lx, ly, w, h);
                } else {
                    g.drawRect(lx, ly, w, h);
                }
            }
        }

        public void update() {
        }
    }

    public class gunsprite extends bitmap implements Moveable, Intersect {

        public gunsprite(Image i, Applet a) {
            super(i, a);
        }

        public gunsprite(int x, int y, Image i, Applet a) {
            super(x, y, i, a);
        }

        public gunsprite(int x, int y, int w, int h, Image i, Applet a) {
            super(x, y, w, h, i, a);
        }

        public void setPosition(int x, int y) {
            lx = x;
            ly = y;
        }

        public void setVelocity(int x, int y) {
        }

        public void updatePosition() {
        }

        public boolean intersect(int x1, int y1, int x2, int y2) {
            return x1 < lx + w && lx < x2 && y1 < ly + h && ly < y2;
        }

        public void hit() {
            System.out.println(">>HIT<<");
        }

    }

    /**
     * *************************INTERFACES*************************************************
     */
    interface Moveable {

        public abstract void setPosition(int x, int y);

        public abstract void setVelocity(int vx, int vy);

        public abstract void updatePosition();
    }

    interface Intersect {

        public boolean intersect(int x1, int y1, int x2, int y2);

        public void hit();
    }

    /**
     * **************************************************************************
     */
    public class missele extends rectsprite {

        protected int vy, starty, stopy;
        Intersect target[];

        public missele(int x, int y, Color c, int vy, int starty, int stopy, Intersect target[]) {
            super(x, y, c);
            this.vy = vy;
            this.starty = starty;
            this.stopy = stopy;
            this.target = target;
        }

        public void init(int x) {
            lx = x;
            ly = starty;
            restore();
        }

        public void update() {
            if (active) {
                ly += vy;
                if (ly >= stopy) {
                    suspend();
                } else {
                    for (int i = 0; i < target.length; i++) {
                        if (target[i].intersect(lx, ly, lx + w, ly + h)) {
                            target[i].hit();
                            suspend();
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * **************************************************************************
     */
    public class gunmanager {

        private gunsprite gun;
        private int gun_w, gun_h;
        private missele missi;
        private int w, h, min_x, max_x;
        private int gun_min_x, gun_max_x;
        private int mis_min_x, mis_max_x;
        private int gun_y;
        static final int MISSILE_WIDTH = 3;
        static final int MISSILE_HEIGHT = 27;
        final Color MISSILE_COLOR = Color.red;
        static final int MISSILE_SPEED = -27; //static final Color MISSILE_COLOR=  Color.red ;

        public gunmanager(int width, int height, Image gunImage, Intersect target[], Applet a) {
            this.w = width;
            this.h = height;
            gun = new gunsprite(gunImage, a);
            gun_w = gunImage.getWidth(a) / 2;
            gun_h = gunImage.getHeight(a);
            gun_y = height - gun_h;
            min_x = gun_w;
            max_x = width - gun_w;
            gun_min_x = 0;
            gun_max_x = width - 2 * gun_w;
            mis_min_x = min_x - 2;
            mis_max_x = max_x - 2;
            gun.setPosition(width / 2 - gun_w, gun_y);
            missi = new missele(MISSILE_WIDTH, MISSILE_HEIGHT, MISSILE_COLOR, MISSILE_SPEED, height - gun_h, 0, target);

        }

        public void movegun(int x) {
            if (x <= min_x) {
                gun.setPosition(gun_min_x, gun_y);
            } else if (x >= max_x) {
                gun.setPosition(gun_max_x, gun_y);
            } else {
                gun.setPosition(x - gun_w, gun_y);
            }
        }

        public void firemissi(int x) {
            if (!missi.isActive()) {
                if (x <= min_x) {
                    missi.init(mis_min_x);
                } else if (x >= max_x) {
                    missi.init(mis_max_x);
                }
            } else {
                missi.init(x - 2); // initialize missile
            }
        }

        public void update() {
            missi.update();
        }

        public void paint(Graphics g) {
            gun.paint(g);
            missi.paint(g);
        }

        public gunsprite getGun() {
            return gun;
        }

        public int getGunY() {
            return gun_y;
        }
    }

    class bitloop extends bitmap implements Moveable {

        protected Image images[];       // sequence of bitmaps  
        protected int currentImage;     // the current bitmap  
        protected boolean foreground;   // are there foreground images?  
        protected boolean background;

        public bitloop(int x, int y, int ww, int hh, Image i, Applet a) {
            super(x, y, ww, hh, i, a);
        }

        public bitloop(int x, int y, Image i, Image f[], Applet a) {
            super(x, y, i, a);
            if (i != null) {
                // if there's a background image      
                background = true;
            } else {
                background = false;
            }

            images = f;
            currentImage = 0;
            if (images == null || images.length == 0) {
                foreground = false;
            } else {
                foreground = true;
                if (!background) {
                    w = images[0].getWidth(a);
                    h = images[0].getHeight(a);
                }
            }
        }

        public void update() {
            if (active && foreground) {
                currentImage = (currentImage + 1) % images.length;
            }
            updatePosition();
        }

        public void paint(Graphics g) {
            if (visible) {
                if (background) {
                    g.drawImage(img, lx, ly, app);
                }
                if (foreground) {
                    g.drawImage(images[currentImage], lx, ly, app);
                }
            }
        }

        @Override
        public void setPosition(int x, int y) {
            lx = x;
            ly = y;
        }

        protected int vx;
        protected int vy;

        @Override
        public void setVelocity(int vx, int vy) {
            this.vx = vx;
            this.vy = vy;
        }

        @Override
        public void updatePosition() {
            lx += vx;
            ly += vy;
        }

    }

    public static class ufo extends bitloop implements Intersect {

        byte state;
        static final byte STANDBY = 0;
        static final byte ATTACK = 1;
        static final byte RETREAT = 2;
        static final byte LAND = 3;
        static final double STANDBY_EXIT = .95;
        static final double ATTACK_EXIT = .95;
        static final double RETREAT_EXIT = .95;
        static final double LAND_EXIT = .95;
        static final double FLIP_X = 0.9;
        static final int RETREAT_Y = 17;
        int max_x, max_y;
        static Intersect target;
        static int gun_y;

        public ufo(Image ufoImages[], int max_x, int max_y, Applet a) {
            super(0, 0, null, ufoImages, a);
            this.max_x = max_x;
            this.max_y = max_y;
            currentImage = getRand(5);

            startStandby();
        }

        static public void initialize(gunmanager gm) {
            target = gm.getGun();
            gun_y = gm.getGunY();
        }

        @Override
        public boolean intersect(int x1, int y1, int x2, int y2) {
            return visible && (x2 >= lx) && (lx + w >= x1) && (y2 >= ly) && (ly + h >= y1);
        }

        @Override
        public void hit() {
            if (state != ATTACK) {
                suspend();
            }
        }

        protected void landingRoutine() {
            System.out.println("ufo landed");
            suspend();
        }

        protected void startStandby() {
            vx = getRand(8) - 4;
            vy = 0;
            state = STANDBY;
        }

        protected void startAttack() {
            vx = getRand(10) - 5;
            vy = getRand(5) + 4;
            state = ATTACK;
        }

        protected void startRetreat() {
            vx = 0;
            vy = -getRand(3) - 2;
            state = RETREAT;
        }

        protected void startLand() {
            vx = 0;
            vy = getRand(3) + 2;
            state = LAND;
        }

        static public int getRand(int x) {
            return (int) (x * Math.random());
        }

        public void update() {
            if ((ly + h >= gun_y) && target.intersect(lx, ly, lx + w, ly + h)) {
                target.hit();
                suspend();
                return;
            }
            double r1 = Math.random();    // pick random nums  
            double r2 = Math.random();
            switch (state) {
                case STANDBY:
                    if (r1 > STANDBY_EXIT) {
                        if (r2 > 0.5) {
                            startAttack();
                        } else {
                            startLand();
                        }

                    } else if ((lx < w) || (lx > max_x - w) || (r2 > FLIP_X)) {
                        vx = -vx;
                    }

                    break;
                case ATTACK:
                    if ((r1 > ATTACK_EXIT) || (ly > gun_y - 17)) {
                        startRetreat();
                    } else if ((lx < w) || (lx > max_x - w) || (r2 > FLIP_X)) {
                        vx = -vx;
                    }
                    break;
                case RETREAT:
                    if (r1 > RETREAT_EXIT) {
                        if (r2 > 0.5) {
                            startAttack();
                        } else {
                            startStandby();
                        }
                    } else if (ly < RETREAT_Y) {
                        startStandby();
                    }
                    break;
                case LAND:
                    if (r1 > LAND_EXIT) {
                        startStandby();
                    } else if (ly >= max_y - h) {
                        landingRoutine();
                    }
                    break;
            }
            super.update();
        }

    }

    public static class ufomanager {

        static int width, height;
        private ufo ufos[];
        static final int NUM_UFOS = 7;

        public ufomanager(int width, int height, Image ufoImages[], Applet a) {
            this.width = width;
            this.height = height;
            ufos = new ufo[NUM_UFOS];
            for (int i = 0; i < ufos.length; i++) {
                ufos[i] = new ufo(ufoImages, width, height, a);
                initializePosition(ufos[i]);
            }
        }

        public void initialize(gunmanager gm) {
            ufo.initialize(gm);
        }

        private void initializePosition(Moveable m) {
            m.setPosition(ufo.getRand(width - 100) + 50,
                    ufo.getRand(height - 150) + 10);
        }

        public ufo[] getUFO() {
            return ufos;
        }

        public void paint(Graphics g) {
            for (int i = 0; i < ufos.length; i++) {
                ufos[i].paint(g);
            }
        }

        public void update() {
            for (int i = 0; i < ufos.length; i++) {
                if (ufos[i].isActive()) {
                    ufos[i].update();
                } else {
                    initializePosition(ufos[i]);
                    ufos[i].restore();
                }

            }

        }

    }

    /**
     * ****************************************************************************
     */
    public void init() {
        showStatus("Loading Images -- WAIT!");
        setBackground(Color.black);
        width = bounds().width;
        height = bounds().height;
        loadImages();
        um = new ufomanager(
                this.width, this.height, 
                ufoImages, this);
        gm = new gunmanager(width, height, gunImage, um.getUFO(), this);
        um.initialize(gm);
        image = createImage(width, height);
        offscreen = image.getGraphics();

    }

    public void loadImages() {
        MediaTracker t = new MediaTracker(this);
        gunImage = getImage(getCodeBase(), "images/gun.png");
        t.addImage(gunImage, 0);
        for (int i = 0; i < 1; i++) {
            ufoImages[i] = getImage(getCodeBase(), "images/ufo" + i + ".jpg");
            t.addImage(ufoImages[i], 0);
        }
        try {
            t.waitForAll();
        } catch (InterruptedException e) {
        }
        if (t.isErrorAny()) {
            showStatus("Error Loading Images!");
        } else if (t.checkAll()) {
            showStatus("Images successfully loaded");
        }

// TODO start asynchronous download of heavy resources
    }

    public boolean mouseMove(Event e, int x, int y) {
        gm.movegun(x);
        return true;
    }

    public boolean mouseDrag(Event e, int x, int y) {
        gm.movegun(x);
        return true;
    }

    public boolean mouseDown(Event e, int x, int y) {
        gm.firemissi(x);
        return true;
    }

    public void start() {
        showStatus("Starting Game!");
        animation = new Thread(this);
        if (animation != null) {
            animation.start();
        }
    }

    public void updateManagers() {
        gm.update();
        um.update();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        offscreen.setColor(Color.black);
        offscreen.fillRect(0, 0, width, height);  // clear buffer
        gm.paint(offscreen);
        um.paint(offscreen);
        g.drawImage(image, 0, 0, this);
    }

    public void run() {
        while (true) {
            repaint();
            updateManagers();
            Thread.currentThread().yield();
            try {
                Thread.sleep(REFRESH_RATE);
            } catch (Exception exc) {
            };
        }
    }

    public void stop() {
        showStatus("Game Stopped");
        if (animation != null) {
            animation.stop();
            animation = null;
        }
    }
    // TODO overwrite start(), stop() and destroy() methods
}
