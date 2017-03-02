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
public class app5_strings_mouseevents extends Applet {
       Font c;
       String a="haaaaaaaaaaaa!!!";
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
       c=new Font("Courier",Font.BOLD+Font.ITALIC,24);
      
        
        // TODO start asynchronous download of heavy resources
    }
public void paint(Graphics g){
    g.setFont(c);
    FontMetrics m=g.getFontMetrics();
    int len =m.stringWidth(a);
    int w=(bounds().width-len)/2;
    int h=bounds().height/2;
    g.setColor(Color.green);
    g.drawString(a, w, h);
}
public boolean mouseDown(Event e,int x,int y){
    showStatus("down"+x+","+y);
    
    return true;
}
public boolean mouseUp(Event e,int x,int y){
    showStatus("up"+x+","+y);
    
    return true;
}

public boolean mouseDrag(Event e,int x,int y){
    showStatus("drag"+x+","+y);
    
    return true;
}
public boolean mouseMove(Event e,int x,int y){
    showStatus("move"+x+","+y);
    
    return true;
}
public boolean mouseEnter(Event e,int x,int y){
    showStatus("enter"+x+","+y);
           try {
               Thread.sleep(1000);
           } catch (InterruptedException ex) {
               Logger.getLogger(app5_strings_mouseevents.class.getName()).log(Level.SEVERE, null, ex);
           }
    return true;
}
public boolean mouseExit(Event e,int x,int y){
    showStatus("exit"+x+","+y);
    
    return true;
}
// TODO overwrite start(), stop() and destroy() methods
}
