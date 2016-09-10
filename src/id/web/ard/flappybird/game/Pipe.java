package id.web.ard.flappybird.game;

import id.web.ard.flappybird.FlappyBird;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

public class Pipe extends Thread {
	private int x;
	private int y;
	private int v;
	private int isReset = 0;
	
	private final Image top;
	private final Image bottom;
	
	private int oldx;
	private int oldy;
	
	public Pipe(int x, int y,int v) {
		this.x = x;
		this.y = y;
		this.v = v;
		
		oldx = x;
		oldy = y;
		top = new ImageIcon(FlappyBird.class.getResource("image/Pipe_Top.png")).getImage();
		bottom = new ImageIcon(FlappyBird.class.getResource("image/Pipe_Bottom.png")).getImage();
	}
	
	public void drawSelf(Graphics g) {
		g.drawImage(top, x, y - top.getHeight(null), top.getWidth(null), top.getHeight(null), null);
		g.drawImage(bottom, x, y + GameFrame.w , bottom.getWidth(null), bottom.getHeight(null) , null);
	}
	
	public void move() {
		if (GameFrame.flag == 0) {
			return;
		}
		if (x > -52) {
			x--;
		}
		resetPipe();
	}
	
	public void resetPipe() {
		if (x <= -52) {
			x = 4 * GameFrame.d - 52;
			y = new Random().nextInt(322 - GameFrame.w) + 50;
			isReset = 0;
		}
	}
	
	public boolean isBirdDied(Bird bird) {
		
		int[] xpoints1 = {x, x, x + top.getWidth(null), x + top.getWidth(null)};
		int[] ypoints1 = {y - top.getHeight(null), y, y, y - top.getHeight(null)};
		
		int[] xpoints2 = {x, x, x + bottom.getWidth(null), x + bottom.getWidth(null)};
		int[] ypoints2 = {y + GameFrame.w, y + GameFrame.w + bottom.getHeight(null), y + GameFrame.w + bottom.getHeight(null), y + GameFrame.w };
		
		Polygon p1 = new Polygon(xpoints1, ypoints1, 4);
		Polygon p2 = new Polygon(xpoints2, ypoints2, 4);
		
		Area a1 = new Area(p1);
		Area a2 = new Area(p2);
		
		return a1.intersects(bird.getX(), bird.getY(), 35, 30) || a2.intersects(bird.getX(), bird.getY(), 35, 35) || bird.getY() > 450 || bird.getY() < 0;
		
	}
	
	public boolean isBirdPass(Bird bird) {
		if (bird.getX() > x+52 && isReset == 0) {
			isReset = 1;
			return true;
		} else {
			return false;
		}
	}
	
	public void reStart() {
		x = oldx;
		y = new Random().nextInt(322 - GameFrame.w) + 50;
		isReset = 0;
	}
	
	@Override
	public void run() {
		while (true) {
			move();
			try {
				sleep(v);
			} catch (InterruptedException ex) {
				Logger.getLogger(Pipe.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
