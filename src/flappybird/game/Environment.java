/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird.game;

/**
 *
 * @author ardiansyah
 */
public class Environment {
	private Bird bird;	
	private Pipe[] pipe;
	private boolean deadStatus;
	private int pipeFront;
	private int pipeGap;
	
	public void setEnvironment(Bird bird, Pipe[] pipe, int pipeFront, boolean deadStatus) {
		this.bird = bird;
		this.pipe = pipe;
		this.deadStatus = deadStatus;
		this.pipeFront = pipeFront;
		this.pipeGap = GameFrame.w;
	}

	public Bird getBird() {
		return bird;
	}
	
	public Pipe[] getPipe() {
		return pipe;
	}
	
	public boolean getDeadStatus() {
		return deadStatus;
	}
	
	public int getPipeFront() {
		return pipeFront;
	}
	
	public int getPipeGap() {
		return pipeGap;
	}
	
	public int getScore() {
		return GameFrame.score;
	}
}
