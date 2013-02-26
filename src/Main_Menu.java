
import java.io.IOException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.midlet.MIDlet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shruthi
 */
public class Main_Menu extends Canvas
{
    Sprite play,exit;
    //Image playimg;
    Image background;
    int displayer;
    int posx,posy;
    public Main_Menu()
    {
        setFullScreenMode(true);
        try {
            background=Image.createImage("/Batman(240-320).png");
            Image img=Image.createImage("/Batmansymbolsprite.png");
            play=new Sprite(img, img.getWidth()/2, img.getHeight());
            play.defineReferencePixel(0,img.getHeight());
            play.setFrame(1);
            play.setRefPixelPosition(143, 250);
            img=Image.createImage("/Batmansymbolsprite(exit).png");
            exit=new Sprite(img, img.getWidth()/2, img.getHeight());
            exit.defineReferencePixel(0,img.getHeight());
            exit.setRefPixelPosition(143, 290);
                    //cursor = Image.createImage("/cursor2.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        displayer = 4;
      //  cursor_img = new Sprite(cursor);
        posx = getWidth()/3 + 20;
        posy = getHeight()/8 + 20;
      //  cursor_img.defineReferencePixel(0, 0);

    }
    protected void paint(Graphics g)
    {
        g.drawImage(background, 0, 0, Graphics.TOP|Graphics.LEFT);
        play.paint(g);
        exit.paint(g);


    }
    protected void keyPressed(int keyCode)
    {
        int gameAction = getGameAction(keyCode);
        //boolean key = false;
         if(gameAction == UP||keyCode==KEY_NUM2)
        {
           play.setFrame(1);
           exit.setFrame(0);
          displayer = 0;
          repaint();
        }
        if(gameAction == DOWN||keyCode==KEY_NUM8)
        {
           exit.setFrame(1);
           play.setFrame(0);
           displayer = 1;
           repaint();
        }
        if(gameAction == FIRE||keyCode==KEY_NUM5)
        {
            if(displayer == 0)
            {
                Runtime.getRuntime().gc();
               Runtime.getRuntime().gc();
                Menu menu = new Menu();
                Midlet.display.setCurrent(menu);
                menu.run();
                menu = null;
            }
            else if(displayer == 1)
            {
               midlet.notifyDestroyed();
            }
        }
        //if(gameAction == UP||keyCode==KEY_NUM2)
        

          /*if(displayer == 4 || displayer == 3 && key != true)
          {
              posx = getWidth()/3 + 20;
               posy = getHeight()/8 + 20;
               key = true;
               displayer = 4;
          }
          else if(displayer == 2 && key != true)
          {
               posx = getWidth()/3 + 20;
               posy = getHeight()/8 + 40;
               key = true;
               displayer = 3;
          }
          else if(displayer == 1 && key != true)
          {
               posx = getWidth()/3 + 20;
               posy = getHeight()/8 + 60;
               key = true;
               displayer = 2;
          }*/
          //key = false;
       // }
       // if(gameAction == DOWN||keyCode==KEY_NUM8)
      //  {
          /*if(displayer == 4 && key != true)
          {
              posx = getWidth()/3 + 20;
               posy = getHeight()/8 + 40;
               key = true;
               displayer = 3;
          }
          else if(displayer == 3 && key != true)
          {
               posx = getWidth()/3 + 20;
               posy = getHeight()/8 + 60;
               key = true;
               displayer = 2;
          }
          else if(displayer == 2 || displayer == 1 && key != true)
          {
               posx = getWidth()/3 + 20;
               posy = getHeight()/8 + 80;
               key = true;
               displayer = 1;
          }*/
       //   key = false;
      //  }
      /*  if(gameAction == FIRE)
        {
            if(displayer == 4)
            {
                Runtime.getRuntime().gc();
                Menu menu = new Menu();
                Midlet.display.setCurrent(menu);
                menu.run();
                menu = null;

            }
            else if(displayer == 3)
            {

                CreditCanvas credit = new CreditCanvas();
                Midlet.display.setCurrent(credit);
                credit.start();
            }
            else if(displayer == 2)
            {
                  HelpScreen help = new HelpScreen();
                Midlet.display.setCurrent(help);
                help.start();

            }
            else if(displayer == 1)
            {
                midlet.notifyDestroyed();
            }
        }*/
        repaint();

    }
    static MIDlet midlet;
    public void setMid(MIDlet mid)
    {
        midlet=mid;
    }
    public void start()
    {
        repaint();
    }

}
