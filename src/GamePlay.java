import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener,ActionListener {

	//KeyListener for arrowkeys & ActionListener for ball movement.
	
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;// for setting the speed of the ball
	private int delay = 8;// current speed
	
	private int playerX = 310;//starting position of paddle
	
	private int ballposX = 120;//starting position of ball
	private int ballposY = 350;//starting position of ball
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public GamePlay() {
		map = new MapGenerator(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay,this);
		timer.start();
		
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.BLACK);
		g.fillRect(1, 1, 692, 592);
		
		//drawing map
		map.draw((Graphics2D)g);
		//borders
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		g.fillRect(0, 0, 3, 592);
		
		//scores
		g.setColor(Color.WHITE);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString(""+score, 590, 30);
		
		//the paddle
		g.setColor(Color.GREEN);
		g.fillRect(playerX, 550, 100, 8);

		//the ball
		g.setColor(Color.YELLOW);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		if(totalBricks <=0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,25));
			g.drawString("CONGRATULATIONS!You WIN!", 230, 300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		if(ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Game Over, Score: "+score, 190, 300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Restart", 230, 350);
			
			
		}
		g.dispose();
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(play) {
			
			if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))) {
				 
				ballYdir = -ballYdir;
			}
			
			
		 A: for(int i=0;i<map.map.length;i++) {
				for(int j=0;j<map.map[i].length;j++) {
					if(map.map[i][j]>0) {
						int brickX = j*map.brickWidth + 80;
						int brickY = i*map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight; 
						
						Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
						Rectangle ballRect = new Rectangle(ballposX,ballposY,20,20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
						    totalBricks--;
						    score+=5;
						    
						   //to avoid problem for left and rigth intersection of ball
						    if(ballposX + 19 <= brickRect.x || ballposX + 1 >=	brickRect.x +brickRect.width) {
						    	
						    	ballXdir = -ballXdir;
						    	
						    }else {
						    	
						    	ballYdir = -ballYdir;
						    }
						    
						    break A;
						}
					}
				}
			}
			
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0) {
				ballXdir = -ballXdir;//for left border
			}
			if(ballposY < 0) {
				ballYdir = -ballYdir;//for top border
			} 
			if(ballposX > 670) {
				ballXdir = -ballXdir;//for right border
			} 
		}
		
		repaint();//necessary to move the paddle or ball.if paint method is not repainted it cant show the change of movement of 
		//the paddle or the ball.
		
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
    
	@Override
	public void keyPressed(KeyEvent e) {
    
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >=600) {
				playerX=600;
			} else {
				
				moveRight();
			}
			
		}
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
        	if(playerX < 10) {
				playerX=10;
			} else {
				
				moveLeft();
			}
		}
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	
        	if(!play) {
        		play = true;
        		ballposX = 120;
        		ballposY = 350;
        		ballXdir = -1;
        		ballYdir = -2;
        		playerX = 310;
        		score = 0;
        		totalBricks = 21; 
        		map = new MapGenerator(3,7);
        		
        		repaint();
        	}
        }
    }
	
	public void moveRight() {
		play = true;
		playerX+=30;
	}
	public void moveLeft() {
		play = true;
		playerX-=30;
	}



}
