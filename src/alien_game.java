
import java.applet.*;
import java.awt.*;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author luffy
 */
public class alien_game {
    
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

    public static class missele extends rectsprite {

        protected int vy, starty, stopy;
        Intersect target[];

        public missele(int w, int h, Color c, int vy, int starty, int stopy, Intersect target[]) {
            super(w, h, c);
            this.vy = vy;
            this.starty = starty;
            this.stopy = stopy;
            this.target = target;
        }

        public void initialise(int x) {
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

    public class gunmanager {

        private int h_app, w_app;
        private int min_x, max_x, gun_min_x, gun_max_x, missi_min_x, missi_max_x;
        private int gun_h, gun_w, gun_y;
        private gunsprite gun;
        private missele missi;
        static final int missi_w = 3;
        static final int missi_h = 30;
        static final int missi_v = -30;
        final Color missi_c = Color.red;

        public gunmanager(int w_app, int h_app, Image img, Intersect target[], Applet a) {
            this.w_app = w_app;
            this.h_app = h_app;
            gun = new gunsprite(img, a);
            gun_h = img.getHeight(a);
            gun_w = img.getWidth(a);
            gun_y = h_app - gun_h;
            min_x = gun_w / 2;
            max_x = w_app - min_x;
            gun_min_x = 0;
            gun_max_x = w_app - gun_w;
            missi_min_x = min_x - 2;
            missi_max_x = max_x - 2;
            gun.setPosition(w_app / 2 - gun_w / 2, gun_y);
            missi = new missele(missi_w, missi_h, missi_c, missi_v, gun_y, 0, target);
        }

        public void moveGun(int x) {
            if (x <= min_x) {
                gun.setPosition(gun_min_x, gun_y);
            } else if (x >= max_x) {
                gun.setPosition(gun_max_x, gun_y);
            } else {
                gun.setPosition(x, gun_y);
            }
        }

        public void firemissi(int x) {
            if (!missi.isActive()) {
                if (x <= min_x) {
                    missi.initialise(missi_min_x);
                } else if (x >= max_x) {
                    missi.initialise(missi_max_x);
                } else {
                    missi.initialise(x);
                }

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

        public int getGuny() {
            return gun_y;
        }

    }

    public class bitloop extends bitmap implements Moveable {

        protected int vx, vy;
        protected Image images[];
        protected int current_img;
        protected boolean foreground, background;

        public bitloop(int x, int y, Image i, Image f[], Applet a) {
            super(x, y, i, a);
            images = f;
            if (i != null) {
                background = true;
            } else {
                background = false;
            }
            if (images != null && images.length != 0) {
                foreground = true;

                if (!background) {
                    w = images[0].getWidth(a);
                    h = images[0].getHeight(a);
                }
            } else {
                foreground = false;
            }

        }

        public void update() {
            if (active && foreground) {
                current_img = (current_img + 1) % images.length;
            }

            updatePosition();
        }

        public void paint(Graphics g) {
            if (visible) {
                if (background) {
                    g.drawImage(img, lx, ly, app);
                }
                if (foreground) {
                    g.drawImage(images[current_img], lx, ly, app);
                }
            }
        }

        public void setVelocity(int x, int y) {
            vx = x;
            vy = y;
        }

        public void setPosition(int x, int y) {
            lx = x;
            ly = y;
        }

        public void updatePosition() {
            lx += vx;
            ly += vy;
        }
    }

    public class UFO extends bitloop implements Intersect {

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
        static int guny;
        
        
        
    }

    
    
}
