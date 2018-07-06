package com.ard333.flappybird.game;

import com.ard333.flappybird.FlappyBird;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Score {
	private ArrayList<Image> score;
	
	public Score() {
		score = new ArrayList<>();
		score.add(new ImageIcon(FlappyBird.class.getResource("image/0.png")).getImage());
	}
	
	public void drawSelf(Graphics g) {
		showStartScore(g);
	}
	
	public void showStartScore(Graphics g) {
		String s = String.valueOf(GameFrame.score);
		char[] ss = s.toCharArray();
		int start = 200 - ss.length * 20;
		
		score = new ArrayList<>();
		for (int i = 0; i < ss.length; i++) {
			score.add(new ImageIcon(FlappyBird.class.getResource("image/" + ss[i] + ".png")).getImage());
		}
		
		int i = 0;
		for (Image sc : score) {
			g.drawImage(sc, start + i++ * 40, 30, 40, 60, null);
		}
	}
}
