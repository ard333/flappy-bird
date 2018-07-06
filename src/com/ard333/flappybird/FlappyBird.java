package com.ard333.flappybird;

import com.ard333.flappybird.agent.rl.QLANNBPAgent;
import com.ard333.flappybird.agent.rl.QLAgent;
import com.ard333.flappybird.agent.rl.SARSAANNBPAgent;
import com.ard333.flappybird.agent.rl.SARSAAgent;
import com.ard333.flappybird.game.GameFrame;

/**
 *
 * @author Ardiansyah <ard333.ardiansyah@gmail.com>
 */
public class FlappyBird {
	
	public static void main(String[] args) {
		
		//Q-Learning
//		QLAgent agent = new QLAgent(120);

		//SARSA
//		SARSAAgent agent = new SARSAAgent(120);
		
		//Combination Q-Learning and Backpropagation
//		QLANNBPAgent agent = new QLANNBPAgent(true);
		
		//Combination SARSA and Backpropagation
		SARSAANNBPAgent agent = new SARSAANNBPAgent(true);
		
		GameFrame game = new GameFrame(agent);
		
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
//			QLANNBP.dumpWeights();
//			QLA.dumpTableSize();
		}));
	}
	
}
