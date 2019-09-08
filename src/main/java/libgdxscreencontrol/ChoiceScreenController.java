package libgdxscreencontrol;

import javax.annotation.Nullable;

import com.badlogic.gdx.utils.ObjectMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.IChoiceScreen.IChoiceScreenFactory;
import lombok.NonNull;

public class ChoiceScreenController {

    @NonNull
    private final ObjectMap<String, IChoiceScreen> choiceScreens =
	new ObjectMap<>();
    @NonNull
    private final ObjectMap<String, IChoiceScreenFactory<?>> choiceScreenFactories =
	new ObjectMap<>();
    @NonNull
    private final Table<String, Integer, String> choiceMap = HashBasedTable.create();

    /**
     * Add the specified screen with the specified name to this controller.
     * 
     * @param name name of the screen
     * @param screen
     */
    public void add(
	@NonNull final String name, @NonNull final IChoiceScreen screen
    ) {
	choiceScreens.put(name, screen);
    }

    /**
     * Add the specified screen factory with the specified name to this controller.
     * 
     * @param name name of the screen
     * @param factory
     */    
    public void add(
	@NonNull final String name, @NonNull final IChoiceScreenFactory<?> factory
    ) {
	choiceScreenFactories.put(name, factory);
    }

    /**
     *
     */
    public String getNameOfChoice(
	@NonNull final String choiceScreenName, final int choice
    ) {
	if (choiceMap.contains(choiceScreenName, choice)) {
	    return choiceMap.get(choiceScreenName, choice);
	} else {
	    throw new IllegalArgumentException(
		"Choice " + choice + " does not exist for screen: " + choiceScreenName
	    );
	}
    }

    public boolean has(@Nullable final String name) {
	if (name == null) {
	    return false;
	}
	else if (choiceScreens.containsKey(name) ||
		 choiceScreenFactories.containsKey(name)){
	    return true;
	}
	else {
	    return false;
	}
    }

    public IChoiceScreen get(@NonNull final String name) {
	if (choiceScreens.containsKey(name)) {
	    return choiceScreens.get(name);
	}
	else if (choiceScreenFactories.containsKey(name)) {
	    final IChoiceScreen screen = choiceScreenFactories.get(name).create();
	    choiceScreens.put(name, screen);
	    choiceScreenFactories.remove(name);
	    return screen;
	}
	else {
	    throw new IllegalArgumentException("No screen exists with name: " + name);
	}
    }

    public void setChoice(
	@NonNull final String choiceScreenName,
	final int choice,
	@NonNull final String choiceName
    ) {
	choiceMap.put(choiceScreenName, choice, choiceName);
    }
}
