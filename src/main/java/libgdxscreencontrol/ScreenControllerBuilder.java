package libgdxscreencontrol;

import com.badlogic.gdx.utils.ObjectMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.ITransitionScreen;
import lombok.NonNull;

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
	if (!isScreenRegistered(choiceScreenName)) {
	    throw new IllegalArgumentException(
		"Cannot find a registered screen with name: " + choiceScreenName
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
	transitionMap.put(transitionScreenName, screenName);
	return this;
    }

    public <T extends ITransitionScreen> T get(@NonNull String name, Class<T> clazz) {
	return null;
    }

    public boolean isScreenRegistered(String screenName) {
	return choiceScreens.containsKey(screenName) ||
	    transitionScreens.containsKey(screenName);
    }
}
