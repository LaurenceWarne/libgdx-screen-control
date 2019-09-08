package libgdxscreencontrol.screen;

public interface IChoiceScreen extends ITransitionScreen {

    int getNoOfChoices();

    int getChoice();

    public interface IChoiceScreenFactory<T extends IChoiceScreen> {
	
	T create();	
    }
}

