package libgdxscreencontrol;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.ITransitionScreen;
import lombok.NonNull;

/**
 * Builder class for easy creation of <code>ScreenController</code> instances.
 */
public class ScreenControllerBuilder {

    @NonNull
    private final TransitionScreenController transitionController =
	new TransitionScreenController();
    @NonNull
    private final ChoiceScreenController choiceController =
	new ChoiceScreenController();
    @NonNull
    private String startingScreenName;

    public ScreenControllerBuilder withStartingScreen(
	@NonNull String screenName
    ) throws IllegalArgumentException {
	if (isScreenRegistered(screenName)) {
	    this.startingScreenName = screenName;
	    return this;
	}
	else {
	    throw new IllegalArgumentException(
		"A screen with name: " + startingScreenName + " has not been registered"
	    );
	}
    }
    
    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull ITransitionScreen screen
    ) {
	transitionController.add(name, screen);
	return this;
    }

    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull IChoiceScreen screen
    ) {
	choiceController.add(name, screen);
	return this;
    }    

    public ScreenControllerBuilder choice(
	@NonNull String choiceScreenName, @NonNull String choiceName, int choice
    ) throws IllegalArgumentException {
	if (!choiceController.has(choiceScreenName)) {
	    throw new IllegalArgumentException(
		"Cannot find a registered choice screen with name: " + choiceScreenName
	    );
	}
	else {
	    choiceController.setChoice(choiceScreenName, choice, choiceName);
	    return this;
	}
    }

    public ScreenControllerBuilder follow(
	@NonNull String transitionScreenName, @NonNull String screenName
    ) {
	if (!transitionController.has(transitionScreenName)) {
	    throw new IllegalArgumentException(
		"Cannot find a registered choice screen with name: " +
		transitionScreenName
	    );
	}
	else {
	    transitionController.setTransition(transitionScreenName, screenName);
	    return this;
	}	
    }

    public <T extends ITransitionScreen> T get(
	@NonNull String name, Class<T> clazz
    ) throws IllegalArgumentException {
	final ITransitionScreen screen;
	if (transitionController.has(name)) {
	    screen = transitionController.get(name);
	}
	else if (choiceController.has(name)) {
	    screen = choiceController.get(name);
	}
	else {
	    throw new IllegalArgumentException(
		"Could not find a screen registered with name: " + name
	    );
	}
	try {
	    return (T) screen;
	} catch (ClassCastException e) {
	    throw new IllegalArgumentException(
		name + " screen is of type: " + screen.getClass() +
		" not of type " + clazz
	    );
	}
    }

    public boolean isScreenRegistered(@NonNull String screenName) {
	return choiceController.has(screenName) ||
	    transitionController.has(screenName);
    }

    public ScreenController build() {
	// validate?
	return new ScreenController(
	    transitionController, choiceController, startingScreenName
	);
    }
}
