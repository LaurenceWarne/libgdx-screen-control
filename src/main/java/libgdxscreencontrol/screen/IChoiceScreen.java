package libgdxscreencontrol.screen;

/**
 * Represents a screen which can be 'completed', but may not always be followed by the same screen. A menu screen for example.
 */
public interface IChoiceScreen extends ITransitionScreen {

    /**
     * Return a positive integer indicating which screen should follow this one, or -1 if this screen has not finished processing.
     *
     * @return integer indicating next screen or -1
     */
    int getChoice();

    public interface IChoiceScreenFactory<T extends IChoiceScreen> {
	
	T create();	
    }
}

