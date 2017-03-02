/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.applet.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luffy
 */
public class app3 extends Applet implements Runnable{

    Thread t1,t2;
    Graphics buff;
    Image img;
    int x,y,limitu,limitd;
 byte state;
 boolean a=false;
  int width, height;       
  static final byte UP = 0;  
  static final byte DOWN = 1; 
  static final byte LEFT = 2;
  static final byte RIGHT = 3;
static final int REFRESH_RATE = 15;
    
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
   setBackground(Color.blue);
        x=100;y=25;limitu =0;limitd=500;
        width=50;height=50;state=UP;
  img=createImage(1000,1000);
  buff=img.getGraphics();
        // TODO start asynchronous download of heavy resources
    }
    public void start(){
        t1=new Thread(this);
        if(t1!=null)t1.start();
        
    }
    public void paint(Graphics g){System.out.println(">>paint<<");
    buff.setColor(Color.blue);buff.fillRect(0, 0, 1000,1000);
    buff.setColor(Color.yellow);buff.fillOval(x, y, 15, 15);
      g.drawImage(img, 0, 0, this);
    }
    
    void bounce(){
        System.out.println(">>bounce<<");
         x++;
        switch(state){
                case UP:  if(y<=limitu+5){a=true;state=DOWN;}else {y-=5; }  break;
                case DOWN:  if(y>=limitd){state=UP;}else{y+=5;} break;    
            }
            
           if(a){ limitu +=100;a=false;}
         
    }
    public void update(Graphics g){
        paint(g);
    }
public void run(){
    while(true){
        repaint();
        bounce();
        try {
            Thread.sleep(REFRESH_RATE);
        } catch (InterruptedException ex) {
            Logger.getLogger(app3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    public void stop(){
        if(t1!=null)t1.stop();
    }
    // TODO overwrite start(), stop() and destroy() methods
}
