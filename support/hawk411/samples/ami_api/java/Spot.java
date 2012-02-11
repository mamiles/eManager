//******************************************************************************
//*
//* Module:  Spot.java
//*
//* Purpose: A simple application used to illustrate how to use the TIBCO Hawk
//*          Application Management Interface (AMI) API for Java.
//*
//*           Copyright 2000 TIBCO Software Inc. All rights reserved.
//******************************************************************************
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import com.tibco.tibrv.*;
import COM.TIBCO.hawk.ami.*;

//******************************************************************************
//* Class: Spot
//*
/** Implements sample AMI application using TIBCO Hawk Application Management
 *  Interface (AMI) API for Java.
 ******************************************************************************/
 public class Spot extends Frame implements Runnable, ActionListener
 {
   //***************************************************************************
   //                              Constants
   //***************************************************************************

   /** Rendezvous queue.*/
   private TibrvQueue mRvQueue = null;
   /** Instance of Spot AMI interface class.*/
   private SpotAmi    mSpotAmi = null;
   /** Position and radius of the circle.*/
   private int        x = 150, y = 100, r=25;
   /** Trajectory of circle.*/
   private int        dx = 2, dy = 2;
   /** The size of the bouncing area.*/
   private Dimension  size;
   /** The off-screen image for double-buffering.*/
   private Image      buffer;
   /** A Graphics object for the buffer.*/
   private Graphics   bufferGraphics;
   /** Thread that performs the animation.*/
   private Thread     animator;
   /** The current color of the ball.*/
   private Color      ballColor;
   /** The name of the ball color.*/
   private String     ballColorName;
   /** A table holding the number of times each color was chosen.*/
   private Hashtable  colorAssignmentCount;
   /** The angle of the ball's trajectory.*/
   private int        angle;
   /** Speed multiplier for ball speed.*/
   private int        warpFactor   =  2;
   /** Lower offset for ball position.*/
   private int        LOWER_OFFSET =  0;
   /** Upper offset for ball position.*/
   private int        UPPER_OFFSET = 40;
   /** Spot application Frame menus.*/
   private Menu       fileMenu, angleMenu, colorMenu, actionMenu;
   /** Current sleep time for animation frame display loop..*/
   private int        sleepTime    =   100;
   /** Animation suspended flag.*/
   private boolean    suspended =  false;

   /** Maximum sleep time for animation frame display loop.*/
   private final static int MaxSleepTime = 10000;
   /** Minimum sleep time for animation frame display loop..*/
   private final static int MinSleepTime =     0;
   /** Ball angles.*/
   private final static int[]    angles  = {   0, 30, 45, 60, 90,120,135,150,
                                             180,210,225,240,270,300,310,330 };
   /** Ball colors.*/
   private final static String[] colors  = { "black", "blue", "cyan", "darkGray",
                                             "gray",  "green", "lightGray",
                                             "magenta", "orange", "pink", "red",
                                             "yellow" };
   /** Action menu strings.*/
   private final static String[] actions = { "suspend", "resume", "accelerate",
                                             "decelerate" };

   //***************************************************************************
   //                          Accessor Methods
   //***************************************************************************

   /** Returns name of current ball color.*/
   public String    getColorByName()          { return ballColorName; }
   /** Returns table of times each color has been chosen.*/
   public Hashtable getAssignmentCount()      { return colorAssignmentCount; }
   /** Returns current angle of ball.*/
   public int       getAngle()                { return angle; }

   //***************************************************************************
   //                           Private Methods
   //***************************************************************************

   /** Sets the current ball color.*/
   private void     setColor(Color c)         { ballColor = c; }
   /** Sets the current x/y deltas for changing the ball positioin.*/
   private void     setDeltas( int x, int y ) { dx = warpFactor*x;
                                                dy = warpFactor*y; }

   //***************************************************************************
   //* Method: Spot.main
   //*
   /** Allows Spot to be run as a command line application.
    *
    *  @param args Command line arguments array.
    ***************************************************************************/
    public static void main(
      String[] args )
    {
      String[]  rvdSessionArgs = {"7474", null, "tcp:7474"};
      boolean   rvdsession     = false;
      Spot      theSpot        = null;

      try
      {
        // Process command line arguments
        for (int i=0; i<args.length; i++)
        {
          if (args[i].equals("-rvd_session"))
          {
            if ( i > (args.length - 4))
            {
              System.err.println( "From Spot: Insufficient -rvd_session " +
                                  "parameters specified." );
              Spot.usage();
            }

            if(rvdsession)
            {
              System.err.println( "From Spot: Only one -rvd_session may be " +
                                  "specified." );
              Spot.usage();
            }

            rvdSessionArgs[0] = args[++i];
            rvdSessionArgs[1] = args[++i];
            rvdSessionArgs[2] = args[++i];
            rvdsession = true;
          }
          else
          {
            System.err.println("From Spot: Invalid command line option " +
                               "specified, " + args[i] + "." );
            Spot.usage();
          }
        }

        // Pass null in place of null strings or empty quotes.
        for(int i=0; i< rvdSessionArgs.length; i++)
         if ( rvdSessionArgs[i] != null
              && (   rvdSessionArgs[i].equals("")
                  || rvdSessionArgs[i].equals("\"\"") ) )
           rvdSessionArgs[i]=null;


        // Open Tibrv
        Tibrv.open();

        // Create an instance of Spot.
        theSpot = new Spot( rvdSessionArgs[0], rvdSessionArgs[1],
                            rvdSessionArgs[2] );

        // Run Spot.
        theSpot.execute();

        // Close Tibrv
        Tibrv.close();

      }
      catch ( java.lang.Throwable caughtThrowable )
      {
        caughtThrowable.printStackTrace();
      }

      System.exit(0);
    }
   //***************************************************************************
   //* Method: Spot.<init>
   //*
   /** Constructs an instance of the Spot class for illustrating the use of the
    *  TIBCO Hawk Application Management Interface (AMI) API for Java.
    *
    *  @param RV transport for Spot AMI connection to Hawk.
    *  @param RV queue for Spot AMI connection to Hawk.
    ***************************************************************************/
    public Spot(
      String     rvService,
      String     rvNetwork,
      String     rvDaemon )
      throws java.lang.Exception
    {
      // Initialize Frame superclass.
      super("Spot");

      // Create a Rendezvous Rendezvous queue to be used for AMI.
      mRvQueue = new TibrvQueue();

      // Initialize Spot AMI interface class.
      mSpotAmi = new SpotAmi( this, rvService, rvNetwork, rvDaemon, mRvQueue);

      addComponentListener(new ComponentListenerHandler());
      addWindowListener(new WindowListenerHandler());

      // Initialize menus for Spot application Frame.
      MenuBar mb = new MenuBar();
      fileMenu = new Menu("File");
      MenuItem exitMenu = new MenuItem("Exit");
      exitMenu.addActionListener(this);
      fileMenu.add(exitMenu);
      mb.add(fileMenu);

      angleMenu = new Menu("Angle");
      AngleMenuItem angleMenuItem;
      for (int i = 0; i < angles.length; i++) {
        angleMenuItem = new AngleMenuItem(String.valueOf(angles[i]));
        angleMenuItem.addActionListener(this);
        angleMenu.add(angleMenuItem);
      }
      mb.add(angleMenu);

      colorMenu = new Menu("Color");
      ColorMenuItem colorMenuItem;
      for (int i = 0; i < colors.length; i++) {
        colorMenuItem = new ColorMenuItem(colors[i]);
        colorMenuItem.addActionListener(this);
        colorMenu.add(colorMenuItem);
      }
      mb.add(colorMenu);

      actionMenu = new Menu("Action");
      ActionMenuItem actionMenuItem;
      for (int i = 0; i < actions.length; i++) {
        actionMenuItem = new ActionMenuItem(actions[i]);
        actionMenuItem.addActionListener(this);
        actionMenu.add(actionMenuItem);
      }
      mb.add(actionMenu);

      setMenuBar(mb);

      // Complete Frame initialization.
      setSize(300, 250);
      setResizable(true);
      size = this.getSize();
      resetColorCount();
      setColorByName("blue");
      setAngle(30);
      setLocation(50,50);
    }

   //***************************************************************************
   //* Method: Spot.execute
   //*
   /** Activates Spot GUI and then dispatches RV messages.
    ***************************************************************************/
    public void execute()
      throws java.lang.Exception
    {
      start();
      setVisible(true);

      // Dispatch Tibrv events. Destroy RV queue to terminate this loop.
      while( true )
      {
        try { mRvQueue.dispatch(); }
        catch( java.lang.Throwable caughtThrowable ) { break; }
      }

      mSpotAmi.terminate(); // Shutdown AMI application.
      setVisible(false);    // Hide the frame.
      dispose();            // Free windowing system resources.
    }

   //***************************************************************************
   //* Method: Spot.paint
   //*
   /** Draw animated spot using double-buffering technique.
    *
    *  @param inGraphics Associated graphics context.
    ***************************************************************************/
    public void paint(
      Graphics g)
    {
      // Set up an off-screen Image for double-buffering.
      if ( buffer == null )
      {
        buffer = this.createImage( size.width, size.height + LOWER_OFFSET );
        bufferGraphics = buffer.getGraphics();
      }

      // Draw onto the off-screen Image.
      bufferGraphics.setColor  ( this.getBackground() );
      bufferGraphics.fillRect  ( 0, 0, size.width, size.height + LOWER_OFFSET );
      bufferGraphics.setColor  ( ballColor );
      bufferGraphics.fillOval  ( x-r, y-r, r*2, r*2 );
      bufferGraphics.setColor  ( Color.black );
      bufferGraphics.drawString( " angle: "  + getAngle() +
                                 "  color: " + getColorByName(),
                                 5, size.height-5 );

      // Copy the off-screen Image onto the screen
      g.drawImage( buffer, 0, 0, this );
    }
   //***************************************************************************
   //* Method: Spot.update
   //*
   /** This method overrides default processing to eliminate clearing of Frame
    *  client area which causes the animation to flash. We do not need to clear
    *  when double-buffering.
    *
    *  @param inGraphics Associated graphics context.
    ***************************************************************************/
    public void update(Graphics g) { paint(g); }

   //***************************************************************************
   //* Method: Spot.run
   //*
   /** Animate the Spot bouncing ball.
    ***************************************************************************/
    public void run()
    {
      while(true)
      {
        if ( !suspended )
        {
          // Bounce the circle if we've hit an edge.
          if ( (x - r + dx <      0    ) ||
               (x + r + dx > size.width)  )
             dx = -dx;

          if ( (y - r - UPPER_OFFSET + dy <      0     ) ||
               (y + r - LOWER_OFFSET + dy > size.height)  )
             dy = -dy;

          // Move the circle.
          x += dx;  y += dy;

          // Repaint old position of circle.
          repaint();
        }

        // Pause before drawing the circle again.
        try { Thread.sleep(sleepTime); } catch (InterruptedException e) { ; }
      }
    }
   //***************************************************************************
   //* Method: Spot.start
   //*
   /** Start the animation thread.
    ***************************************************************************/
    public void start()
    {
      if (animator == null)
      {
        animator = new Thread(this);
        animator.start();
      }
    }

   //***************************************************************************
   //* Method: Spot.selectedExit
   //*
   /** Process exit action.
    ***************************************************************************/
    public void selectedExit()
    {
      try
      { // Shutdown message dispatcher.
        mRvQueue.destroy();
      }
      catch ( Exception caughtException )
      { caughtException.printStackTrace();
      }
    }
   //***************************************************************************
   //* Method: Spot.setAngle
   //*
   /** Set the current ball angle.
    *
    *  @param inAngle Ball trajectory angle to set.
    ***************************************************************************/
    public void setAngle (
      int inAngle )
      throws AmiException
    {
      // Convert negative angles.
      if (inAngle < 0) inAngle += 360;

      // Normallize angles greater than 360.
      inAngle = inAngle % 360;
      if (this.angle != inAngle)
      {
        this.angle = inAngle;

        // Convert angle to appropriate deltas. Round down to 30/45 multiples.
        if      (inAngle <  30) { this.angle =   0; setDeltas( 0,-2); }
        else if (inAngle <  45) { this.angle =  30; setDeltas( 1,-2); }
        else if (inAngle <  60) { this.angle =  45; setDeltas( 1,-1); }
        else if (inAngle <  90) { this.angle =  60; setDeltas( 2,-1); }
        else if (inAngle < 120) { this.angle =  90; setDeltas( 2, 0); }
        else if (inAngle < 135) { this.angle = 120; setDeltas( 2, 1); }
        else if (inAngle < 150) { this.angle = 135; setDeltas( 1, 1); }
        else if (inAngle < 180) { this.angle = 150; setDeltas( 1, 2); }
        else if (inAngle < 210) { this.angle = 180; setDeltas( 0, 2); }
        else if (inAngle < 225) { this.angle = 210; setDeltas(-1, 2); }
        else if (inAngle < 240) { this.angle = 225; setDeltas(-1, 1); }
        else if (inAngle < 270) { this.angle = 240; setDeltas(-2, 1); }
        else if (inAngle < 300) { this.angle = 270; setDeltas(-2, 0); }
        else if (inAngle < 310) { this.angle = 300; setDeltas(-2,-1); }
        else if (inAngle < 330) { this.angle = 310; setDeltas(-1,-1); }
        else if (inAngle < 360) { this.angle = 330; setDeltas(-1,-2); }

        if ( mSpotAmi != null ) mSpotAmi.notifyAngleChange();
      }
    }
   //***************************************************************************
   //* Method: Spot.setColorByName
   //*
   /** Set the current ball color.
    *
    *  @param inColor The name of the color to be set.
    ***************************************************************************/
    public void setColorByName(
      String  inColor )
      throws AmiException
    {
      // Set specified color.
      if      (inColor.equalsIgnoreCase("black"    )) setColor(Color.black    );
      else if (inColor.equalsIgnoreCase("blue"     )) setColor(Color.blue     );
      else if (inColor.equalsIgnoreCase("cyan"     )) setColor(Color.cyan     );
      else if (inColor.equalsIgnoreCase("darkGray" )) setColor(Color.darkGray );
      else if (inColor.equalsIgnoreCase("gray"     )) setColor(Color.gray     );
      else if (inColor.equalsIgnoreCase("green"    )) setColor(Color.green    );
      else if (inColor.equalsIgnoreCase("lightGray")) setColor(Color.lightGray);
      else if (inColor.equalsIgnoreCase("magenta"  )) setColor(Color.magenta  );
      else if (inColor.equalsIgnoreCase("orange"   )) setColor(Color.orange   );
      else if (inColor.equalsIgnoreCase("pink"     )) setColor(Color.pink     );
      else if (inColor.equalsIgnoreCase("red"      )) setColor(Color.red      );
      else if (inColor.equalsIgnoreCase("yellow"   )) setColor(Color.yellow   );
      else return;

      if (!inColor.equalsIgnoreCase(ballColorName))
      {
          ballColorName = inColor;
          if ( mSpotAmi != null ) mSpotAmi.notifyColorChange();
      }

      // Update count of times this color was chosen.
      Integer cc = (Integer)(colorAssignmentCount.get(ballColorName));
      if (cc != null)
      {
        int count = cc.intValue();
        count++;
        colorAssignmentCount.put(ballColorName, new Integer(count));
      }
    }
   //***************************************************************************
   //* Method: Spot.resetColorCount
   //*
   /** Intialize table for tracking count of times each color was chosen.
    ***************************************************************************/
    public void resetColorCount()
    {
      colorAssignmentCount = new Hashtable();
      for (int i=0; i < colors.length; i++)
      {
        colorAssignmentCount.put(colors[i], new Integer(0));
      }
    }
   //***************************************************************************
   //* Method: Spot.usage
   //*
   /** Allows Spot to be run as a command line application.
    *
    *  @param args Command line arguments array.
    ***************************************************************************/
    private static void usage()
    {
      System.err.println("Usage: Spot " +
                         "[-rvd_session <service> <network> <daemon>]");
    }
   //***************************************************************************
   //* Method: Spot.actionPerformed
   //*
   /** Provides menu actions handler
    *
    *  @param ActionEvent
    ***************************************************************************/
    public void actionPerformed(ActionEvent e)
    {
      Object source = e.getSource();
      String label = e.getActionCommand();

      if (source instanceof AngleMenuItem)
      {
        try
        {
          int selected_angle = 0;

          try { selected_angle = Integer.parseInt(label); }
          catch (NumberFormatException exc)
          {
            selected_angle = 0;
          }

          setAngle(selected_angle);
        }
        catch ( Exception caughtException )
        { caughtException.printStackTrace();
        }
      }
      else if (source instanceof ColorMenuItem)
      {
        try
        {
          setColorByName(label);
        }
        catch ( Exception caughtException )
        { caughtException.printStackTrace();
        }
      }
      else if (source instanceof ActionMenuItem)
      {
        if (label.equalsIgnoreCase("suspend"))
        {
          try
          { suspendAnimation();
          }
          catch ( Exception caughtException )
          { caughtException.printStackTrace();
          }
        }
        else if (label.equalsIgnoreCase("resume"))
        {
          try
          { resumeAnimation();
          }
          catch ( Exception caughtException )
          { caughtException.printStackTrace();
          }
        }
        else if (label.equalsIgnoreCase("accelerate"))
        { accelerate();
        }
        else if (label.equalsIgnoreCase("decelerate"))
        { decelerate();
        }
      }
      else if (source instanceof MenuItem)
      {
        if (label.equalsIgnoreCase("Exit"))
        {
          selectedExit();
        }
      }
    }

   //***************************************************************************
   //* Method: Spot.accelerate
   //*
   /** Increases the animation frame rate (speeding up the bouncing ball).
    ***************************************************************************/
    public void accelerate()
    {
      int theSleepTime = sleepTime;

      if ( theSleepTime > 50 ) theSleepTime -= 50;
      else                     theSleepTime -= 5;

      if ( theSleepTime < MinSleepTime ) theSleepTime = MinSleepTime;

      sleepTime = theSleepTime;
    }
   //***************************************************************************
   //* Method: Spot.decelerate
   //*
   /** Increases the animation frame rate (speeding up the bouncing ball).
    ***************************************************************************/
    public void decelerate()
    {
      int theSleepTime = sleepTime;

      if ( theSleepTime < 50 ) theSleepTime += 5;
      else                     theSleepTime += 50;

      if ( theSleepTime > MaxSleepTime ) theSleepTime = MaxSleepTime;

      sleepTime = theSleepTime;
    }

   //***************************************************************************
   //* Method: Spot.suspendAnimation
   //*
   /** Suspend animation of bouncing ball.
    ***************************************************************************/
    public void suspendAnimation()
      throws java.lang.Exception
    {
      suspended = true;

      if ( mSpotAmi != null )
        mSpotAmi.sendUnsolicitedMsg( AmiConstants.ALERT_TYPE_INFO,
                   "The movement of the ball is suspended", new Integer(1) );
    }
   //***************************************************************************
   //* Method: Spot.resumeAnimation
   //*
   /** Resume animation of bouncing ball.
    ***************************************************************************/
    public void resumeAnimation()
      throws java.lang.Exception
    {
      suspended = false;

      if ( mSpotAmi != null )
        mSpotAmi.sendUnsolicitedMsg( AmiConstants.ALERT_TYPE_INFO,
                     "The movement of the ball is resumed", new Integer(2) );
    }
   //***************************************************************************
   //* Inner Class: AngleMenuItem
   //*
   /** Provides unique class for Spot angle menu.
    ***************************************************************************/
    class AngleMenuItem extends MenuItem
    {
      AngleMenuItem(String s)
      {
        super(s);
      }
    }
   //***************************************************************************
   //* Inner Class: ColorMenuItem
   //*
   /** Provides unique class for Spot color menu.
    ***************************************************************************/
    class ColorMenuItem extends MenuItem
    {
      ColorMenuItem(String s)
      {
        super(s);
      }
    }

   //***************************************************************************
   //* Inner Class: ActionMenuItem
   //*
   /** Provides unique class for Spot action menu.
    ***************************************************************************/
    class ActionMenuItem extends MenuItem
    {
      ActionMenuItem(String s)
      {
        super(s);
      }
    }

   //***************************************************************************
   //* Inner Class: ComponentListenerHandler
   //*
   /** Provides actions handler
    ***************************************************************************/
    class ComponentListenerHandler extends ComponentAdapter
    {
      public void componentResized(ComponentEvent e)
      {
        buffer = null;
        size = getSize();

        if ((x-r) > size.width)
          x = size.width - r;

        if ((y-r) > size.height && (size.height-UPPER_OFFSET) > 2*r)
          y = size.height - r;
      }
    }

   //***************************************************************************
   //* Inner Class: WindowListenerHandler
   //*
   /** Provides handler for closing the window
    ***************************************************************************/
    class WindowListenerHandler extends WindowAdapter
    {
      public void windowClosing(WindowEvent e)
      {
        selectedExit();
      }
    }
 }
