package libgdxscreencontrol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.IChoiceScreen.IChoiceScreenFactory;
import libgdxscreencontrol.screen.ITransitionScreen;
import libgdxscreencontrol.screen.ITransitionScreen.ITransitionScreenFactory;

public class ScreenControllerBuilderTest {

    private ScreenControllerBuilder sc;
    @Mock
    private ITransitionScreen t1, t2;
    @Mock
    private IChoiceScreen c1, c2;
    @Mock
    private ITransitionScreenFactory<?> tf1, tf2;
    @Mock
    private IChoiceScreenFactory<?> cf1, cf2;

    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);
	sc = new ScreenControllerBuilder();
    }

    @Test
    public void testBuilderHasChoiceScreenPreviouslyAdded() {
	sc.register("screen1", c1);
	assertTrue(sc.isScreenRegistered("screen1"));
    }

    @Test
    public void testBuilderHasTransitionScreenPreviouslyAdded() {
	sc.register("screen1", t1);
	assertTrue(sc.isScreenRegistered("screen1"));
    }    

    @Test
    public void testBuilderHasChoiceScreenPreviouslyAddedViaFactory() {
	sc.register("screen1", cf1);
	assertTrue(sc.isScreenRegistered("screen1"));
    }

    @Test
    public void testBuilderHasTransitionScreenPreviouslyAddedViaFactory() {
	sc.register("screen1", tf1);
	assertTrue(sc.isScreenRegistered("screen1"));
    }        

    @Test
    public void testIsRegisteredReturnsFalseOnScreenNotAdded() {
	sc.register("screen1", c1);
	assertFalse(sc.isScreenRegistered("screen2"));
    }

    @Test
    public void testCanRegisterStartingScreen() {
	sc.register("screen1", c1);
	sc.withStartingScreen("screen1");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrectExceptionThrownOnStartingScreenNotRegistered() {
	sc.register("screen1", c1);
	sc.withStartingScreen("screen2");
    }

    @Test
    public void testCanFollowTransitionScreenWithTransitionScreen() {
	sc.register("screen1", t1);
	sc.register("screen2", t2);
	sc.setSuccession("screen1", "screen2");
    }

    @Test
    public void testCanFollowTransitionScreenWithChoiceScreen() {
	sc.register("screen1", t1);
	sc.register("choice", c2);
	sc.setSuccession("screen1", "choice");
    }

    @Test
    public void testCanFollowScreenWithScreenNotRegistered() {
	sc.register("screen1", t1);
	sc.setSuccession("screen1", "screen2");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrectExceptionThrownOnInitialScreenNotRegistered() {
	sc.register("screen1", t1)
	    .setSuccession("screen2", "screen1");
    }

    @Test
    public void testCanGetScreenRegsitered() {
	sc.register("screen1", new MyChoiceScreen());
	MyChoiceScreen myChoiceScreen = sc.get("screen1", MyChoiceScreen.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrectExceptionThrownOnGetWithWrongType() {
	sc.register("screen1", new MyChoiceScreen());
	MyTransitionScreen myTransitionScreen = sc.get(
	    "screen1", MyTransitionScreen.class
	);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrectExceptionThrownOnGetWithScreenNotRegsitered() {
	sc.register("screen1", new MyChoiceScreen());
	MyTransitionScreen myTransitionScreen = sc.get(
	    "screen2", MyTransitionScreen.class
	);
    }

    @Test
    public void testCanAddChoiceNotRegistered() {
	sc.register("screen1", c1)
	    .choice("screen1", "screen2", 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrectExceptionThrownWithScreenNotAChoiceScreen() {
	sc.register("screen1", t1)
	    .choice("screen1", "screen2", 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrectExceptionThrownWithScreenNotRegistered() {
	sc.register("screen1", t1)
	    .choice("screen2", "screen1", 1);
    }

    @Test(expected=IllegalStateException.class)
    public void testCorrectExceptionThrownWhenStartingScreenNotSet() {
	ScreenController controller = new ScreenControllerBuilder()
	    .register("my cool screen", t1)
	    .register("my other screen", c1)
	    .setSuccession("my cool screen", "my other screen")
	    .build();
    }

    @Test
    public void testCanCreateControllerWhenStartingScreenSet() {
	ScreenController controller = new ScreenControllerBuilder()
	    .register("my cool screen", t1)
	    .register("my other screen", c1)
	    .withStartingScreen("my cool screen")
	    .setSuccession("my cool screen", "my other screen")
	    .build();
    }
    
}
