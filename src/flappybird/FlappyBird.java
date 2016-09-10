/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird;

import flappybird.agent.rl.QLANNBPAgent;
import flappybird.agent.rl.QLAgent;
import flappybird.agent.rl.SARSAAgent;
import flappybird.game.GameFrame;

/**
 *
 * @author ardiansyah
 */
public class FlappyBird {
	
	public static void main(String[] args) {
		
		//Q-Learning
//		QLAgent agent = new QLAgent(120);

		//SARSA
		SARSAAgent agent = new SARSAAgent(120);
		
		//Combination Q-Learning and Backpropagation
//		QLANNBPAgent agent = new QLANNBPAgent(true);
		
		
		
		GameFrame game = new GameFrame(agent);
		
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
//			QLANNBP.dumpWeights();
//			QLA.dumpTableSize();
		}));
	}
	
}
