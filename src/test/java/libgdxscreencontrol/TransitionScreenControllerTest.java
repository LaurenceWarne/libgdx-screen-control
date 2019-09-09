package libgdxscreencontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import libgdxscreencontrol.screen.ITransitionScreen;
import libgdxscreencontrol.screen.ITransitionScreen.ITransitionScreenFactory;

public class TransitionScreenControllerTest {

    private TransitionScreenController tsController;
    @Mock
    private ITransitionScreen s1, s2, s3;
    @Mock
    private ITransitionScreenFactory<ITransitionScreen> f1, f2, f3;    

    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);
	tsController = new TransitionScreenController();
    }

    @Test
    public void testAddedScreenContainedInController() {
	tsController.add("screen1", s1);
	assertTrue(tsController.has("screen1"));
    }

    @Test
    public void testAddedScreenFactoryContainedInController() {
	tsController.add("screen1", f1);
	assertTrue(tsController.has("screen1"));
    }

    @Test
    public void testHasReturnsFalseOnNull() {
	assertFalse(tsController.has(null));
    }

    @Test
    public void testCanGetScreenPreviouslyAdded() {
	tsController.add("screen1", s1);
	assertTrue(s1 == tsController.get("screen1"));
    }

    @Test
    public void testGetCreatesScreenWithFactory() {
	tsController.add("screen1", f1);
	Mockito.when(f1.create()).thenReturn(s3);
	assertTrue(s3 == tsController.get("screen1"));
    }

    @Test
    public void testHasReturnsFalseOnNameNeverAdded() {
	assertFalse(tsController.has("not-a-screen"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetThrowsCorrectExceptionOnNonexistantScreen() {
	tsController.get("not-a-screen");
    }

    @Test
    public void testCanGetNextScreenPreviouslySet() {
	tsController.add("screen1", s1);	
	tsController.setTransition("screen1", "screen2");
	assertEquals("screen2", tsController.getTransition("screen1"));
    }


    @Test(expected=IllegalArgumentException.class)    
    public void testCorrectExceptionThrownOnGetTransitionWithScreenNotAdded() {
	tsController.add("screen1", s1);	
	tsController.setTransition("screen1", "screen2");
	tsController.getTransition("screen2");
    }
    
}
