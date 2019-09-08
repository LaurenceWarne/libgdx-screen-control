package libgdxscreencontrol;

import com.badlogic.gdx.utils.ObjectMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.ITransitionScreen;
import lombok.NonNull;

/**
 * Builder class for easy creation of <code>ScreenController</code> instances.
 */
public class ScreenControllerBuilder {

    private final ObjectMap<String, ITransitionScreen> transitionScreens =
	new ObjectMap<>();
    private final ObjectMap<String, IChoiceScreen> choiceScreens =
	new ObjectMap<>();
    private final ObjectMap<String, String> transitionMap = new ObjectMap<>();
    private final Table<String, Integer, String> choiceMap = HashBasedTable.create();

    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull ITransitionScreen screen
    ) {
	transitionScreens.put(name, screen);
	return this;
    }

    public ScreenControllerBuilder register(
	@NonNull String name, @NonNull IChoiceScreen screen
    ) {
	choiceScreens.put(name, screen);
	return this;
    }    

    public ScreenControllerBuilder choice(
	@NonNull String choiceScreenName, @NonNull String choiceName, int choice
    ) throws IllegalArgumentException {
	if (!choiceScreens.containsKey(choiceScreenName)) {
	    throw new IllegalArgumentException(
		"Cannot find a registered choice screen with name: " + choiceScreenName
	    );
	}
	else if (!isScreenRegistered(choiceName)) {
	    throw new IllegalArgumentException(
		"Cannot find a registered screen with name: " + choiceName
	    );	    
	}
	else {
	    choiceMap.put(choiceScreenName, choice, choiceName);
	    return this;
	}
    }

    public ScreenControllerBuilder follow(
	@NonNull String transitionScreenName, @NonNull String screenName
    ) {
	if (!transitionScreens.containsKey(transitionScreenName)) {
	    throw new IllegalArgumentException(
		"Cannot find a registered choice screen with name: " +
		transitionScreenName
	    );
	}
	else if (!isScreenRegistered(screenName)) {
	    throw new IllegalArgumentException(
		"Cannot find a registered screen with name: " + screenName
	    );	    
	}
	else {
	    transitionMap.put(transitionScreenName, screenName);
	    return this;
	}	
    }

    public <T extends ITransitionScreen> T get(
	@NonNull String name, Class<T> clazz
    ) throws IllegalArgumentException {
	final ITransitionScreen screen;
	if (transitionScreens.containsKey(name)) {
	    screen = transitionScreens.get(name);
	}
	else if (choiceScreens.containsKey(name)) {
	    screen = choiceScreens.get(name);
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
	return choiceScreens.containsKey(screenName) ||
	    transitionScreens.containsKey(screenName);
    }
}
