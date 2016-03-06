/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird.agent.rl;

import flappybird.agent.Agent;
import flappybird.game.Environment;
import flappybird.game.GameFrame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author ardiansyah
 */
public class QLAgent implements Agent{
	
	private HashMap<ArrayList<Integer>, HashMap<Integer, Double>> QTable;
	
	private int cAction;
	private int pAction;
	
	private ArrayList<Integer> cState;
	private ArrayList<Integer> pState;
	
	private int pYBird;
	private int pScore;
	
	private final Random r;
	private final double discount;
	private final double epsilon;
	private final double learningRateQL;
	private double prob;
	
	private final Timer learningTime;
	private boolean learningStatus;
	
	public QLAgent(int minute) {
		this.r = new Random();
		this.epsilon = 0.0;
		this.learningRateQL = 0.9;
		this.discount = 0.9;
		this.QTable = new HashMap<>();
		this.pYBird = 250;
		this.pAction = 1;
		
		learningStatus = true;
		learningTime = new Timer(1000 * 60 * minute, (ActionEvent e) -> {
			GameFrame.score = 0;
			System.out.println("Learning done...");
			learningStatus = false;
		});
		learningTime.setRepeats(false);
		learningTime.start();
	}
	
	@Override
	public int getAction(Environment env) {
		int reward = 0;
		//======================================================================
		if (env.getDeadStatus()) {
			reward -= 100;
		}
//		else {
//			reward += 1;
//		}
		if (env.getScore() > pScore) {
			reward += 100;
		}
		//======================================================================
		
		this.cState = getState(env);
		
		//======================================================================
		this.prob = this.r.nextDouble();
		//Epsilon Greedy
		if (this.prob > this.epsilon || !learningStatus) {
			this.cAction = this.getBestActionValue(this.cState).getKey();
		} else {
			this.cAction = r.nextInt(2);
		}
		//======================================================================
		
		if (!this.QTable.containsKey(this.cState)) {
			HashMap<Integer, Double> temp = new HashMap<>();
			temp.put(1, 0.0);
			temp.put(0, 0.0);
			this.QTable.put(this.cState, temp);
		}
		
		if (this.pState!=null && learningStatus) {
			if (this.QTable.get(this.pState).get(this.pAction)!=null) {
				double pQas = this.QTable.get(this.pState).get(this.pAction);
				//update Q value
				this.QTable.get(this.pState).replace(
					this.pAction,
					(pQas + this.learningRateQL * (reward + (this.discount*this.getBestActionValue(this.cState).getValue()) - pQas))
				);
			}
		}
		
		pYBird = env.getBird().getY();
		this.pScore = env.getScore();
		this.pState = new ArrayList<>(this.cState);
		this.pAction = this.cAction;
		
		return this.cAction;
	}
	
	private ArrayList<Integer> getState(Environment env) {
		ArrayList<Integer> stateResult = new ArrayList<>();
		
		int diffBirdBottomPipe = Math.round(((env.getPipe()[env.getPipeFront()].getY()+env.getPipeGap())-env.getBird().getY())/10);
		int distanceBirdToPipe = Math.round((env.getPipe()[env.getPipeFront()].getX()-env.getBird().getX())/30);
		if (distanceBirdToPipe<0) {
			distanceBirdToPipe=0;
		}
		
		stateResult.add(diffBirdBottomPipe);
		stateResult.add(distanceBirdToPipe);
		return stateResult;
	}
	
	private HashMap.Entry<Integer, Double> getBestActionValue(ArrayList<Integer> stateChoose) {
		int actionAtMax = r.nextInt(2);
		double valueAtMax = 0.0;
		HashMap<Integer, Double> temp = this.QTable.get(stateChoose);
		if (temp!=null) {
			double valueAction1 = temp.get(1);
			double valueAction0 = temp.get(0);
			if (valueAction1>valueAction0) {
				actionAtMax = 1;
				valueAtMax = valueAction1;
			} else {
				actionAtMax = 0;
				valueAtMax = valueAction0;
			}
		}
		return new HashMap.SimpleEntry<>(actionAtMax, valueAtMax);
	}
	
	public void dumpTableSize() {
		System.out.println(QTable.size()*2);
	}
}
