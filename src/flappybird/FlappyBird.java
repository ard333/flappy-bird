/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird;

import flappybird.agent.RandomActionAgent;
import flappybird.game.GameFrame;

/**
 *
 * @author ardiansyah
 */
public class FlappyBird {
	
	public static void main(String[] args) {
		RandomActionAgent randomAgent = new RandomActionAgent();
		GameFrame game = new GameFrame(randomAgent);
	}
	
}
