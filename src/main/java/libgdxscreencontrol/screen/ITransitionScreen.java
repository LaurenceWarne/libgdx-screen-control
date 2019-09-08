package libgdxscreencontrol.screen;

import com.badlogic.gdx.Screen;

/**
 * Represents a screen which can be 'completed' in some capacity.
 */
public interface ITransitionScreen extends Screen {

    boolean isFinished();

    public interface ITransitionScreenFactory<T extends ITransitionScreen> {

	T create();
    }
}

