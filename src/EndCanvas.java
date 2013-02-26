
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
 * @author Nokia
 */
public class EndCanvas extends Canvas {
    //int irritating;
     //String please_recognize;
    int menux,menuy,exitx,exity,replayx,replayy;
    Image img,background;
    Sprite menu,replay,exit;
    int position,posx,posy;
    EndCanvas()
    {
        setFullScreenMode(true);
        try {
            background=Image.createImage("/Batman(240-320).png");
            Image img=Image.createImage("/Batmansymbolsprite(exit).png");
            exit = new Sprite(img, img.getWidth()/2, img.getHeight());
            exit.defineReferencePixel(0, img.getHeight());
            img=Image.createImage("/Batmansymbolsprite(replay).png");
            replay = new Sprite(img, img.getWidth()/2, img.getHeight());
            replay.defineReferencePixel(0, img.getHeight());
            img=Image.createImage("/Batmansymbolsprite(menu).png");
            menu = new Sprite(img, img.getWidth()/2, img.getHeight());
            menu.defineReferencePixel(0, img.getHeight());
            menu.setFrame(1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        menux = 0;
        menuy = getHeight() - 30;
        posx = menux;
        posy = menuy;
        exitx = getWidth()/3;
        exity = getHeight() - 30;
        replayx = (getWidth()/3)*2;
        replayy = getHeight() - 30;
        menu.setRefPixelPosition(menux, menuy);
        replay.setRefPixelPosition(replayx, replayy);
        exit.setRefPixelPosition(exitx, exity);
        position = 0;
    }

int x=getHeight()/3 + 20;
    protected void paint(Graphics g)
    {
        String str = "Score :";
         g.drawImage(background, 0, 0, Graphics.TOP|Graphics.LEFT);
         menu.paint(g);
         exit.paint(g);
         replay.paint(g);
         g.setColor(255, 255, 255);
 g.drawString("Game over", getWidth()/2 - 40,10, Graphics.TOP| Graphics.LEFT);
 g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
 g.drawString(Midlet.reason, 10 ,40, Graphics.TOP| Graphics.LEFT);
 g.drawString(str, (getWidth()/2-40),getHeight()/3, Graphics.TOP|Graphics.LEFT);
 if(Menu.displayer == 0)
 {
     g.drawString(Integer.toString(MyGame1.score), (getWidth()/2 + 40), getHeight()/3,Graphics.TOP|Graphics.LEFT);

 }
 else if(Menu.displayer == 1)
 {
     g.drawString(Integer.toString(MyGame2.score1), (getWidth()/2 + 40), getHeight()/3,Graphics.TOP|Graphics.LEFT);
 }
 
 if(Midlet.new_score==true)
        {
            g.drawString("CONGRATULATIONS",getWidth()/2, x+10, Graphics.HCENTER|Graphics.BASELINE);
            g.drawString("NEW HIGH SCORE", getWidth()/2, x+10+20, Graphics.HCENTER|Graphics.BASELINE);
            if(Menu.displayer == 0)
            {
                for(int i=0;i<5;++i)
            { Integer a= new Integer(Midlet.scores_classic[i]);
              String string= a.toString();
              g.drawString(string,getWidth()/2,x+50+(i+1)*10,Graphics.HCENTER|Graphics.TOP);
            }
            }
            else if(Menu.displayer == 1)
            {
                for(int j=0;j<5;++j)
            { Integer a= new Integer(Midlet.scores_timer[j]);
              String string= a.toString();
              g.drawString(string,getWidth()/2,x+50+(j+1)*20,Graphics.HCENTER|Graphics.TOP);
            }
            }
            

        }
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        g.drawString("Menu", menux,menuy, Graphics.TOP|Graphics.LEFT);
        g.drawString("Exit", exitx,exity, Graphics.TOP|Graphics.LEFT);
         g.drawString("Replay", replayx,replayy, Graphics.TOP|Graphics.LEFT);
    }
    protected void keyPressed(int keyCode)
    {
        int gameAction = getGameAction(keyCode);
        boolean key = false;
        if(gameAction == RIGHT||keyCode==KEY_NUM6)
        {
            if(position == 0 && key != true)
            {
                exit.setFrame(1);
                menu.setFrame(0);
                replay.setFrame(0);
                position = 1;
                key = true;
            }
            else if(position ==  1  || position == 2&& key != true)
            {
                exit.setFrame(0);
                menu.setFrame(0);
                replay.setFrame(1);
                position = 2;
                key = true;
            }
            key = false;
        }
        if(gameAction == LEFT||keyCode==KEY_NUM4)
        {
            if(position == 2 && key != true)
            {
                exit.setFrame(1);
                menu.setFrame(0);
                replay.setFrame(0);
                position = 1;
                key = true;
            }
            else if(position ==  1  || position == 0 && key != true)
            {
                exit.setFrame(0);
                menu.setFrame(1);
                replay.setFrame(0);
                position = 0;
                key = true;
            }
            key = false;
        }
        if(gameAction == FIRE||keyCode==KEY_NUM5)
        {
            if(position == 0 && key != true)
            {
                Runtime.getRuntime().gc();
                key = true;
                Main_Menu menu = new Main_Menu();
                Midlet.display.setCurrent(menu);
                menu.start();
                menu = null;



            }
            else if(position ==  1  && key != true)
            {
                key = true;
                midlet.notifyDestroyed();

            }
            else if(position ==  2  && key != true)
            {
                key = true;
               if(Menu.displayer == 0)
               {
                    
                try
                {
                    Runtime.getRuntime().gc();
                   MyGame1 game = new MyGame1();
                Thread runner = new Thread(game);
                Midlet.display.setCurrent(game);
                runner.start();
                game = null;
                   }
                catch(OutOfMemoryError e)
                {
                    Runtime.getRuntime().gc();
                   }
           
    // release some (all) of the above objects
                
                
               }
                else if(Menu.displayer == 1)
               {
                    
                try
                {
                    Runtime.getRuntime().gc();
                   MyGame2 game1 = new MyGame2();
                Thread runner1 = new Thread(game1);
                Midlet.display.setCurrent(game1);
                runner1.start();
                game1 = null;
                   }
                catch(OutOfMemoryError e)
                {
                    Runtime.getRuntime().gc();
                }
           
               
               }

            }


            key = false;
        }
        repaint();
    }
    static MIDlet midlet;
    public void setMid(MIDlet mid)
    {
        midlet = mid;
    }
    public void start()
    {
        repaint();
    }

}
