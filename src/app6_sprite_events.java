/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.applet.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luffy
 */
public class app6_sprite_events extends Applet implements Runnable {
Thread t;
boolean aa;
int tx1=500,ty1=500;
final int  rr=15;
    final int no=4;
final byte UP=0;
final byte DOWN=1;

Graphics buff;
Image img;
    Font c;
    String a = "haaaaaaaaaaaa!!!";
sprite s[]=new sprite[no];
    abstract class sprite {

        protected boolean active, visible;

        abstract void paint(Graphics g);

        abstract void update();
        abstract void grow();
        abstract void shrink();

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean b) {
            visible = b;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean b) {
            active = b;
        }

        public void suspend() {
            setVisible(false);
            setActive(false);
        }
        
               // restore the sprite  public void restore() {    setVisible(true);    setActive(true);  }

    }
 
    abstract class sp2d extends sprite{
    protected int x,y;
    Color c;
    boolean fill;
    void setFill(boolean a){
        fill=a;
    }
    boolean getFill(){
        return fill;
    }
     void setcolor(Color a){
        c=a;
    }
    Color getcolor(){
        return c;
    }
}
    public class rect extends sp2d{
    protected int h,w;
    byte state;
    int limitu=0,limitd=500;
    public rect(int x,int y,int w,int h,Color c){
        this.x=x;
        this.y=y;
        this.h=h;
        this.w=w;
        this.c=c;
    }
        void grow(){
            h+=10;
            w+=10;
        }
        void shrink(){
            h-=10;
            w-=10;
        }
    void update(){
        System.out.println(">>bounce<<");
        //x++;
        switch(state){
                case UP:  if(y<=limitu+5){aa=true;state=DOWN;}else {y-=5; }  break;
                case DOWN:  if(y>=limitd+5){state=UP;}else{y+=5;} break;    
            }
            
           if(aa){ limitu +=100;aa=false;}
          // if(limitu==limitd)t.stop();
    }
    void paint(Graphics g){
        if(active){
            g.setColor(c);
            if(fill){
                g.fillRect(x, y, w, h);                
            }else g.drawRect(x, y, w, h);
        }
    }
    }

    public void initsprite(){
    
s[0]=new rect(25,25,100,100,Color.red);
s[1]=new rect(300,300,50,50,Color.CYAN);
s[2]=new rect(100,100,100,100,Color.MAGENTA);
s[3]=new rect(150,50,20,20,Color.GREEN);
for(int i=0;i<no;++i){
    s[i].setActive(true);
    ((sp2d)s[i]).setFill(true);
    ((sp2d)s[i]).setcolor(((rect)s[i]).c);
}

}
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        setBackground(Color.black);
      // c=new Font("Courier",Font.BOLD+Font.ITALIC,24);
      
       initsprite();
       img=createImage(1000, 1000);
       buff=img.getGraphics();

        // TODO start asynchronous download of heavy resources
    }
public void start(){
    t=new Thread(this);
if(t!=null)t.start();
}
    public void paint(Graphics g) {
        System.out.println("<<<paint>>>");
      // buff.setFont(c);
       /* FontMetrics m = g.getFontMetrics();
        int len = m.stringWidth(a);
        int w = (bounds().width - len) / 2;
        int h = bounds().height / 2;*/
       // buff.setColor(Color.yellow);
     //  buff.drawString(a, 10, 10);
        buff.setColor(Color.blue);buff.fillRect(0, 0, 1000,1000);
        buff.setColor(Color.red);
        buff.fillOval(tx1,ty1 , 100, 100);
    
        for(int i=0;i<no;++i){
       
        
        s[i].paint(buff);
    }
     g.drawImage(img, 0, 0, this);

    }
public void update(Graphics g){
    paint(g);
}
public void updatesprite(){
    
    tx1++;ty1++;
    for(int i=0;i<no;++i){
        s[i].update();
    }
}
public void run(){
   while(true){ 
       repaint();
    updatesprite();
    try {
        t.sleep(rr);
    } catch (InterruptedException ex) {
        Logger.getLogger(app6_sprite_events.class.getName()).log(Level.SEVERE, null, ex);
    }
   }
}
public void stop(){
    t.stop();
}

    public boolean mouseDown(Event e, int x, int y) {
        showStatus("down" + x + "," + y);
    if(x>=((rect)s[0]).x&&x<=((rect)s[0]).x+100&&y>=((rect)s[0]).y&&y<=((rect)s[0]).y+100){
        ((rect)s[0]).active=false;
    }
        return true;
    }

    public boolean mouseUp(Event e, int x, int y) {
        showStatus("up" + x + "," + y);
((rect)s[0]).x=x;((rect)s[0]).y=y;
((rect)s[0]).limitu=y;
 ((rect)s[0]).active=true;
        return true;
    }

    public boolean mouseDrag(Event e, int x, int y) {
        showStatus("drag" + x + "," + y);

        return true;
    }

    public boolean mouseMove(Event e, int x, int y) {
        showStatus("move" + x + "," + y);

        return true;
    }

    public boolean mouseEnter(Event e, int x, int y) {
        showStatus("enter" + x + "," + y);
        
        return true;
    }

    public boolean mouseExit(Event e, int x, int y) {
        showStatus("exit" + x + "," + y);

        return true;
    }
    public boolean keyDown(Event e,int key){
        switch(key){
            case Event.RIGHT:    s[0].grow();    repaint();   
            break; 
            case Event.LEFT:    s[0].shrink();    repaint();   
            break; 
          
        }
        return true;
    }
// TODO overwrite start(), stop() and destroy() methods
}
