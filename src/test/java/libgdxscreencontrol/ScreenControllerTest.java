package libgdxscreencontrol;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.IChoiceScreen.IChoiceScreenFactory;
import libgdxscreencontrol.screen.ITransitionScreen;
import libgdxscreencontrol.screen.ITransitionScreen.ITransitionScreenFactory;

public class ScreenControllerTest {

    private ScreenController controller;
    @Mock
    private TransitionScreenController tc;
    @Mock
    private ChoiceScreenController cc;
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
	when(tc.get("loading-screen")).thenReturn(t1);
	when(tc.has("loading-screen")).thenReturn(true);
	when(cc.get("menu-screen")).thenReturn(c1);
	when(cc.has("menu-screen")).thenReturn(true);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCorrectExceptionThrownWithStartingScreenNotRegistered() {
	controller = new ScreenController(tc, cc, "not-a-screen");
    }

    @Test
    public void testCanCreateScreenControllerWithTransitionScreenAsStartingScreen() {
	controller = new ScreenController(tc, cc, "loading-screen");	
    }

    @Test
    public void testCanCreateScreenControllerWithChocieScreenAsStartingScreen() {
	controller = new ScreenController(tc, cc, "menu-screen");	
    }

    @Test
    public void testGetReturnsStartingTransitionScreen() {
	controller = new ScreenController(tc, cc, "loading-screen");	
	assertTrue(t1 == controller.get());
    }

    @Test
    public void testGetReturnsStartingChoiceScreen() {
	controller = new ScreenController(tc, cc, "menu-screen");
	assertTrue(c1 == controller.get());
    }

    @Test
    public void testUpdateDoesNotChangeCurrentTransitionScreenWhenScreenNotFinished() {
	controller = new ScreenController(tc, cc, "loading-screen");
	controller.update();
	assertTrue(t1 == controller.get());	
    }

    @Test
    public void testUpdateDoesNotChangeCurrentChoiceScreenWhenScreenNotFinished() {
	controller = new ScreenController(tc, cc, "menu-screen");
	controller.update();
	assertTrue(c1 == controller.get());
    }

    @Test
    public void testUpdateUpdatesScreenWhenTransitionScreenFinished() {
	controller = new ScreenController(tc, cc, "loading-screen");
	when(t1.isFinished()).thenReturn(true);
	when(tc.getTransition("loading-screen")).thenReturn("screen2");
	when(tc.get("screen2")).thenReturn(t2);
	when(tc.has("screen2")).thenReturn(true);
	controller.update();
	assertTrue(t2 == controller.get());
    }

    @Test(expected=IllegalStateException.class)
    public void testCorrectErrorThrownOnUpdateWithUnregisteredSucceedingScreen() {
	controller = new ScreenController(tc, cc, "loading-screen");
	when(t1.isFinished()).thenReturn(true);
	when(tc.getTransition("loading-screen")).thenReturn("screen2");
	controller.update();
    }

    @Test
    public void testUpdateUpdatesToTransitionScreenWhenChoiceScreenFinished() {
	controller = new ScreenController(tc, cc, "menu-screen");
	when(c1.isFinished()).thenReturn(true);
	when(c1.getChoice()).thenReturn(0);
	when(cc.getChoice("menu-screen", 0)).thenReturn("screen2");
	when(tc.get("screen2")).thenReturn(t2);
	when(tc.has("screen2")).thenReturn(true);
	controller.update();
	assertTrue(t2 == controller.get());
    }

    @Test
    public void testUpdateUpdatesToChoiceScreenWhenChoiceScreenFinished() {
	controller = new ScreenController(tc, cc, "menu-screen");
	when(c1.isFinished()).thenReturn(true);
	when(c1.getChoice()).thenReturn(0);
	when(cc.getChoice("menu-screen", 0)).thenReturn("player-screen");
	when(cc.get("player-screen")).thenReturn(c2);
	when(cc.has("player-screen")).thenReturn(true);
	controller.update();
	assertTrue(c2 == controller.get());
    }
    
    @Test
    public void testMenuScreenWithMultipleChoices() {
	controller = new ScreenController(tc, cc, "menu-screen");
	when(c1.isFinished()).thenReturn(true);
	when(c1.getChoice()).thenReturn(1);
	when(cc.getChoice("menu-screen", 0)).thenReturn("player-screen");
	when(cc.getChoice("menu-screen", 1)).thenReturn("options-screen");
	when(cc.get("player-screen")).thenReturn(c2);
	when(cc.has("player-screen")).thenReturn(true);
	when(tc.get("options-screen")).thenReturn(t2);
	when(tc.has("options-screen")).thenReturn(true);
	controller.update();
	assertTrue(t2 == controller.get());
    }

    @Test
    public void testAllUsedScreensDisposed() {
	controller = new ScreenController(tc, cc, "menu-screen");
	when(c1.isFinished()).thenReturn(true);
	when(c1.getChoice()).thenReturn(0);
	when(cc.getChoice("menu-screen", 0)).thenReturn("player-screen");
	when(tc.get("player-screen")).thenReturn(t1);
	when(tc.has("player-screen")).thenReturn(true);
	controller.update();

	when(tc.get("game-screen")).thenReturn(t2);
	when(tc.has("game-screen")).thenReturn(true);	
	when(tc.getTransition("player-screen")).thenReturn("game-screen");
	when(t1.isFinished()).thenReturn(true);
	controller.update();

	controller.dispose();
	verify(c1, times(1)).dispose();
	verify(t1, times(1)).dispose();
	verify(t2, times(1)).dispose();
    }

    @Test
    public void testResetCalledOnAlreadyUsedTransitionScreen() {
	controller = new ScreenController(tc, cc, "loading-screen");
	when(tc.get("loading-screen")).thenReturn(t1);
	when(tc.has("loading-screen")).thenReturn(true);
	when(tc.get("game-screen")).thenReturn(t2);
	when(tc.has("game-screen")).thenReturn(true);	
	when(tc.getTransition("loading-screen")).thenReturn("game-screen");
	when(t1.isFinished()).thenReturn(true);
	controller.update();

	when(tc.getTransition("game-screen")).thenReturn("loading-screen");
	when(t2.isFinished()).thenReturn(true);
	controller.update();
	verify(t1, times(1)).reset();
    }

    @Test
    public void testActiveScreenNotChangedAfterUpdateException() {
	controller = new ScreenController(tc, cc, "loading-screen");
	when(tc.get("loading-screen")).thenReturn(t1);
	when(tc.has("loading-screen")).thenReturn(true);
	when(tc.getTransition("loading-screen")).thenReturn("game-screen");
	try {
	    controller.update();
	} catch (IllegalArgumentException e) {
	    assertTrue(t1 == controller.get());
	}
    }

}
