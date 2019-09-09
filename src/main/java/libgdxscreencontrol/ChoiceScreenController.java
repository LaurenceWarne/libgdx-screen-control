package libgdxscreencontrol;

import javax.annotation.Nullable;

import com.badlogic.gdx.utils.ObjectMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.IChoiceScreen.IChoiceScreenFactory;
import lombok.NonNull;

/**
 * Manages {@link IChoiceScreen}s and their choices.
 */
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
     * Get the choice assigned to the specified choiceScreenName.
     *
     * @param choiceScreenName of the screen
     * @param choice
     * @return name of choice
     * @throws IllegalArgumentException if the choice has not yet been assigned or a screen with the specified name is not registered with this object.
     */
    public String getChoice(
	@NonNull final String choiceScreenName, final int choice
    ) throws IllegalArgumentException {
	if (choiceMap.contains(choiceScreenName, choice)) {
	    return choiceMap.get(choiceScreenName, choice);
	} else {
	    throw new IllegalArgumentException(
		"Choice " + choice + " does not exist for screen: " + choiceScreenName
	    );
	}
    }

    /**
     * Return true if the specified name belongs to a screen, else false. False is returned if name is null.
     *
     * @param name
     * @return true if the specified name belongs to a screen, else false
     */
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

    /**
     * Get the {@link IChoiceScreen} object registered with the specified name.
     *
     * @param name
     * @return screen registered with name
     * @throws IllegalArgumentException if no screen is registered with the given name
     */
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

    /**
     * Set a choice of the specified screen.
     *
     * @param choiceScreenName
     * @param choice
     * @param choiceName
     * @throws IllegalArgumentException if choiceScreenName does not belong to any screen added to this object
     */
    public void setChoice(
	@NonNull final String choiceScreenName,
	final int choice,
	@NonNull final String choiceName
    ) throws IllegalArgumentException {
	if (has(choiceScreenName)) {
	    choiceMap.put(choiceScreenName, choice, choiceName);
	}
	else {
	    throw new IllegalArgumentException("No screen exists with name: " + choiceScreenName);
	}
    }
}
