package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable,KeyListener{
	
	private int score;
	private int level;
	private boolean gameover;
	
	public static final int WIDTH=400;
	public static final int HEIGHT=400;
	
	private Graphics2D g2d;
	private BufferedImage image;
	
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	Entity head,apple;
	ArrayList<Entity> snake;
	private final int SIZE=10;
	
	private int dx,dy;
	
	private boolean up,down,left,right,start;
	
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	public void addNotify(){
		super.addNotify();
		thread=new Thread(this);
		thread.start();
	}
	
	private void setFPS(int fps){
		targetTime=1000/fps;
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int k=e.getKeyCode();
		if(k==KeyEvent.VK_UP)
			up=true;
		if(k==KeyEvent.VK_DOWN)
			down=true;
		if(k==KeyEvent.VK_LEFT)
			left=true;
		if(k==KeyEvent.VK_RIGHT)
			right=true;
		if(k==KeyEvent.VK_ENTER)
			start=true;
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int k=e.getKeyCode();
		if(k==KeyEvent.VK_UP)
			up=false;
		if(k==KeyEvent.VK_DOWN)
			down=false;
		if(k==KeyEvent.VK_LEFT)
			left=false;
		if(k==KeyEvent.VK_RIGHT)
			right=false;
		if(k==KeyEvent.VK_ENTER)
			start=false;
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		// TODO Auto-generated method stub
		if(running)
			return ;
		init();
		long startTime;
		long elapsed,wait;
		while(running){
			startTime=System.nanoTime();
			
			update();
			requestRender();
			
			elapsed=System.nanoTime()-startTime;
			wait=targetTime-elapsed/1000000;
			if(wait>0){
				try{
					Thread.sleep(wait);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	private void requestRender() {
		// TODO Auto-generated method stub
		render(g2d);
		Graphics g=getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
	}

	private void update() {
		// TODO Auto-generated method stub
		if(gameover){
			if(start){
				setUpLevel();
			}
			return ;
		}
		if(up && dy==0){
			dy=-SIZE;
			dx=0;
		}
		if(down && dy==0){
			dy=SIZE;
			dx=0;
		}
		if(left && dx==0){
			dy=0;
			dx=-SIZE;
		}
		if(right && dx==0){
			dy=0;
			dx=SIZE;
		}
		if(dx!=0 || dy!=0){
			for(int i=snake.size()-1;i>0;i--){
				snake.get(i).setPosition(snake.get(i-1).getX(),snake.get(i-1).getY());
			}
			head.move(dx, dy);
		}
		for(Entity e:snake){
			if(e.isCollision(head)){
				gameover=true;
				break;
			}
		}
		if(apple.isCollision(head)){
			score++;
			setApple();
			Entity e =new Entity(SIZE);
			e.setPosition(-100,-100);
			snake.add(e);
		}
			
		if(head.getX()<0)
			head.setX(WIDTH);
		if(head.getX()>WIDTH)
			head.setX(0);
		if(head.getY()<0)
			head.setY(HEIGHT);
		if(head.getY()>HEIGHT)
			head.setY(0);
	}

	private void init() {
		// TODO Auto-generated method stub
		image=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
		g2d=image.createGraphics();
		running=true;
		setUpLevel();
		gameover=false;
		setFPS(10);
	}
	
	public void setUpLevel(){
		snake=new ArrayList<Entity>();
		head=new Entity(SIZE);
		head.setPosition(WIDTH/2,HEIGHT/2);
		snake.add(head);
		for(int i=1;i<10;i++){
			Entity e=new Entity(SIZE);
			e.setPosition(head.getX()+(i*SIZE),head.getY());
			snake.add(e);
		}
		apple=new Entity(SIZE);
		setApple();
		score=0;
	}
	
	public void setApple(){
		int x=(int)(Math.random()*(WIDTH-SIZE));
		int y=(int)(Math.random()*(HEIGHT-SIZE));
		apple.setPosition(x,y);
		y=y-(y%SIZE);
		x=x-(x%SIZE);
	}
	
	public void render(Graphics2D g2d){
		g2d.clearRect(0, 0, WIDTH,HEIGHT);
		g2d.setColor(Color.green);
		for(Entity e:snake){
			e.render(g2d);
		}
		g2d.setColor(Color.RED);
		apple.render(g2d);
		
		/*g2d.setColor(Color.WHITE);
		g2d.drawString("Score : ",+score, 10);*/
	}
	
}
