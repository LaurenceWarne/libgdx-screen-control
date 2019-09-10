package libgdxscreencontrol;

import com.badlogic.gdx.ScreenAdapter;

import libgdxscreencontrol.screen.ITransitionScreen;

public class MyTransitionScreen extends ScreenAdapter implements ITransitionScreen {

    @Override
    public boolean isFinished() {
	return false;
    }

    @Override
    public void reset() {
		
    }
}
