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
public class app4_colorballs extends Applet implements Runnable{

    Thread t1,t2;
    Graphics buff;
    Image img;
    int x[],y[],sx[],sy[],limitu,limitd;
 byte state;
 Color c[];
 boolean a=false;
  int width, height,i;       
  static final byte UP = 0;  
  static final byte DOWN = 1; 
  static final byte LEFT = 2;
  static final byte RIGHT = 3;
static final int REFRESH_RATE = 10;
static int no=5; 
static int boundlow=0,boundhigh=500;
   
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
   setBackground(Color.blue);
   x=new int[no];
   y=new int[no];
   c=new Color[no];
   sx=new int[no];
   sy=new int[no];
    initialise();
    
      //  x[0]=100;y[0]=25;limitu =0;limitd=500;
        //width=50;height=50;state=UP;
  img=createImage(1000,1000);
  buff=img.getGraphics();
        // TODO start asynchronous download of heavy resources
    }
    public void start(){
       
        t1=new Thread(this);
        if(t1!=null)t1.start();
        
    }
    public void initialise(){
    x[0]=3;y[0]=54;c[0]=Color.yellow;sx[0]=1;sy[0]=0;
    x[1]=300;y[1]=21;c[1]=Color.red;sx[1]=1;sy[0]=0;
    x[2]=100;y[2]=300;c[2]=Color.BLACK;sx[2]=0;sy[2]=1;
    x[3]=500;y[3]=500;c[3]=Color.magenta;sx[3]=0;sy[3]=1;
    x[4]=0;y[4]=250;c[4]=Color.green;sx[4]=1;sy[4]=1;
    
    }
    public void paint(Graphics g){System.out.println(">>paint<<");
    buff.setColor(Color.blue);buff.fillRect(0, 0, 1000,1000);
    for(i=0;i<no;++i){
    buff.setColor(c[i]);buff.fillOval(x[i], y[i], 15, 15);
    }
      g.drawImage(img, 0, 0, this);
    }
    
    void bounce(){
        System.out.println(">>bounce<<");
        for(i=0;i<no;++i){
            switch(sx[i]){
                case 0:if(x[i]<300)x[i]++;else{sx[i]=1;}    break;
                case 1: if(x[i]>boundlow)x[i]--;else{sx[i]=0;}   break;    
            }
            switch(sy[i]){
                case 0:if(y[i]<400)y[i]++;else{sy[i]=1;}break;
                case 1:if(y[i]>boundlow)y[i]--;else{sy[i]=0;}break;    
            }
        }
       /*  x++;
        switch(state){
                case UP:  if(y<=limitu+5){a=true;state=DOWN;}else {y-=5; }  break;
                case DOWN:  if(y>=limitd){state=UP;}else{y+=5;} break;    
            }
            
           if(a){ limitu +=100;a=false;}
         */
        
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
