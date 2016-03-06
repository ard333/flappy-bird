package flappybird.game;

import flappybird.FlappyBird;
import flappybird.agent.Agent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements Runnable {
	private final Bird bird;	
	private final Pipe[] pipe;
	
	//(index)pipe in front of the bird
	private int pipeFront;
	
	private final Image background;	
	private final Image ground;
	
	private final Score gs;
	private final Agent agent;
	private Environment env = new Environment();
	private boolean isBirdPass;
	
	private Timer getActionTimer;
	private int agentAction;
	private int pYBird;
	private boolean deadStatus;
	
	public GamePanel(Bird bird,Pipe[] pipe, Score gs, Agent agent) {
		background = new ImageIcon(FlappyBird.class.getResource("image/Background.png")).getImage();
		ground = new ImageIcon(FlappyBird.class.getResource("image/Ground.png")).getImage();
		
		this.bird = bird;
		this.pipe = new Pipe[4];
		System.arraycopy(pipe, 0, this.pipe, 0, this.pipe.length);
		this.gs = gs;
		this.agent = agent;
		this.isBirdPass = false;
		
		pYBird = 200;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(background, 0, 0, 800, 600, null);
		
		for (Pipe p : pipe) {
			p.drawSelf(g);
			if (p.isBirdDied(bird)) {
				GameFrame.flag = 2;
			}
			if (p.isBirdPass(bird)) {
				GameFrame.addScore();
				isBirdPass = true;
			}
		}
		g.drawImage(ground, 0, 480, 800, 112, null);
		bird.drawSelf(g);
		gs.drawSelf(g);
		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void run() {
		GameFrame.flag = 1;
		while (true) {
			long start = System.currentTimeMillis();
			
			if (pipe[pipeFront].getX()-bird.getX() < -65) {
				if (pipeFront == 3) {
					pipeFront = 0;
				} else {
					pipeFront++;
				}
			}
			if (GameFrame.flag == 2) {
				deadStatus = true;
				System.out.println("Score \t"+GameFrame.score);
				pipeFront = 0;
				bird.reStart();
				for (Pipe p : pipe) {
					p.reStart();
				}
			}
			env.setEnvironment(bird, pipe, pipeFront, deadStatus);
			//System.out.println(getState(env));
			this.isBirdPass = false;
			
			pYBird = env.getBird().getY();
			
			agentAction = agent.getAction(env);
			if (this.agentAction == 1) {
				bird.setFlyStatus();
			}
			
			if (GameFrame.flag == 2) {
				GameFrame.score = 0;
				GameFrame.start = System.currentTimeMillis();
				GameFrame.flag = 1;
				deadStatus = false;
			}
			repaint();
			
			long end = System.currentTimeMillis();
			if (end - start < (1000 / 60)) {
				try {
					Thread.sleep(1000 / 60 - end + start);
				} catch (InterruptedException ex) {
					Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}
}
