package com.ard333.flappybird.game;

import com.ard333.flappybird.agent.Agent;
import java.util.Random;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
	private final GamePanel gamePanel;  
	private Bird bird;
	private final Pipe[] pipe;
	public static int score;
	
	//celah
	public static final int w = 120;
	//jarak antar pipa
	public static final int d = 170 + 52;
	
	public static int flag = 0;
	public static long start = 0;
	
	private final Score gs;
	private final int x = 300;
	private final int[] ypoints = {
		new Random().nextInt(322 - GameFrame.w) +  50,
		new Random().nextInt(322 - GameFrame.w) +  50,
		new Random().nextInt(322 - GameFrame.w) +  50,
		new Random().nextInt(322 - GameFrame.w) +  50
	};
	
	public GameFrame(Agent agent) {
		GameFrame.score = 0;
		gs = new Score();
		GameFrame.start = System.currentTimeMillis();
		
		bird = new Bird(135,250,10,3,60);
		pipe = new Pipe[4];
		for (int i = 0; i < ypoints.length; i++) {
			pipe[i] = new Pipe(x + i * GameFrame.d, ypoints[i], 7);
		}
		
		gamePanel = new GamePanel(bird, pipe, gs, agent);
		
		bird.start();
		for (Pipe p : pipe) {
			p.start();
		}
		
		Thread gp = new Thread(gamePanel);
		gp.start();
		
		add(gamePanel);
		setResizable(false);
		setTitle("Flappy Bird");
		setBounds(10,10,400,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static int getScore() {
		return GameFrame.score;
	}
	
	public static void addScore() {
		if (GameFrame.flag == 1) {
			GameFrame.score++;
		}
	}
	
	public void setStatus(int status) {
		GameFrame.flag = status;
	}
}
