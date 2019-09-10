package libgdxscreencontrol;

import com.badlogic.gdx.ScreenAdapter;

import libgdxscreencontrol.screen.IChoiceScreen;

public class MyChoiceScreen extends ScreenAdapter implements IChoiceScreen {

    @Override
    public boolean isFinished() {
	return false;
    }

    @Override
    public void reset() {
		
    }

    @Override
    public int getChoice() {
	return 0;
    }
}
