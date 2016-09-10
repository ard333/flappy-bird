/*
 * Ardiansyah | http://ard.web.id
 */
package id.web.ard.flappybird;

import id.web.ard.flappybird.agent.rl.QLANNBPAgent;
import id.web.ard.flappybird.agent.rl.QLAgent;
import id.web.ard.flappybird.agent.rl.SARSAAgent;
import id.web.ard.flappybird.game.GameFrame;

/**
 *
 * @author Ardiansyah <ard333.ardiansyah@gmail.com>
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
