
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shruthi
 */
public class CreditCanvas extends Canvas
{
    int backx,backy;
    Sprite cursor;
    Image img;
    
    public CreditCanvas()
    {
        setFullScreenMode(true);
        try {
            img = Image.createImage("/cursor2.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        cursor = new Sprite(img);
        cursor.defineReferencePixel(0, 0);
        backx = 0;
        backy = getHeight() - 20;

    }
    protected void paint(Graphics g) {
        g.setColor(0,0,0);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(255,0,0);
        g.drawString("CREDITS", getWidth()/2, getHeight()/8, Graphics.TOP|Graphics.LEFT);
        g.drawString("Back", backx, backy, Graphics.TOP|Graphics.LEFT);

    }
     protected void keyPressed(int keyCode)
    {
        int gameAction = getGameAction(keyCode);
        if(gameAction == FIRE)
        {
            Midlet.display.setCurrent(Midlet.main);
            Midlet.main.start();
        }
        repaint();
    }
    public void start()
    {
        repaint();
    }

}
