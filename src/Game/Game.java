/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Game.Display.Display;
import Game.Display.Sprite;
import Game.Display.SpriteSheet;
import Game.Entity.Player;
import Game.GUI.GUI;
import Game.Input.Keyboard;
import Game.Input.Mouse;
import Game.Level.Level;
import Items.Clothing.Backpack;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author vv
 */
public class Game extends Canvas implements Runnable
{

    /**
     * @param args the command line arguments
    */
    private final static String TITLE = "Runaway";
    public final static int WIDTH = 700;
    public final static int HEIGHT = 700;
    private final static int FRAMECAP = 60;
    
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    private boolean running = false, show = false;
    
    private JFrame frame;
    private Thread thread;
    public static Level mainLevel;
    private Graphics g;
    //private Cutscene cutscene;
    public static Keyboard keyboard;
    public static Mouse mouse;
    public static GUI gui;
    public static Display display;
    
    public static long updates = 0;
    //levels
    public static Level level1;
    public static Player player;
    
    public static void main(String[] args)
    {
        // TODO code application logic here
        Game game = new Game();
        
        try
        {
            game.frame.setIconImage(ImageIO.read(new File("src/Resources/Icons/icon.png")));
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
        game.frame.setResizable(false);
        game.frame.add(game);
        game.frame.setTitle(TITLE);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        
        game.start();
    }
    
    /**
     * Game constructor.
     */
    public Game()
    {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame = new JFrame();
        display = new Display(WIDTH, HEIGHT);
        thread = new Thread(this, "Display");
        keyboard = new Keyboard();
        mouse = new Mouse();
        addKeyListener(keyboard);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        player = new Player(0, 0);
        level1 = new Level(0, 0, SpriteSheet.shtMapInfo);
        mainLevel = level1;
        level1.load();
        gui = new GUI();
        level1.add(new Backpack(160, 160, Sprite.sprBackpackFront, Sprite.sprBackpackBack, Sprite.sprBackpackLeft, Sprite.sprBackpackRight));
        //cutscene = new Cutscene("src/Resources/Video/video.mp4", this);
    }
    
    /**
     * Start function. Called after Game has been instantiated.
     */
    public void start()
    {
        if(running == true)return;
        running = true;
        thread.start();
    }
    
    /**
     * Stop function. This will be used to close the game.
     */
    public void stop()
    {
        if(running == false)return;
        running = false;
        try
        {
            thread.join();
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        System.exit(0);
    }
    
    /**
     * Runnable inherited function. Automatically called, it is the main game loop.
     */
    @Override
    public void run()
    {
        player.setPos(0, 0);
        player.centerCamera();
        //
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / FRAMECAP;
        double delta = 0;
        requestFocus();
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1)
            {
                //System.out.println("Updates: " + updates);
                update();
                updates++;
                render();
            }
        }
        stop();
    }
    
    /**
     * Update function. Called each frame. Update object.
     */
    private void update()
    {
        //cutscene.init();
        //update input first
        keyboard.update();
        mouse.update();
        gui.update();
        mainLevel.update();
    }
    
    /**
     * Render function. Called each frame, after update is done. 
     * Draw objects to the screen.
     */
    private void render()
    {
        BufferStrategy bs = getBufferStrategy();
        
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        
        g = bs.getDrawGraphics();
        
        display.clear();
        display.setGraphics(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        //render thing HERE
        mainLevel.render();
        //render GUI over everything
        gui.render();
        
        for(int i = 0; i < pixels.length; i++)
            pixels[i] = display.pixels[i];
        
        g.dispose();
        bs.show();
        
        if(show == false)
        {
            frame.setVisible(true);
            show = true;
        }
    }
}