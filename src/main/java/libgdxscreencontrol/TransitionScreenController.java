package libgdxscreencontrol;

import javax.annotation.Nullable;

import com.badlogic.gdx.utils.ObjectMap;

import libgdxscreencontrol.screen.ITransitionScreen;
import libgdxscreencontrol.screen.ITransitionScreen.ITransitionScreenFactory;
import lombok.NonNull;

/**
 * Manages {@link ITransitionScreen}s.
 */
public class TransitionScreenController {

    @NonNull
    private final ObjectMap<String, ITransitionScreen> transitionScreens =
	new ObjectMap<>();
    @NonNull
    private final ObjectMap<String, ITransitionScreenFactory<?>>
	transitionScreenFactories = new ObjectMap<>();
    @NonNull
    private final ObjectMap<String, String> transitionMap = new ObjectMap<>();

    /**
     * Add the specified screen with the specified name to this controller.
     * 
     * @param name name of the screen
     * @param screen
     */
    public void add(
	@NonNull final String name, @NonNull final ITransitionScreen screen
    ) {
	transitionScreens.put(name, screen);
    }

    /**
     * Add the specified screen factory with the specified name to this controller.
     * 
     * @param name name of the screen
     * @param factory
     */    
    public void add(
	@NonNull final String name, @NonNull final ITransitionScreenFactory<?> factory
    ) {
	transitionScreenFactories.put(name, factory);
    }

    /**
     * Get the name of the screen which succeeds the specified {@link ITransitionScreen}
     *
     * @param transitionScreenName the string associated with the {@link ITransitionScreen}
     * @return name of screen succeeding the transition screen
     * @throws IllegalArgumentException if the transition has not yet been assigned or a screen with the specified name is not registered with this object.
     */
    public String getTransition(
	@NonNull final String transitionScreenName
    ) throws IllegalArgumentException {
	if (transitionMap.containsKey(transitionScreenName)) {
	    return transitionMap.get(transitionScreenName);
	} else {
	    throw new IllegalArgumentException(
		"A screen with name: " + transitionScreenName +
		" has not been added to this object"
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
	else if (transitionScreens.containsKey(name) ||
		 transitionScreenFactories.containsKey(name)){
	    return true;
	}
	else {
	    return false;
	}
    }

    /**
     * Get the {@link ITransitionScreen} object registered with the specified name.
     *
     * @param name
     * @return screen registered with name
     * @throws IllegalArgumentException if no screen is registered with the given name
     */
    public ITransitionScreen get(@NonNull final String name) {
	if (transitionScreens.containsKey(name)) {
	    return transitionScreens.get(name);
	}
	else if (transitionScreenFactories.containsKey(name)) {
	    final ITransitionScreen screen = transitionScreenFactories.get(name).create();
	    transitionScreens.put(name, screen);
	    transitionScreenFactories.remove(name);
	    return screen;
	}
	else {
	    throw new IllegalArgumentException("No screen exists with name: " + name);
	}
    }

    /**
     * Set the screen to follow the specified {@link ITransitionScreen}.
     *
     * @param transitionScreenName
     * @param followingScreenName
     * @throws IllegalArgumentException if transitionScreenName does not belong to any screen added to this object
     */
    public void setTransition(
	@NonNull final String transitionScreenName,
	@NonNull final String followingScreenName
    ) throws IllegalArgumentException {
	if (has(transitionScreenName)) {
	    transitionMap.put(transitionScreenName, followingScreenName);
	}
	else {
	    throw new IllegalArgumentException(
		"No screen exists with name: " + transitionScreenName
	    );
	}
    }
}
