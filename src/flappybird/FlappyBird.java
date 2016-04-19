/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird;

import flappybird.agent.rl.QLANNBPAgent;
import flappybird.agent.rl.QLAgent;
import flappybird.game.GameFrame;

/**
 *
 * @author ardiansyah
 */
public class FlappyBird {
	
	public static void main(String[] args) {
		
		//Q-Learning
//		QLAgent QLA = new QLAgent(25);
//		GameFrame game = new GameFrame(QLA);
		
		//Combination Q-Learning and Backpropagation
		QLANNBPAgent QLANNBP = new QLANNBPAgent(true);
		GameFrame game = new GameFrame(QLANNBP);
		
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
//			QLANNBP.dumpWeights();
//			QLA.dumpTableSize();
		}));
	}
	
}
