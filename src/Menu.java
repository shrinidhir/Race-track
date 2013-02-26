
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
 * @author Shrinidhi
 */
public class Menu extends Canvas{
    Image background;
    Sprite mode_timer,mode_classic;
    int posx,posy;
    int toggle;
    boolean running;
   static int displayer;
    public Menu()
    {
        setFullScreenMode(true);
        try {
            background=Image.createImage("/Batman(240-320).png");
            Image img1=Image.createImage("/Batmansymbolsprite(timer).png");
            mode_timer = new Sprite(img1,img1.getWidth()/2,img1.getHeight());
            mode_timer.defineReferencePixel(0, img1.getHeight());
            mode_timer.setRefPixelPosition(143, 290);
            img1=Image.createImage("/Batmansymbolsprite(classic).png");
            mode_classic=new Sprite(img1, img1.getWidth()/2, img1.getHeight());
            mode_classic.defineReferencePixel(0, img1.getHeight());
            mode_classic.setRefPixelPosition(143, 250);
            mode_timer.setFrame(1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        posx = getWidth()/2;
        posy = getHeight()/2 + 100  ;
        displayer = 1;
    }

    protected void paint(Graphics g)
    {
        g.drawImage(background, 0, 0, Graphics.TOP|Graphics.LEFT);
        g.setColor(255, 255, 255);
       mode_timer.paint(g);
       mode_classic.paint(g);
    }
    protected void keyPressed(int keyCode)
    {
        int gameAction = getGameAction(keyCode);

        if(gameAction == UP||keyCode==KEY_NUM2)
        {
           mode_timer.setFrame(0);
           mode_classic.setFrame(1);
          displayer = 0;
          repaint();
        }
        if(gameAction == DOWN||keyCode==KEY_NUM8)
        {
           mode_timer.setFrame(1);
           mode_classic.setFrame(0);
           displayer = 1;
           repaint();
        }
        if(gameAction == FIRE||keyCode==KEY_NUM5)
        {
            if(displayer == 0)
            {
                Runtime.getRuntime().gc();
               MyGame1 game = new MyGame1();
               Thread runner = new Thread(game);
               Midlet.display.setCurrent(game);
               runner.start();
            }
            else if(displayer == 1)
            {
                Runtime.getRuntime().gc();
                MyGame2 game1 = new MyGame2();
               Thread runner1 = new Thread(game1);
               Midlet.display.setCurrent(game1);
               runner1.start();


            }
        }

    }
    public void run()
    {
        {
            repaint();
        }
    }


}
