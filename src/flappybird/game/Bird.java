package flappybird.game;

import flappybird.FlappyBird;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

public class Bird extends Thread {
	
	private int x;
	private int y;
	private int oldx;
	private int oldy;
	private int ooldx;
	private int ooldy;
	
	private final Image[] bird = {
		new ImageIcon(FlappyBird.class.getResource("image/Birds_01.png")).getImage(),
		new ImageIcon(FlappyBird.class.getResource("image/Birds_02.png")).getImage(),
		new ImageIcon(FlappyBird.class.getResource("image/Birds_03.png")).getImage()
	};
	
	private int imageIndex = 0;
	private int downv;
	private int upv;
	private int upvn = 0;
	private int up;
	
	private static final int DOWN = 0;
	private static final int FLY = 1;
	private int flag = Bird.FLY;
	private final double g = 0.0003;
	
	public Bird(int x, int y, int downv, int upv, int up) {
		this.x = x;
		this.y = y;
		this.oldx = x;
		this.oldy = y;
		this.downv = downv;
		this.upv = upv;
		this.up = up;
		ooldx = x;
		ooldy = y;
		
		this.upvn = upv;
	}
	
	public void setImageIndex() {
		if (imageIndex == 2) {
			imageIndex = 0;
		} else {
			imageIndex++;
		}
	}
	
	public void drawSelf(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		double a = Math.atan((y - ooldy + 0.000001) / 50);
		if (a > Math.atan(2)) {
			a = Math.atan(2);
		} else if (a < Math.atan(-2)) {
			a = Math.atan(-2);
		}
		if (GameFrame.flag == 1) {
			g2.rotate(a, x + 17, y + 17);
		}
		g.drawImage(bird[imageIndex], x, y, bird[imageIndex].getWidth(null), bird[imageIndex].getHeight(null), null);
		
		if (GameFrame.flag == 1) {
			g2.rotate(-a, x + 17, y + 17);
		}
	}
	
	public void setStatus() {
		if (flag == Bird.DOWN) {
			flag = Bird.FLY;
			ooldx = x;
			ooldy = y;
			GameFrame.start = System.currentTimeMillis();
		} else {
			flag = Bird.DOWN;
			ooldx = x;
			ooldy = y;
			GameFrame.start = System.currentTimeMillis();
			upv = upvn;
		}
	}
	
	public void setFlyStatus() {
		flag = Bird.FLY;
		ooldx = x;
		ooldy = y;
		GameFrame.start = System.currentTimeMillis();
		upv = upvn;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void reStart() {
		x = oldx;
		y = oldy;
		ooldx = x;
		ooldy = y;
	}
	
	public void move() {
		if (GameFrame.flag == 0) {
			setImageIndex();
			return;
		} else if (GameFrame.flag == 2) {
			return;
		}
		if (flag == Bird.DOWN) {
			long end = System.currentTimeMillis();
		    long t = (end - GameFrame.start);
			int oy = (int) (ooldy + 0.8 * g * t * t);
			y = oy;
		} else {
			y--;
			long end = System.currentTimeMillis();
		    long t = (end - GameFrame.start);
			upv += 20 * g * t;
			if((upvn - 60 * g * t) <= 0){
				setStatus();
			}
		}
		setImageIndex();
	}
	
	@Override
	public void run() {
		while (true) {
			move();
			try {
				if (flag == Bird.FLY) {
					sleep(upv);
				} else {
					sleep(downv);
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(Bird.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public int getFlag() {
		return flag;
	}
	
}
