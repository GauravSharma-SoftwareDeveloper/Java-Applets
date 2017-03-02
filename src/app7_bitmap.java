/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author luffy
 */
import java.applet.*;
import java.awt.*;
import java.net.*;


public class app7_bitmap extends Applet{
    Image i;
   AudioClip c ;
    /*
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        System.out.println(getCodeBase());
       i=getImage(getDocumentBase(),"alien.jpg");
    //c= getAudioClip(getDocumentBase(),"1.au");c.play();
     //  c=getAudioClip(getDocumentBase(), "1.au");
// TODO start asynchronous download of heavy resources
    }
  public void paint(Graphics g) {
        g.drawImage(i,50,50,150,150,this);    
  }

  public boolean mouseUp(Event e, int x, int y) {
        showStatus("up" + x + "," + y);
   c.play();
        return true;
    }
}
