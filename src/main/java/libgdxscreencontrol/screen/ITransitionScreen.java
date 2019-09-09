package libgdxscreencontrol.screen;

import com.badlogic.gdx.Screen;

/**
 * Represents a screen which can be 'completed' in some capacity.
 */
public interface ITransitionScreen extends Screen {

    /**
     * Return true if this screen has 'completed', else false.
     * 
     * @return true if this screen has 'completed', else false
     */
    boolean isFinished();

    /**
     * Reset this screen so that it can be used again.
     */
    void reset();

    public interface ITransitionScreenFactory<T extends ITransitionScreen> {

	T create();
    }
}

