package libgdxscreencontrol;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ObjectMap;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.ITransitionScreen;
import lombok.NonNull;

public class ScreenController {

    @NonNull
    private final TransitionScreenController transitionController;
    @NonNull
    private final ChoiceScreenController choiceController;
    @NonNull
    private Screen currentScreen;
    @NonNull
    private String currentScreenName;
    @NonNull
    private ObjectMap<String, Screen> usedScreens = new ObjectMap<>();

    ScreenController(
	@NonNull TransitionScreenController transitionScreenController,
	@NonNull ChoiceScreenController choiceScreenController,
	@NonNull String startingScreen
    ) throws IllegalArgumentException {
	this.transitionController = transitionScreenController;
	this.choiceController = choiceScreenController;
	this.currentScreenName = startingScreen;
	try {
	    setCurrentScreenFromName();
	} catch (IllegalStateException e) {
	    throw new IllegalArgumentException(
		"The screen: " + startingScreen + " does not refer to any" +
		" registered screen"
	    );
	}
    }

    public Screen get() throws IllegalStateException {
	if (transitionController.has(currentScreenName)) {
	    return transitionController.get(currentScreenName);
	}
	else if (choiceController.has(currentScreenName)) {
	    return choiceController.get(currentScreenName);
	}
	else {
	    throw new IllegalStateException(
		"No screen found with name: " + currentScreenName
	    );
	}
    }

    public boolean update() throws IllegalStateException {
	if (transitionController.has(currentScreenName)) {
	    return updateTransition();
	}
	else if (choiceController.has(currentScreenName)) {
	    return updateChoice();
	}
	else {
	    throw new IllegalStateException(
		"No screen found with name: " + currentScreenName
	    );
	}	
    }

    private boolean updateTransition() throws IllegalStateException {
	final ITransitionScreen screen = transitionController.get(currentScreenName);
	if (screen.isFinished()) {
	    currentScreenName = transitionController.getTransition(currentScreenName);
	    setCurrentScreenFromName();
	    return true;
	}
	else {
	    return false;
	}
    }

    private boolean updateChoice() throws IllegalStateException {
	final IChoiceScreen screen = choiceController.get(currentScreenName);
	if (screen.isFinished()) {
	    final int choice = screen.getChoice();
	    currentScreenName = choiceController.getChoice(currentScreenName, choice);
	    setCurrentScreenFromName();
	    return true;
	}
	else {
	    return false;
	}	
    }

    private void setCurrentScreenFromName() throws IllegalStateException {
	if (transitionController.has(currentScreenName)) {
	    this.currentScreen = transitionController.get(currentScreenName);
	    if (usedScreens.containsKey(currentScreenName)) {
		transitionController.get(currentScreenName).reset();
	    }
	}
	else if (choiceController.has(currentScreenName)) {
	    this.currentScreen = choiceController.get(currentScreenName);
	    if (usedScreens.containsKey(currentScreenName)) {
		choiceController.get(currentScreenName).reset();
	    }
	}
	else {
	    throw new IllegalStateException(
		"No screen found with name: " + currentScreenName
	    );
	}
	usedScreens.put(currentScreenName, currentScreen);	    
    }

    public void dispose() {
	for (Screen screen : usedScreens.values()) {
	    screen.dispose();
	}
    }
}
