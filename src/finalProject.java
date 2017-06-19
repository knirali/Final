
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author niralikantaria
 */
public class finalProject extends JComponent {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    //Title of the window
    String title = "SPACE";

    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;

    // YOUR GAME VARIABLES WOULD GO HERE
    //player variable created 
    Rectangle player = new Rectangle(10, 10, 85, 80);

    //player movenments 
    boolean downPressed;
    boolean upPressed;

    //balls speed 
    int ballzSpeed = 1;

    //gameover boolean created 
    boolean gameOver = false;

    //13 balls in the y axis 
    int[] ballsY = new int[13];

    //13 balls in the x axis 
    int[] ballsX = new int[13];

    // points start at 0 
    int points = 0;

    //levels start at 0 
    int levels = 0;

    //font created 
    Font myFont = new Font("Arial", Font.BOLD, 75);
    Font font1 = new Font("Arial", Font.BOLD, 40);
    Font font2 = new Font("Arial", Font.BOLD, 60);
    Font font3 = new Font("Arial", Font.BOLD, 150);

    //Buffered Image for the imported images 
    BufferedImage Gamebackground;
    BufferedImage Ship;
    BufferedImage Moon;
    BufferedImage gameover;
    BufferedImage name;

    // GAME VARIABLES END HERE   
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public finalProject() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();

        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE
        //background image drawed 
        g.drawImage(Gamebackground, 0, 0, WIDTH, HEIGHT, null);

        //draw the player 
        g.drawImage(Ship, player.x, player.y, player.width, player.height, null);

        //draw the balls 
        for (int i = 0; i < ballsY.length; i++) {
            g.drawImage(Moon, ballsX[i], ballsY[i], 70, 70, null);
        }

        //the display of points and level  
        g.setFont(font1);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Level:" + levels, 645, 35);
        g.setFont(myFont);
        g.drawString("" + points, 710, 100);

        //if loop created to end the game if collision occurs 
        if (gameOver) {
            //screen cleared 
            g.clearRect(0, 0, WIDTH, HEIGHT);
            //background shows again at the end 
            g.drawImage(Gamebackground, 0, 0, WIDTH, HEIGHT, null);
            //game over image drawn 
            g.drawImage(gameover, WIDTH / 2 - 319, 380, 600, 175, null);
            //final points displayed  
            g.setFont(font3);
            g.setColor(Color.WHITE);
            g.drawString("" + points, WIDTH / 2 - 85, 330);
            //final level displayed 
            g.setFont(font2);
            g.drawString("Level: " + levels, WIDTH / 2 - 140, 390);
            g.drawImage(name, WIDTH / 2 - 330, 50, 600, 150, null);

        }
        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void preSetup() {
        // Any of your pre setup before the loop starts should go here
        //images loaded in from pictures 
        Gamebackground = loadImage("pictures/space.png");
        Ship = loadImage("pictures/ufo.png");
        Moon = loadImage("pictures/moon1.png");
        gameover = loadImage("pictures/gameover.png");
        name = loadImage("pictures/name.png");

        //for loop created to go make a set of balls that are located outside of the screen 
        for (int i = 0; i < ballsX.length; i++) {
            ballsY[i] = yPosition();
            ballsX[i] = 780 + (55 * i);
        }
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        preSetup();

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            //if up key is pressed then player moved up 
            if (upPressed) {
                player.y = player.y - ballzSpeed - 1;
            }
            //if down key is pressed player moves down 
            if (downPressed) {
                player.y = player.y + ballzSpeed + 1;
            }
            //stop player from exceeding top boundary 
            if (player.y <= 20) {
                player.y = 20;
            }
            //stop player form exceeding bottom boundary 
            if (player.y >= 530) {
                player.y = 530;
            }
            //for everytime the speed goes up the balls speed goes up with it 
            for (int i = 0; i < ballsX.length; i++) {
                ballsX[i] -= ballzSpeed;
            }
            //when the ball reaches 0, the next set of balls come 
            if (ballsX[0] < 0 - 50) {
                newBallz();
            }
            //if collisions occurs, a game over sign pops up 
            if (checkCollisions()) {
                //ig game over is true the screen appears 
                gameOver = true;
                repaint();
                try {
                    //gameover screen is on for 3sec and then game is exited 
                    Thread.sleep(30000);
                    System.exit(0);
                    //if an error occured in the game then the game is exited 
                } catch (InterruptedException ex) {
                    Logger.getLogger(finalProject.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(0);
                }
            }
            // GAME LOGIC ENDS HERE 
            // update the drawing (calls paintComponent)
            repaint();

            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(1);
                } else {
                    // sleep to make up the extra time
                    Thread.sleep(desiredTime - deltaTime);
                }
            } catch (Exception e) {
            };
        }
    }

    //new method created to enter in a new set of balls 
    private void newBallz() {
        //balls positions
        ballsY[0] = 0;
        ballsX[0] = 0;
        for (int i = 1; i < ballsY.length; i++) {
            ballsY[i - 1] = ballsY[i];
            ballsX[i - 1] = ballsX[i];
        }
        ballsY[12] = yPosition();
        ballsX[12] = 780;
        //everytime the ball passes the last position the points go up by 1 
        points++;
        //every 10 points the speed increases by 1 
        ballzSpeed = (int) (1 + Math.floor(points / 10));
        levels = (int) (1 + Math.floor(points / 10));
    }

    //method created to place the balls in y position 
    public static int yPosition() {
        //balls in the y-axis are randomly generated down on the screen 
        int yPos;
        Random rand = new Random();
        //placing a ball randomly on the whole screen 
        switch (rand.nextInt(11) + 1) {
            case 1:
                yPos = 30;
                break;
            case 2:
                yPos = 85;
                break;
            case 3:
                yPos = 140;
                break;
            case 4:
                yPos = 195;
                break;
            case 5:
                yPos = 250;
                break;
            case 6:
                yPos = 305;
                break;
            case 7:
                yPos = 360;
                break;
            case 8:
                yPos = 415;
                break;
            case 9:
                yPos = 470;
                break;
            case 10:
                yPos = 525;
                break;
            case 11:
                yPos = 580;
                break;
            default:
                yPos = 30;
                break;

        }
        //the y position is returned 
        return yPos;
    }

    //boolean method created to check for collisions 
    private boolean checkCollisions() {
        //boolean is set as false 
        boolean collide = false;
        //for loop was created 
        for (int i = 0; i < 2; i++) {
            //if loop created to determine is the ball is in the area of player 
            if (ballsX[i] <= player.x + player.width - 1 && ballsX[i] > player.x) {
                //if the ball is in the same area (share the same pixals) collision is ture
                for (int k = ballsY[i]; k < ballsY[i] + 50; k++) {
                    if (k >= player.y && k <= player.y + player.height - 20) {
                        collide = true;
                    }
                }
            }

        }
        //collision message is returned 
        return collide;
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {

        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {

        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {
            //if the down kwy is pressed then the player moves down 
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            //if the up key is pressed then the player moves up 
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            }

        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            //if the down pressed variable is false then the player doesnt move 
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            //if the upPressed variable is false then the player doesnt move
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        finalProject game = new finalProject();

        // starts the game loop
        game.run();
    }

    //mehtod created to import images from a folder called pcitures 
    public BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        try {
            // use ImageIO to load in an Image
            // ClassLoader is used to go into a folder in the directory and grab the file
            img = ImageIO.read(ClassLoader.getSystemResourceAsStream(filename));
        } catch (IOException ex) {
            System.out.println("Error Loading Image");
            throw new RuntimeException(ex);
        }
        //image is returned 
        return img;
    }
}
