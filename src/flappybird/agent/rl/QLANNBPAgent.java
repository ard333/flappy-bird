/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird.agent.rl;

import flappybird.agent.Agent;
import flappybird.agent.rl.ann.ANNBackpropagation;
import flappybird.game.Environment;
import java.awt.event.ActionEvent;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author ardiansyah
 */
public class QLANNBPAgent implements Agent{
	
	private ANNBackpropagation annbpAction0;
	private Double[][] w1Action0 = new Double[3][4];
	private Double[][] w2Action0 = new Double[5][1];
	private ANNBackpropagation annbpAction1;
	private Double[][] w1Action1 = new Double[3][4];
	private Double[][] w2Action1 = new Double[5][1];
	
	private int cAction;
	private int pAction;
	
	private ArrayList<Double> cState;
	private ArrayList<Double> pState;
	
	private int pYBird;
	private int pScore;
	
	private final Random r = new Random();
	private final double discount = 0.9;
	private final double epsilon = 0.0;
	private final double learningRateQL = 0.9;
	private double prob;
	
	private final Timer resetWeightsTime;
	private int pTotalScoreForReinit;
	private int cTotalScoreForReinit;
	
	private boolean learningStatus;
	
	public QLANNBPAgent(boolean learningStatus) {
		
		annbpAction0 = new ANNBackpropagation(2, 4, 1, 0.9);
		annbpAction1 = new ANNBackpropagation(2, 4, 1, 0.9);
		
		this.pYBird = 250;
		this.pAction = 1;
		this.pScore = 0;
		
		pState = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			pState.add(0.0);
		}
		
		this.cState = new ArrayList<>();
		this.learningStatus = learningStatus;
		
		resetWeightsTime = new Timer(1000 * 60 * 1, (ActionEvent e) -> {
			if (this.learningStatus) {
				if (pTotalScoreForReinit == cTotalScoreForReinit) {
					System.out.println("Re-init weights...");
					annbpAction0.initWeight();
					annbpAction1.initWeight();
					cTotalScoreForReinit = pTotalScoreForReinit = 0;
				}
				pTotalScoreForReinit = cTotalScoreForReinit;
			}
		});
		resetWeightsTime.setRepeats(true);
		resetWeightsTime.start();
		
		System.out.println(new Time(System.currentTimeMillis()));
		
		if (!learningStatus) {
			System.out.println("Learning done...");
			Double[][] w1 = new Double[3][4];
			Double[][] w2 = new Double[5][1];
			w1[0][0]=3.2186936230579097;w1[0][1]=2.778821248248173;w1[0][2]=1.6295539158605414;w1[0][3]=2.766223345914354;
			w1[1][0]=0.16534459314000965;w1[1][1]=0.33115227130518826;w1[1][2]=0.34388285561574905;w1[1][3]=0.412983335896627;
			w1[2][0]=-0.7497859871019271;w1[2][1]=-0.5057207308029977;w1[2][2]=0.14382053995439478;w1[2][3]=-0.5323712316746573;
			w2[0][0]=3.1083244987047083;
			w2[1][0]=2.617148568378103;
			w2[2][0]=1.5414226835375435;
			w2[3][0]=2.5204955621078415;
			w2[4][0]=0.3233471593240993;
			this.setWeights(w1, w2, 0);
			w1[0][0]=0.6718553421939059;w1[0][1]=0.3882279524921715;w1[0][2]=0.41891173166717954;w1[0][3]=0.6778715477815199;
			w1[1][0]=0.16476421651423143;w1[1][1]=1.0371516517587211;w1[1][2]=1.4851440624737031;w1[1][3]=0.9400089112205211;
			w1[2][0]=0.5782754454491055;w1[2][1]=0.8059289375834762;w1[2][2]=0.7492527962696298;w1[2][3]=0.4286741131303963;
			w2[0][0]=0.46555890435290176;
			w2[1][0]=1.2857846092453096;
			w2[2][0]=1.6405075561201958;
			w2[3][0]=0.8083479348516789;
			w2[4][0]=1.5774712420658688;
			this.setWeights(w1, w2, 1);
		}
	}
	
	private void setWeights(Double[][] w1, Double[][] w2, Integer action) {
		if (action == 0) {
			annbpAction0.setWeight(w1, w2);
		} else if(action == 1) {
			annbpAction1.setWeight(w1, w2);
		}
	}
	
	@Override
	public int getAction(Environment env) {
		double reward = 0;
		//======================================================================
		if (env.getDeadStatus()) {
			cTotalScoreForReinit += pScore;
			reward -= 1.0;
		} else {
			reward += 0.1;
		}
		if (env.getScore() > pScore) {
			reward += 1.0;
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
		
		if (learningStatus) {
			Double[] expectedOut = new Double[1];
			Double pQas = this.approxValue(pState, pAction);

			expectedOut[0] = (pQas + this.learningRateQL * (reward + (this.discount * this.getBestActionValue(this.cState).getValue()) - pQas));
			
			if (this.pAction == 0) {
				this.annbpAction0.trainOnce(this.convertState(pState), expectedOut);
			} else if(this.pAction == 1) {
				this.annbpAction1.trainOnce(this.convertState(pState), expectedOut);
			}
		}
		
		pYBird = env.getBird().getY();
		this.pScore = env.getScore();
		this.pState = new ArrayList<>(this.cState);
		this.pAction = this.cAction;
		
		if (learningStatus) {
			this.checkLearningLimit(env);
		}
		return this.cAction;
	}
	
	private void checkLearningLimit(Environment env) {
		if (pScore == 14) {
			for (int i = 0; i < this.annbpAction0.getW1().length; i++) {
				System.arraycopy(this.annbpAction0.getW1()[i], 0, this.w1Action0[i], 0, this.annbpAction0.getW1()[0].length);
			}
			for (int i = 0; i < this.annbpAction0.getW2().length; i++) {
				System.arraycopy(this.annbpAction0.getW2()[i], 0, this.w2Action0[i], 0, this.annbpAction0.getW2()[0].length);
			}
			for (int i = 0; i < this.annbpAction1.getW1().length; i++) {
				System.arraycopy(this.annbpAction1.getW1()[i], 0, this.w1Action1[i], 0, this.annbpAction1.getW1()[0].length);
			}
			for (int i = 0; i < this.annbpAction1.getW2().length; i++) {
				System.arraycopy(this.annbpAction1.getW2()[i], 0, this.w2Action1[i], 0, this.annbpAction1.getW2()[0].length);
			}
		}
		if (pScore >= 30) {
			this.stopLearning();
			this.annbpAction0.setWeight(w1Action0, w2Action0);
			this.annbpAction1.setWeight(w1Action1, w2Action1);
		}
	}
	
	private ArrayList<Double> getState(Environment env) {
		ArrayList<Double> stateResult = new ArrayList<>();
		
		double diffBirdBottomPipe = (double)((env.getPipe()[env.getPipeFront()].getY()+env.getPipeGap())-(env.getBird().getY()))/100;
		double distanceBirdToPipe = (double)(env.getPipe()[env.getPipeFront()].getX()-(env.getBird().getX()))/100;
		if (distanceBirdToPipe<0) {
			distanceBirdToPipe=0;
		}
		stateResult.add(diffBirdBottomPipe);
		stateResult.add(distanceBirdToPipe);
		return stateResult;
	}
	
	private Double[] convertState(ArrayList<Double> stateChoose) {
		Double[] state = new Double[stateChoose.size()];
		
		int i = 0;
		for (Double s : stateChoose) {
			state[i] = (double)s;
			i++;
		}
		return state;
	}
	
	private double approxValue(ArrayList<Double> stateChoose, Integer action) {
		Double[] resultValue = new Double[1];
		Double[] state = convertState(stateChoose);
		
		if (action == 1) {
			this.annbpAction1.test(state);
			resultValue = annbpAction1.getOutput();
		} else if (action == 0) {
			this.annbpAction0.test(state);
			resultValue = annbpAction0.getOutput();
		}
		return resultValue[0];
	}
	
	private HashMap.Entry<Integer, Double> getBestActionValue(ArrayList<Double> stateChoose) {
		int actionAtMax;
		double valueAtMax;
		double valueAction0 = this.approxValue(stateChoose, 0);
		double valueAction1 = this.approxValue(stateChoose, 1);
		if (valueAction1 < valueAction0) {
			actionAtMax = 0;
			valueAtMax = valueAction0;
		} else {
			actionAtMax = 1;
			valueAtMax = valueAction1;
		}
		return new HashMap.SimpleEntry<>(actionAtMax, valueAtMax);
	}
	
	private void stopLearning() {
		System.out.println("Learning done...");
		System.out.println(new Time(System.currentTimeMillis()));
		learningStatus = false;
	}
	
	public void dumpWeights() {
		System.out.println("Action 0");
		annbpAction0.dumpWeight();
		System.out.println("Action 1");
		annbpAction1.dumpWeight();
	}
}
