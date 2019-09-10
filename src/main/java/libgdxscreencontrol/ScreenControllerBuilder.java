package libgdxscreencontrol;

import javax.annotation.Nullable;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.IChoiceScreen.IChoiceScreenFactory;
import libgdxscreencontrol.screen.ITransitionScreen;
import libgdxscreencontrol.screen.ITransitionScreen.ITransitionScreenFactory;
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
    private String startingScreenName;

    /**
     * Set the initial screen of the {@link ScreenController}. Must not be null.
     *
     * @param screenName name of the initial screen.
     * @return this instance for chaining
     * @throws IllegalArgumentException if no screen instance has been registered with this object with the specified name
     */
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

    /**
     * Register the specified {@link ITransitionScreen} with this object with the specified name. If either argument is null, a {@link NullPointerException} is thrown.
     *
     * @param name name to associate with the screen
     * @param screen {@link ITransitionScreen} instance to register
     * @return this instance for chaining
     */
    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull ITransitionScreen screen
    ) {
	transitionController.add(name, screen);
	return this;
    }

    /**
     * Register the specified {@link ITransitionScreenFactory} with this object with the specified name. The factory will be used when the screen is first needed. If either argument is null, a {@link NullPointerException} is thrown.
     *
     * @param name name to associate with the screen
     * @param screen {@link ITransitionScreenFactory} to register
     * @return this instance for chaining
     */
    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull ITransitionScreenFactory<?> factory
    ) {
	transitionController.add(name, factory);
	return this;
    }
    

    /**
     * Register the specified {@link IChoiceScreen} screen with this object with the specified name. If either argument is null, a {@link NullPointerException} is thrown.
     *
     * @param name name to associate with the screen
     * @param screen {@link IChoiceScreen} instance to register
     * @return this instance for chaining
     */
    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull IChoiceScreen screen
    ) {
	choiceController.add(name, screen);
	return this;
    }    

    /**
     * Register the specified {@link IChoiceScreenFactory} with this object with the specified name. The factory will be used when the screen is first needed. If either argument is null, a {@link NullPointerException} is thrown.
     *
     * @param name name to associate with the screen
     * @param screen {@link IChoiceScreenFactory} to register
     * @return this instance for chaining
     */
    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull IChoiceScreenFactory<?> factory
    ) {
	choiceController.add(name, factory);
	return this;
    }    

    /**
     * When the {@link IChoiceScreen} specified by choiceScreenName has finished processing and returned the specified value <code>choice</code>, set the next screen to the screen specified by <code>choiceName</code>.
     *
     * @param choiceScreenName the name of the {@link IChoiceScreen}
     * @param choiceName the name of the screen to follow
     * @param choice the choice returned by the {@link IChoiceScreen}
     * @return this instance for chaining
     * @throws IllegalArgumentException if no screen has been registered with the name <code>choiceScreenName</code>
     *
     */
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

    /**
     * When the {@link ITransitionScreen} specified by <code>transitionScreenName</code> has finished processing, set the next screen to the screen specified by <code>screenName</code>.
     *
     * @param transitionScreenName the name of the {@link ITransitionScreen}
     * @param screenName name of the next screen
     * @return this instance for chaining
     */
    public ScreenControllerBuilder followWith(
	@NonNull String transitionScreenName, @NonNull String screenName
    ) throws IllegalArgumentException {
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

    /**
     * Get the screen registered with the specified name.
     * 
     * @param name name of the screen
     * @param clazz class of the screen
     * @return screen registered with the specified name
     * @throws IllegalArgumentException if the screen registered with name is not an instance of the specified class, or no screen has been registered with the specified name
     */
    public <T extends ITransitionScreen> T get(
	@NonNull String name, @NonNull Class<T> clazz
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
	    return clazz.cast(screen);
	} catch (ClassCastException e) {
	    throw new IllegalArgumentException(
		name + " screen is of type: " + screen.getClass() +
		" not of type " + clazz
	    );
	}
    }

    /**
     * Return true if a screen is registered with the specified name, else false. False is also returned if screenName is null.
     * 
     * @param screenName name of the screen to check
     * @return true if a screen is registered with the specified name, else false
     */
    public boolean isScreenRegistered(@Nullable String screenName) {
	return choiceController.has(screenName) ||
	    transitionController.has(screenName);
    }

    /**
     * Create a {@link ScreenController} instance using the configuration supplied to this object.
     *
     * @return {@link ScreenController}
     */
    public ScreenController build() {
	// validate?
	return new ScreenController(
	    transitionController, choiceController, startingScreenName
	);
    }
}
