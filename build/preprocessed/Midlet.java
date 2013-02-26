
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * @author admin
 */
public class Midlet extends MIDlet {
    static Display display;
    static MyGame1 game;
    static EndCanvas end;
    static String reason;
    //static MyGame2 game1;

       private static RecordStore rs_timer,rs_classic,rs;
   private static RecordEnumeration re_timer,re_classic,re;
   static int no_of_records_timer,no_of_records_classic;
    static int[] scores_timer,scores_classic;
   static int last_id;
   //Menu menu;
   static Main_Menu main;
   private final String REC_STORE="High Score";
    public void startApp() {

        try {
            rs_timer = RecordStore.openRecordStore("TIMER STORE", true);
             rs_classic = RecordStore.openRecordStore("CLASSIC STORE", true);
            re_timer = rs_timer.enumerateRecords(null, null, false);
            re_classic = rs_classic.enumerateRecords(null, null, false);
            no_of_records_timer = rs_timer.getNumRecords();
            no_of_records_classic = rs_classic.getNumRecords();
            scores_timer = new int[5];
            scores_classic=new int[5];
            readScores();

            display = Display.getDisplay(this);
            game = new MyGame1();

            //game1 = new MyGame2();
            //menu = new Menu();
            main = new Main_Menu();
              end = new EndCanvas();
            //Thread runner = new Thread(menu);
            display.setCurrent(main);
            main.start();
            main.setMid(this);
            end.setMid(this);
            //menu.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 public static void readScores()
{       try {
             no_of_records_timer = rs_timer.getNumRecords();
            no_of_records_classic = rs_classic.getNumRecords();
            byte[] recData = new byte[50];
            ByteArrayInputStream strmBytes = new ByteArrayInputStream(recData);
            DataInputStream strmDataType = new DataInputStream(strmBytes);
             if (rs_timer.getNumRecords() > 0) {
                ComparatorInteger comp = new ComparatorInteger();
                RecordEnumeration re = rs_timer.enumerateRecords(null, comp, false);
                int i = 0;
                while (re.hasNextElement()) {
                    try {
                    last_id=re.nextRecordId();
                    rs_timer.getRecord(last_id, recData, 0);

                        try {
                            scores_timer[i] = strmDataType.readInt();
                            i++;
                        } catch (IOException ex) {

                        }
                    } catch (RecordStoreException ex) {
                        ex.printStackTrace();
                    }
                   strmBytes.reset();
                }
            }

            if (rs_classic.getNumRecords() > 0) {
                ComparatorInteger comp = new ComparatorInteger();
                RecordEnumeration re = rs_classic.enumerateRecords(null, comp, false);
                int i = 0;
                while (re.hasNextElement()) {
                    try {
                    last_id=re.nextRecordId();
                    rs_classic.getRecord(last_id, recData, 0);

                        try {
                            scores_classic[i] = strmDataType.readInt();
                            i++;
                        } catch (IOException ex) {

                        }
                    } catch (RecordStoreException ex) {
                        ex.printStackTrace();
                    }
                   strmBytes.reset();
                }
            }
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        }
}

    public static void writeScore(int iData)
{
     new_score=true;
     new_high_score=iData;
     try
     {
       ByteArrayOutputStream strmBytes = new ByteArrayOutputStream();
       DataOutputStream strmDataType = new DataOutputStream(strmBytes);
       byte[] record;
       strmDataType.writeInt(iData);
       strmDataType.flush();
       record = strmBytes.toByteArray();
        if(Menu.displayer==0)
       {
       rs_classic.addRecord(record, 0, record.length);
       }
       else if(Menu.displayer==1)
       {
            rs_timer.addRecord(record, 0, record.length);
       }
       strmBytes.reset();
       strmBytes.close();
       strmDataType.close();
     }
     catch (Exception e)
     {

     }
     readScores();
}
    static int new_high_score;
    static boolean new_score=false;
    public static void GameEnd()
    { if(Menu.displayer == 0)
            {
                 int new_score=MyGame1.score;
      if(no_of_records_classic<5)
          writeScore(new_score);
      else
      {  if(scores_classic[4]<new_score)
              { try {
                    rs_classic.deleteRecord(last_id);
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
                writeScore(new_score);
              }
      }

        }
      else if(Menu.displayer==1)
    {
        int new_score=MyGame2.score1;
      if(no_of_records_timer<5)
          writeScore(new_score);
      else
      {  if(scores_timer[4]<new_score)
              { try {
                    rs_timer.deleteRecord(last_id);
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
                writeScore(new_score);
              }
      }
    }

      
    }
    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
class ComparatorInteger implements RecordComparator
{
     private byte[] recData = new byte[10];

     // Read from a specified byte array
     private ByteArrayInputStream strmBytes = null;

     // Read Java data types from the above byte array
     private DataInputStream strmDataType = null;

     public void compareIntClose()
     {
       try
       {
         if (strmBytes != null)
           strmBytes.close();
         if (strmDataType != null)
           strmDataType.close();
       }
       catch (Exception e)
       {}
     }

     public int compare(byte[] rec1, byte[] rec2)
     {
       int x1, x2;

       try
       {
         // If either record is larger than our buffer, reallocate
         int maxsize = Math.max(rec1.length, rec2.length);
         if (maxsize > recData.length)
           recData = new byte[maxsize];

         // Read record #1
         // We want the integer from the record, which is
         // the second "field" thus we must read the
         // String first to get to the integer value
         strmBytes = new ByteArrayInputStream(rec1);
         strmDataType = new DataInputStream(strmBytes);
         x1 = strmDataType.readInt();  // Read integer

         // Read record #2
         strmBytes = new ByteArrayInputStream(rec2);
         strmDataType = new DataInputStream(strmBytes);
         x2 = strmDataType.readInt();  // Read integer

         // Compare record #1 and #2
         if (x1 == x2)
           return RecordComparator.EQUIVALENT;
         else if (x1 >= x2)
           return RecordComparator.PRECEDES;
         else
           return RecordComparator.FOLLOWS;

       }
       catch (Exception e)
       {
         return RecordComparator.EQUIVALENT;
       }
     }
}
class MyGame1 extends GameCanvas implements Runnable

{   int track_y;
    int car_x,car_y;
    Image track;
    boolean add,decrement,pcdecrement;
    static String reason_classic = "";
    Sprite othercar,oldlady,car,policecar;
    static int score;
    int track1x,track2x,track3x;
    boolean[] police_collide,tank_collide,barricade_collide;
    Player player;
    byte[] audio;
    MyGame1()
    { super(false);
      setFullScreenMode(true);
      score=0;
        track_y=0;
      car_x= getWidth()/5;
        try 
        {
            Image img = Image.createImage("/batman.png");
            track=scale(img,getWidth(),getHeight());
            car=new Sprite(Image.createImage("/batmobile1.png"));
            car.defineReferencePixel(0, car.getHeight());
            img=Image.createImage("/police car sprites.png");
            policecar=new Sprite(img,img.getWidth()/8,img.getHeight());
            policecar.defineReferencePixel(0, img.getHeight());
            policecar.setRefPixelPosition(0, 0);
            img=Image.createImage("/Car_explosionresized.png");
            othercar=new Sprite(img,img.getWidth()/7,img.getHeight());
            othercar.defineReferencePixel(0, img.getHeight());
            othercar.setRefPixelPosition(0, 0);
            img=Image.createImage("/barricade.png");
            oldlady=new Sprite(img,img.getWidth()/2,img.getHeight());
            oldlady.defineReferencePixel(0, img.getHeight());
            oldlady.setRefPixelPosition(0, 0);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
      car_y=getHeight()-car.getHeight();
      car.setRefPixelPosition(car_x, car_y);
      track1x=49;
      track2x=120;
      track3x=194;
      police_collide=new boolean[3];
      tank_collide=new boolean[3];
      barricade_collide=new boolean[3];
      for(int i=0;i<3;i++)
      {
          oldlady_x[i]=0;
          oldlady_y[i]=0;
          othercar_x[i]=0;
          othercar_y[i]=0;
          policecar_x[i]=0;
          policecar_y[i]=0;
          policecar_present[i]=false;
          oldlady_present[i]=false;
          othercar_present[i]=false;
          police_collide[i]=false;
          tank_collide[i]=false;
          barricade_collide[i]=false;
      }
      othercar_present[0]=true;
      othercar_x[0]=track1x;
       InputStream is=getClass().getResourceAsStream("/The_Dark_Knight.mp3");
     audio=new byte[181000];
        try {
            is.read(audio, 0, 181000);
           is=new ByteArrayInputStream(audio,0, 181000);
            player = Manager.createPlayer(is, "audio/mpeg");
            player.realize();
            is.close();;
            is=null;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
    }
    public static Image scale(Image src, int width, int height)
      {
        long start = System.currentTimeMillis();
        int scanline = src.getWidth();
        int srcw = src.getWidth();
        int srch = src.getHeight();
        int buf[] = new int[srcw * srch];
        src.getRGB(buf, 0, scanline,0, 0, srcw, srch);
        int buf2[] = new int[width*height];
        for (int y=0;y<height;y++) {
            int c1 = y*width;
            int c2 = (y*srch/height)*scanline;
           for (int x=0;x<width;x++) {
                buf2[c1 + x] = buf[c2 + x*srcw/width];
            }
        }

        Image img = Image.createRGBImage(buf2, width, height, true);
        return img;
    }

    protected void buildGraphics(Graphics g)
    {
    
    g.drawImage(track,0,track_y,Graphics.TOP|Graphics.LEFT);
    g.drawImage(track,0,track_y-getHeight(),Graphics.TOP|Graphics.LEFT);
    //g.drawImage(car,car_x,car_y,Graphics.TOP|Graphics.LEFT);
    car.setRefPixelPosition(car_x, car_y);
    car.paint(g);
    //g.drawImage(oldlady, 0, oldlady_y, Graphics.TOP|Graphics.LEFT);
    for(int i=0;i<3;i++)
    {
    oldlady.setRefPixelPosition(oldlady_x[i], oldlady_y[i]);
    oldlady.paint(g);
    if(barricade_collide[i])
    {
        for(int j=1;j<2;j++)
        {
            oldlady.setFrame(j);
            oldlady.paint(g);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
        }
        oldlady.setFrame(0);
        barricade_collide[i]=false;
        oldlady_y[present_track-1]=0-oldlady.getHeight();
    oldlady_x[present_track-1]=0;
    oldlady_present[present_track-1]=false;
    }
    othercar.setRefPixelPosition(othercar_x[i], othercar_y[i]);
    othercar.paint(g);
    if(tank_collide[i])
    {
        for(int j=1;j<7;j++)
        {
            othercar.setFrame(j);
            othercar.paint(g);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
        }
        othercar.setFrame(0);
        tank_collide[i]=false;
        othercar_y[present_track-1]=0-othercar.getHeight();
    othercar_x[present_track-1]=0;
    othercar_present[present_track-1]=false;
    }
    policecar.setRefPixelPosition(policecar_x[i], policecar_y[i]);
    policecar.paint(g);
    if(police_collide[i])
    {
        for(int j=1;j<8;j++)
        {
            policecar.setFrame(j);
            policecar.paint(g);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
        }
        policecar.setFrame(0);
        police_collide[i]=false;
        policecar_y[present_track-1]=0-policecar.getHeight();
    policecar_x[present_track-1]=0;
    policecar_present[present_track-1]=false;
     }
    }
    g.setColor(0, 0, 0);

        //if(level<6)
        //str= time+"/"+tim_cons+"  LEVEL:"+level;
        //else
        //str= time+"/"+tim_cons+"  L:"+level;
        //g.drawString(str,0,0, Graphics.TOP| Graphics.LEFT);
        String str1="SCORE:"+ score;//+"/"+level_score;

        g.drawString(str1,0,0,Graphics.TOP|Graphics.LEFT);
        //int width=g.getFont().stringWidth(str)+g.getFont().stringWidth(str1);
        str1=null;
    }
    int[] oldlady_y=new int[3];
    int[] othercar_y=new int[3];
    int[] oldlady_x=new int[3];
    int[] othercar_x=new int[3];
    int[] policecar_x=new int[3];
    int[] policecar_y=new int[3];
    boolean[] policecar_present= new boolean[3];
    boolean[] oldlady_present=new boolean[3];
    boolean[] othercar_present=new boolean[3];

    public void slide()
    {
        
        track_y+=4 ;
        for(int i=0;i<3;i++)
        {
        if(oldlady_present[i])
        {
            oldlady_y[i]+=4 ;
            oldlady.setRefPixelPosition(oldlady_x[i], oldlady_y[i]);
        }
        if(othercar_present[i])
        {
            othercar_y[i]+=4 ;
            othercar.setRefPixelPosition(othercar_x[i], othercar_y[i]);
        }
        if(policecar_present[i])
        {
            policecar_y[i]+=(4 );
            policecar.setRefPixelPosition(policecar_x[i], policecar_y[i]);
        }
        if(oldlady_y[i]>getHeight()+oldlady.getHeight())
        {
            oldlady_y[i]=0;
            oldlady.setRefPixelPosition(oldlady_x[i], oldlady_y[i]);
            oldlady_present[i]=false;
        }
        if(othercar_y[i]>getHeight()+othercar.getHeight())
        {
            othercar_y[i]=0;
            othercar.setRefPixelPosition(othercar_x[i], othercar_y[i]);
            othercar_present[i]=false;
        }
        if(policecar_y[i]>getHeight()+policecar.getHeight())
        {
            policecar_y[i]=0;
            policecar.setRefPixelPosition(policecar_x[i], policecar_y[i]);
            policecar_present[i]=false;
        }
        }
        
     // car_y--;
    if(track_y>getHeight())
        track_y=0;
      add = false;
       decrement = false;
   // if(car_y<0)
     //   car_y=getHeight()-car.getHeight();
if(!tank_collide[present_track-1])
{
if(car.collidesWith(othercar, true))
{
    add = true;
    tank_collide[present_track-1]=true;
    /*othercar_y[present_track-1]=0-othercar.getHeight();
    othercar_x[present_track-1]=0;
    othercar_present[present_track-1]=false;*/
}
}
if(!barricade_collide[present_track-1])
{
        if(car.collidesWith(oldlady, true))
        {
            decrement = true;
            barricade_collide[present_track-1]=true;
            /*
            oldlady_y[present_track-1]=0-oldlady.getHeight();
            oldlady_x[present_track-1]=0;
            oldlady_present[present_track-1]=false;*/
        }
}
if(!police_collide[present_track-1])
{
       if (car.collidesWith(policecar, true))
       {
           pcdecrement=true;
          police_collide[present_track-1]=true;
           /*policecar_y[present_track-1]=0-policecar.getHeight();
           policecar_x[present_track-1]=0;
           policecar_present[present_track-1]=false;*/
       }
}
        //if(car.collidesWith(car, keyPressed))
     
        boolean generate=true;
        for(int j=0; j<3;j++)
        {
            if (oldlady_present[j]==true || othercar_present[j]==true || policecar_present[j] == true)
            {
                generate=false;
            }
        }
        if(generate==true)
        {
           
          System.out.println("oldlady");
          //oldlady.setVisible(true);
          if(present_track==1)
          {
          oldlady_x[present_track-1]=present_x;
          oldlady.setRefPixelPosition(present_x, 0);
            }
            else
          {
              oldlady_x[present_track-1]=present_x-30;
          oldlady.setRefPixelPosition(present_x-30, 0);
            }
          oldlady_present[present_track-1]=true;
     
        }
    }
    int ctr = 0;
    boolean running=true;
    MIDlet midlet;
    public void reset()
    {
        running = true;
    }
    public void setMid(MIDlet mid)
    {
        midlet = mid;
    }
    public void run() {
        player.setLoopCount(-1);
        try {
            player.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        reset();
        buildGraphics(getGraphics());

        //System.out.println(System.currentTimeMillis());
        while(running)
        {
           //// System.out.println(System.currentTimeMillis());
           // buildGraphics(getGraphics());
            //checkInput();
            slide();
             if(add == true)
      {
          score+=30;
      }
      if(decrement == true)
      {
          score-=20;
          ctr++;
      }
             if(pcdecrement == true)
      {
          score-=10;
          ctr++;
      }
            add=false;
            decrement = false;
            pcdecrement=false;
            buildGraphics(getGraphics());
            flushGraphics();
            //slide();
            //System.out.println(track_y);
            /*try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }*/
            if(ctr == 5 || score<0)
            {

                if(ctr == 5)
                {
                    reason_classic = "You weren't a good Samaritan!! You pushed the old lady down 5 times";
                }
                else if(score <0)
                {
                    reason_classic = " Your score is too low :(";
                }
                running=false;
            }
            Runtime.getRuntime().gc();
        }
         try {
                player.stop();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
        Midlet.reason = reason_classic;
         Midlet.GameEnd();
               Midlet.display.setCurrent(Midlet.end);
               Midlet.end.start();

            }
    /*public int getKeyStates() {
        DisplayAccess displayAccess = GameMap.get(this);
	if (displayAccess != null) {
	    return displayAccess.getKeyMask();
	} 
	return 0;
    }*/
    int present_track=1,present_x=track1x;
    boolean keyPressed=false;
   //public void checkInput()
    protected void keyPressed(int keyCode)
    {
       
       /*int gameAction=getKeyStates();
       
       switch(gameAction)
       {
           case LEFT_PRESSED:
           {
               System.out.println("Key Pressed");
          if(car_x>getWidth()/5)
            car_x-=getWidth()/5;
            break;
           }
           case RIGHT_PRESSED:
           {
            System.out.println("Key Pressed");
     if(car_x<3*getWidth()/5)
            car_x+=getWidth()/5;
            break;
           }
           default:
           {
               
           }
               
               
            
       }*/
        int keyState = getGameAction(keyCode);
      if (keyCode==KEY_NUM4||keyState==LEFT)
      {
          System.out.println("Key Pressed");
          if(present_track==1)
          {
              reason_classic = "You fell off the flyover";
              running=false;
          }
        else
          {
          if(present_track!=1)
          {
            present_track--;
            keyPressed=true;
          }
          }
          
      }
      else if (keyCode==KEY_NUM6||keyState==RIGHT)
      {
          System.out.println("Key Pressed");
          if(present_track==3)
          {
              reason_classic = "You fell off the flyover!:(";
              running=false;
          }
        else
          {
            if(present_track!=3)
        {
            present_track++;
            keyPressed=true;
        }
          }
      }
       if(present_track==1)
      {
            present_x=car_x=track1x;

      }
        else if(present_track==2)
        {
            present_x=car_x=track2x;
        }
        else if(present_track==3)
        {
           present_x=car_x=track3x;
        }
       if(keyPressed)
       {
      Random generator=new Random();
      int num=generator.nextInt(20);
      if(num>=0&&num<=3)
      {
          System.out.println("oldlady");
          //oldlady.setVisible(true);
          if(present_track==1)
          {
            oldlady_x[present_track-1]=present_x;
          oldlady.setRefPixelPosition(present_x, 0);
          }
        else
          {
              oldlady_x[present_track-1]=present_x-30;
          oldlady.setRefPixelPosition(present_x-30, 0);
         }
          oldlady_present[present_track-1]=true;
      }
      else if(num>3&&num<=10)
      {
          System.out.println("police car");
          //othercar.setVisible(true);
          policecar_x[present_track-1]=present_x-30;
         policecar.setRefPixelPosition(present_x-30, 0);
          policecar_present[present_track-1]=true;
      }
      else if(num>10)
      {
          System.out.println("other car");
          //othercar.setVisible(true);
          othercar_x[present_track-1]=present_x;
         othercar.setRefPixelPosition(present_x, 0);
          othercar_present[present_track-1]=true;
      }
       }
       keyPressed=false;
      
    }
 }
class MyGame2 extends GameCanvas implements Runnable
{

    static String reason = "";
    int track_y;
    int car_x,car_y;
    Image track;
    int oldlady_ctr;
    Player player;
    byte[] audio;
    boolean add,decrement,pcdecrement;
    Sprite othercar,oldlady,car,policecar;
    static int score1;
    static long time_started;
    static long time;
    int track1x,track2x,track3x;
    boolean[] police_collide,tank_collide,barricade_collide;
    MyGame2()
    { super(false);
      setFullScreenMode(true);
      score1=0;
        track_y=0;
      car_x= getWidth()/5;
        try
        {
            Image img = Image.createImage("/batman.png");
            track=scale(img,getWidth(),getHeight());
            car=new Sprite(Image.createImage("/batmobile1.png"));
            car.defineReferencePixel(0, car.getHeight());
            img=Image.createImage("/police car sprites.png");
            policecar=new Sprite(img,img.getWidth()/8,img.getHeight());
            policecar.defineReferencePixel(0, img.getHeight());
            policecar.setRefPixelPosition(0, 0);
            img=Image.createImage("/Car_explosionresized.png");
            othercar=new Sprite(img,img.getWidth()/7,img.getHeight());
            othercar.defineReferencePixel(0, img.getHeight());
            othercar.setRefPixelPosition(0, 0);
            img=Image.createImage("/barricade.png");
            oldlady=new Sprite(img,img.getWidth()/2,img.getHeight());
            oldlady.defineReferencePixel(0, img.getHeight());
            oldlady.setRefPixelPosition(0, 0);
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
      car_y=getHeight()-car.getHeight();
      car.setRefPixelPosition(car_x, car_y);
      for(int i=0;i<3;i++)
      {
          oldlady_x[i]=0;
          oldlady_y[i]=0;
          othercar_x[i]=0;
          othercar_y[i]=0;
          policecar_x[i]=0;
          policecar_y[i]=0;
          policecar_present[i]=false;
          oldlady_present[i]=false;
          othercar_present[i]=false;
          police_collide[i]=false;
          tank_collide[i]=false;
          barricade_collide[i]=false;
      }
      track1x=49;
      track2x=120;
      track3x=194;
      othercar_present[0]=true;
      othercar_x[0]=track1x;
     /* InputStream is=getClass().getResourceAsStream("/The_Dark_Knight.mp3");
     audio=new byte[181000];
        try {
            is.read(audio, 0, 181000);
           is=new ByteArrayInputStream(audio,0, 181000);
            player = Manager.createPlayer(is, "audio/mpeg");
            player.realize();
            is.close();;
            is=null;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }*/
    }
    public static Image scale(Image src, int width, int height)
      {
        long start = System.currentTimeMillis();
        int scanline = src.getWidth();
        int srcw = src.getWidth();
        int srch = src.getHeight();
        int buf[] = new int[srcw * srch];
        src.getRGB(buf, 0, scanline,0, 0, srcw, srch);
        int buf2[] = new int[width*height];
        for (int y=0;y<height;y++) {
            int c1 = y*width;
            int c2 = (y*srch/height)*scanline;
           for (int x=0;x<width;x++) {
                buf2[c1 + x] = buf[c2 + x*srcw/width];
            }
        }

        Image img = Image.createRGBImage(buf2, width, height, true);
        return img;
    }

    protected void buildGraphics(Graphics g)
    {
   g.drawImage(track,0,track_y,Graphics.TOP|Graphics.LEFT);
    g.drawImage(track,0,track_y-getHeight(),Graphics.TOP|Graphics.LEFT);
    //g.drawImage(car,car_x,car_y,Graphics.TOP|Graphics.LEFT);
    car.setRefPixelPosition(car_x, car_y);
    car.paint(g);
    //g.drawImage(oldlady, 0, oldlady_y, Graphics.TOP|Graphics.LEFT);
    for(int i=0;i<3;i++)
    {
    oldlady.setRefPixelPosition(oldlady_x[i], oldlady_y[i]);
    oldlady.paint(g);
    if(barricade_collide[i])
    {
        for(int j=1;j<2;j++)
        {
            oldlady.setFrame(j);
            oldlady.paint(g);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
        }
        oldlady.setFrame(0);
        barricade_collide[i]=false;
        oldlady_y[present_track-1]=0-oldlady.getHeight();
    oldlady_x[present_track-1]=0;
    oldlady_present[present_track-1]=false;
    }
    othercar.setRefPixelPosition(othercar_x[i], othercar_y[i]);
    othercar.paint(g);
    if(tank_collide[i])
    {
        for(int j=1;j<7;j++)
        {
            othercar.setFrame(j);
            othercar.paint(g);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
        }
        othercar.setFrame(0);
        tank_collide[i]=false;
        othercar_y[present_track-1]=0-othercar.getHeight();
    othercar_x[present_track-1]=0;
    othercar_present[present_track-1]=false;
    }
    policecar.setRefPixelPosition(policecar_x[i], policecar_y[i]);
    policecar.paint(g);
    if(police_collide[i])
    {
        for(int j=1;j<8;j++)
        {
            policecar.setFrame(j);
            policecar.paint(g);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
        }
        policecar.setFrame(0);
        police_collide[i]=false;
        policecar_y[present_track-1]=0-policecar.getHeight();
    policecar_x[present_track-1]=0;
    policecar_present[present_track-1]=false;
     }
    }
    g.setColor(0, 0, 0);
        //if(level<6)
        //str= time+"/"+tim_cons+"  LEVEL:"+level;
        //else
        //str= time+"/"+tim_cons+"  L:"+level;
        //g.drawString(str,0,0, Graphics.TOP| Graphics.LEFT);
        String str1="SCORE:"+ score1;//+"/"+level_score;
        String str2 = "TIME:" + Long.toString(time)  +  "/90";
        g.drawString(str1,0,0,Graphics.TOP|Graphics.LEFT);
        g.drawString(str2, getWidth()/2, 0,Graphics.TOP|Graphics.LEFT);
        //int width=g.getFont().stringWidth(str)+g.getFont().stringWidth(str1);
        str1=null;
        str2 = null;
    }
    int[] oldlady_y=new int[3];
    int[] othercar_y=new int[3];
    int[] oldlady_x=new int[3];
    int[] othercar_x=new int[3];
    int[] policecar_x=new int[3];
    int[] policecar_y=new int[3];
    boolean[] policecar_present= new boolean[3];
    boolean[] oldlady_present=new boolean[3];
    boolean[] othercar_present=new boolean[3];
    int speed = 0;
    public void slide()
    {

        track_y+=6 + speed;
        for(int i=0;i<3;i++)
        {
        if(oldlady_present[i])
        {
            oldlady_y[i]+=4 + speed;
            oldlady.setRefPixelPosition(oldlady_x[i], oldlady_y[i]);
        }
        if(othercar_present[i])
        {
            othercar_y[i]+=4 + speed;
            othercar.setRefPixelPosition(othercar_x[i], othercar_y[i]);
        }
        if(policecar_present[i])
        {
            policecar_y[i]+=4 + speed;
            policecar.setRefPixelPosition(policecar_x[i], policecar_y[i]);
        }
        if(oldlady_y[i]>getHeight()+oldlady.getHeight())
        {
            oldlady_y[i]=0;
            oldlady.setRefPixelPosition(oldlady_x[i], oldlady_y[i]);
            oldlady_present[i]=false;
        }
        if(othercar_y[i]>getHeight()+othercar.getHeight())
        {
            othercar_y[i]=0;
            othercar.setRefPixelPosition(othercar_x[i], othercar_y[i]);
            othercar_present[i]=false;
        }
        if(policecar_y[i]>getHeight()+policecar.getHeight())
        {
            policecar_y[i]=0;
            policecar.setRefPixelPosition(policecar_x[i], policecar_y[i]);
            policecar_present[i]=false;
        }
        }

     // car_y--;
    if(track_y>getHeight())
        track_y=0;
      add = false;
       decrement = false;
   // if(car_y<0)
     //   car_y=getHeight()-car.getHeight();
if(!tank_collide[present_track-1])
{
if(car.collidesWith(othercar, true))
{
    add = true;
    tank_collide[present_track-1]=true;
    /*othercar_y[present_track-1]=0-othercar.getHeight();
    othercar_x[present_track-1]=0;
    othercar_present[present_track-1]=false;*/
}
}
if(!barricade_collide[present_track-1])
{
        if(car.collidesWith(oldlady, true))
        {
            decrement = true;
            barricade_collide[present_track-1]=true;
            /*
            oldlady_y[present_track-1]=0-oldlady.getHeight();
            oldlady_x[present_track-1]=0;
            oldlady_present[present_track-1]=false;*/
        }
}
if(!police_collide[present_track-1])
{
       if (car.collidesWith(policecar, true))
       {
           pcdecrement=true;
          police_collide[present_track-1]=true;
           /*policecar_y[present_track-1]=0-policecar.getHeight();
           policecar_x[present_track-1]=0;
           policecar_present[present_track-1]=false;*/
       }
}

        //if(car.collidesWith(car, keyPressed))

        boolean generate=true;
        for(int j=0; j<3;j++)
        {
            if (oldlady_present[j]==true || othercar_present[j]==true || policecar_present[j] == true)
            {
                generate=false;
            }
        }
        if(generate==true)
        {

          System.out.println("oldlady");
          //oldlady.setVisible(true);
          oldlady_x[present_track-1]=present_x-30;
          oldlady.setRefPixelPosition(present_x-30, 0);
          oldlady_present[present_track-1]=true;

        }
    }
    int ctr = 0;
    boolean running=true;
    MIDlet midlet;
    public void setMid(MIDlet mid)
    {
        midlet = mid;
    }
    public void reset()
    {
        running = true;
        /*int k = 0;
        while(k < 3)
        {
            oldlady_x[k] =  0;
            oldlady_y[k] = 0;
            policecar_x[k] = 0;
            policecar_y[k] =  0;
            othercar_x[k] = 0;
            othercar_y[k] = 0;
            oldlady_present[k] = false;
            othercar_present[k] = false;
            policecar_present[k] = false;
        }*/

    }
    public void run() {
       /* player.setLoopCount(-1);
        try {
            player.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        reset();
        buildGraphics(getGraphics());
        time_started = System.currentTimeMillis();
        long time_elapsed = System.currentTimeMillis();

        //System.out.println(System.currentTimeMillis());
        while(running)
        {
           //// System.out.println(System.currentTimeMillis());
           // buildGraphics(getGraphics());
            time_elapsed = System.currentTimeMillis() - time_started;
            time = time_elapsed/1000;
            //checkInput();
            slide();
             if(add == true)
      {
          score1+=30;
      }
      if(decrement == true)
      {
          score1 = score1 - 20;
          ctr++;
      }
             if(pcdecrement == true)
      {
          score1-=10;
          ctr++;
      }
            add=false;
            decrement = false;
            pcdecrement=false;
            buildGraphics(getGraphics());
            flushGraphics();
            //slide();
            //System.out.println(track_y);
            /*try {
                Thread.sleep(30);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }*/
            if(score1<0)
            {
                running=false;
                reason = "OOPSS!! Your score is too low";

            }
            if(time >= 90)
            {
                running = false;
                reason = "You ran out of time!!! Better luck next time";
            }
            Runtime.getRuntime().gc();
        }
        /* try {
                player.stop();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }*/
        Midlet.reason = reason;
         Midlet.GameEnd();
               Midlet.display.setCurrent(Midlet.end);
               Midlet.end.start();

            }
    int present_track=1,present_x=track1x;
    boolean keyPressed=false;
   //public void checkInput()
    protected void keyPressed(int keyCode)
    {
        //System.out.println("Banthu");
       /*int gameAction=getKeyStates();

       switch(gameAction)
       {
           case LEFT_PRESSED:
           {
               System.out.println("Key Pressed");
          if(car_x>getWidth()/5)
            car_x-=getWidth()/5;
            break;
           }
           case RIGHT_PRESSED:
           {
            System.out.println("Key Pressed");
     if(car_x<3*getWidth()/5)
            car_x+=getWidth()/5;
            break;
           }
           default:
           {

           }



       }*/
        
        int keyState = getGameAction(keyCode);
        System.out.println(getKeyName(keyCode));
        System.out.println(keyCode);
        System.out.println(LEFT);
        
      if (keyCode==KEY_NUM4||keyState==LEFT)
      {
          System.out.println("Key Pressed");
          if(present_track==1 )
          {
               reason= "You fell off the flyover!! :(";
              running=false;
             
          }
        else
          {
          if(present_track!=1)
          {
            present_track--;
            keyPressed=true;
          }
          }

      }
      else if ((keyCode==KEY_NUM6)||(keyState==RIGHT))
      {
          System.out.println("Key Pressed");
          if(present_track==3)
          {
              reason= "You fell off the flyover!! :(";
              running=false;
          }
        else
          {
            if(present_track!=3)
        {
            present_track++;
            keyPressed=true;
        }
          }
      }
        else if ((keyState==UP)||(keyCode==KEY_NUM2) )
      {
          speed++;
      }
      else if ((keyState==DOWN)||(keyCode==KEY_NUM8))
      {
          speed--;
      }
       if(present_track==1)
      {
            present_x=car_x=track1x;

      }
        else if(present_track==2)
        {
            present_x=car_x=track2x;
        }
        else if(present_track==3)
        {
           present_x=car_x=track3x;
        }
       if(keyPressed)
       {
      Random generator=new Random();
      int num=generator.nextInt(25);
      if(score1 <= 50)
      {
      oldlady_ctr = 3;
           }
      if(score1>=50)
      {
         oldlady_ctr = 4;
      }
      else if(score1>50 && score1<=100)
      {
          oldlady_ctr = 5;
      }
      else if(score1 > 100)
      {
          oldlady_ctr = 6;
      }
      if(num>=0&&num<=oldlady_ctr)
      {
          System.out.println("oldlady");
          //oldlady.setVisible(true);
          oldlady_x[present_track-1]=present_x-30;
          oldlady.setRefPixelPosition(present_x-30, 0);
          oldlady_present[present_track-1]=true;
      }
      else if(num>oldlady_ctr && num<=15)
      {
          System.out.println("police car");
          //othercar.setVisible(true);
          policecar_x[present_track-1]=present_x-30;
         policecar.setRefPixelPosition(present_x-30, 0);
          policecar_present[present_track-1]=true;
      }
      else if(num>15)
      {
          System.out.println("other car");
          //othercar.setVisible(true);
          othercar_x[present_track-1]=present_x;
         othercar.setRefPixelPosition(present_x, 0);
          othercar_present[present_track-1]=true;
      }
       }
       keyPressed=false;

    }
}

