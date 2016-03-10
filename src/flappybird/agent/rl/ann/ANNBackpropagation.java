/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird.agent.rl.ann;

import java.util.Random;

/**
 *
 * @author ardiansyah
 */
public class ANNBackpropagation {
	private final Integer numOfInput;
	private final Integer numOfHidden;
	private final Integer numOfOutput;
	
	private final Double learningRate;
	
	private Double[] X;//input
	private Double[] Y;//hidden
	private Double[] Z;//output
	
	private Double[][] w1;//input->hidden
	private Double[][] w2;//hidden->output
	
	private Double[][] deltaW1;
	private Double[][] deltaW2;
	
	public ANNBackpropagation(Integer numOfInput, Integer numOfHidden, Integer numOfOutput, Double learningRate) {
		this.numOfInput = numOfInput;
		this.numOfHidden = numOfHidden;
		this.numOfOutput = numOfOutput;
		this.learningRate = learningRate;
		
		this.init();
	}
	
	private void init() {
		this.X = new Double[numOfInput+1];
		this.Y = new Double[numOfHidden+1];
		this.Z = new Double[numOfOutput];
		this.X[numOfInput] = 1.0;//bias at last index
		this.Y[numOfHidden] = 1.0;//bias at last index
		
		this.w1 = new Double[numOfInput+1][numOfHidden];
		this.w2 = new Double[numOfHidden+1][numOfOutput];
		this.deltaW1 = new Double[numOfInput+1][numOfHidden];
		this.deltaW2 = new Double[numOfHidden+1][numOfOutput];
		
		Random r = new Random();
		
		for (int i = 0; i < this.numOfInput+1; i++) {
			for (int j = 0; j < this.numOfHidden; j++) {
				this.w1[i][j] = 0 + (1 - (0)) * r.nextDouble();//0:1
			}
		}
		for (int i = 0; i < numOfHidden+1; i++) {
			for (int j = 0; j < numOfOutput; j++) {
				this.w2[i][j] = 0 + (1 - (0)) * r.nextDouble();//0:1
			}
		}
	}
	
	public void setWeight(Double[][] inputToHidden, Double[][] hiddenToOutput) {
		for (int i = 0; i < this.w1.length; i++) {
			System.arraycopy(inputToHidden[i], 0, w1[i], 0, this.w1[0].length);
		}
		for (int i = 0; i < this.w2.length; i++) {
			System.arraycopy(hiddenToOutput[i], 0, w2[i], 0, this.w2[0].length);
		}
	}
	
	public void trainOnce(Double[] input, Double[] expectedOutput) {
		System.arraycopy(input, 0, this.X, 0, input.length);
		
		this.feedForward();
		this.backPropagation(expectedOutput);
	}
	public void test(Double[] input) {
		System.arraycopy(input, 0, this.X, 0, this.numOfInput);
		this.feedForward();
	}
	
	private void feedForward() {
		this.setOutputY();
		this.setOutputZ();
	}
	private void setOutputY() {
		Double temp[] = new Double[numOfHidden];
		for (int a = 0; a < numOfHidden; a++) {
			temp[a] = 0.0;
		}
		for (int j = 0; j < this.numOfHidden; j++) {
			for (int i = 0; i < this.numOfInput+1; i++) {
				temp[j] = temp[j] + this.X[i] * this.w1[i][j];
			}
		}
		for (int j = 0; j < numOfHidden; j++) {
			this.Y[j] = this.sigmoidBipolar(temp[j]);
		}
	}
	private void setOutputZ() {
		Double temp[] = new Double[numOfOutput];
		for (int a = 0; a < numOfOutput; a++) {
			temp[a] = 0.0;
		}
		for (int k = 0; k < this.numOfOutput; k++) {
			for (int j = 0; j < this.numOfHidden+1; j++) {
				temp[k] = temp[k] + this.Y[j] * this.w2[j][k];
			}
		}
		for (int k = 0; k < this.numOfOutput; k++) {
			this.Z[k] = this.sigmoidBipolar(temp[k]);
		}
	}
	
	private void backPropagation(Double[] expectedOutput) {
		Double[] fO = new Double[this.numOfOutput];
		
		for (int k = 0; k < numOfOutput; k++) {
			fO[k] = (expectedOutput[k]-this.Z[k]) * (0.5 * (1+Z[k])* (1-Z[k]));
		}
		for (int j = 0; j < this.numOfHidden+1; j++) {//+bias weight
			for (int k = 0; k < this.numOfOutput; k++) {
				this.deltaW2[j][k] = this.learningRate * fO[k] * this.Y[j];
			}
		}
		Double[] fHNet = new Double[this.numOfHidden];
		for (int j = 0; j < this.numOfHidden; j++) {
			fHNet[j] = 0.0;
			for (int k = 0; k < this.numOfOutput; k++) {
				fHNet[j] = fHNet[j] + (fO[k]*this.w2[j][k]);
			}
		}
		Double[] fH = new Double[this.numOfHidden];
		for (int j = 0; j < this.numOfHidden; j++) {
			fH[j] = fHNet[j] * (0.5 * (1+Y[j])* (1-Y[j]));
		}
		for (int i = 0; i < this.numOfInput+1; i++) {
			for (int j = 0; j < numOfHidden; j++) {
				this.deltaW1[i][j] = this.learningRate * fH[j] * this.X[i];
			}
		}
		this.changeWeight();
	}
	private void changeWeight() {
		for (int j = 0; j < numOfHidden+1; j++) {
			for (int k = 0; k < numOfOutput; k++) {
				this.w2[j][k] = this.w2[j][k] + this.deltaW2[j][k];
			}
		}
		for (int i = 0; i < numOfInput+1; i++) {
			for (int j = 0; j < numOfHidden; j++) {
				this.w1[i][j] = this.w1[i][j] + this.deltaW1[i][j];
			}
		}
	}
	
	public Double[][] getW1() {
		return this.w1;
	}
	public Double[][] getW2() {
		return this.w2;
	}
	
	private Double sigmoidBipolar(Double input) {
		return 2/(1 + Math.exp(-input))-1;
	}
	
	public Double[] getOutput() {
		return this.Z;
	}
	
	public void dumpWeight(){
		for (int i = 0; i < numOfInput+1; i++) {
			for (int j = 0; j < numOfHidden; j++) {
				System.out.print("w1["+i+"]["+j+"]="+w1[i][j]+";");
			}
			System.out.println("");
		}
		for (int i = 0; i < numOfHidden+1; i++) {
			for (int j = 0; j < numOfOutput; j++) {
				System.out.print("w2["+i+"]["+j+"]="+w2[i][j]+";");
			}
			System.out.println("");
		}
	}
}
