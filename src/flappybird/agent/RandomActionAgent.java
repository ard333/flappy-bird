/*
 * Ardiansyah | http://ard.web.id
 */
package flappybird.agent;

import flappybird.game.Environment;
import java.util.Random;

/**
 *
 * @author ardiansyah
 */
public class RandomActionAgent implements Agent{

	@Override
	public int getAction(Environment env) {
		//random action 1 or 0
		return new Random().nextInt(2);
	}
	
}
