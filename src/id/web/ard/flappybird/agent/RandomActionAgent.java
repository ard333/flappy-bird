/*
 * Ardiansyah | http://ard.web.id
 */
package id.web.ard.flappybird.agent;

import id.web.ard.flappybird.game.Environment;
import java.util.Random;

/**
 *
 * @author @author Ardiansyah <ard333.ardiansyah@gmail.com>
 */
public class RandomActionAgent implements Agent{

	@Override
	public int getAction(Environment env) {
		//random action 1 or 0
		return new Random().nextInt(2);
	}
	
}
