package libgdxscreencontrol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import libgdxscreencontrol.screen.IChoiceScreen;
import libgdxscreencontrol.screen.IChoiceScreen.IChoiceScreenFactory;

public class ChoiceScreenControllerTest {

    private ChoiceScreenController csController;
    @Mock
    private IChoiceScreen s1, s2, s3;
    @Mock
    private IChoiceScreenFactory<IChoiceScreen> f1, f2, f3;    

    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);
	csController = new ChoiceScreenController();
    }

    @Test
    public void testAddedScreenContainedInController() {
	csController.add("screen1", s1);
	assertTrue(csController.has("screen1"));
    }

    @Test
    public void testAddedScreenFactoryContainedInController() {
	csController.add("screen1", f1);
	assertTrue(csController.has("screen1"));
    }

    @Test
    public void testHasReturnsFalseOnNull() {
	assertFalse(csController.has(null));
    }

    @Test
    public void testCanGetScreenPreviouslyAdded() {
	csController.add("screen1", s1);
	assertTrue(s1 == csController.get("screen1"));
    }

    @Test
    public void testGetCreatesScreenWithFactory() {
	csController.add("screen1", f1);
	Mockito.when(f1.create()).thenReturn(s3);
	assertTrue(s3 == csController.get("screen1"));
    }

    @Test
    public void testHasReturnsFalseOnNameNeverAdded() {
	assertFalse(csController.has("not-a-screen"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetThrowsCorrectExceptionOnNonexistantScreen() {
	csController.get("not-a-screen");
    }

    @Test
    public void testCanGetChoiceNamePreviouslySet() {
	csController.add("screen1", s1);	
	csController.setChoice("screen1", 1, "screen2");
	assertEquals("screen2", csController.getChoice("screen1", 1));
    }

    @Test(expected=IllegalArgumentException.class)    
    public void testCorrectExceptionThrownOnChoiceNotSet() {
	csController.add("screen1", s1);	
	csController.setChoice("screen1", 1, "screen2");
	csController.getChoice("screen1", 2);
    }

    @Test(expected=IllegalArgumentException.class)    
    public void testCorrectExceptionThrownOnGetChoiceWithScreenNotAdded() {
	csController.add("screen1", s1);	
	csController.setChoice("screen1", 1, "screen2");
	csController.getChoice("screen2", 2);
    }
    
}
