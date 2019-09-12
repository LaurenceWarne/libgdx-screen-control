# libgdx Screen Control
[![Build Status](https://travis-ci.org/LaurenceWarne/libgdx-screen-control.svg?branch=master)](https://travis-ci.org/LaurenceWarne/libgdx-screen-control)

libgdx-screen-control is library intended to ease the management of libgdx ```Screen```s.

## Usage

### Quickstart

First we need to create a ```ScreenController``` instance:

```java
final ScreenController screenController = new ScreenControllerbuilder()
	// Register screens with string identifiers
	.register("loading-screen", new MyLoadingScreen())
	.register("menu-screen", new MyMenuScreen())
	.register("options-screen", new MyOptionsScreen())
	.register("game-screen", new MyGameScreen())
	// Organise the screen heirachy from the registered screens
	.withStartingScreen("loading-screen")
	.setSuccession("loading-screen", "menu-screen")  // menu screen comes after loading screen
	.choice("menu-screen", "game-screen", 0)         // Set first option of the menu screen
	.choice("menu-screen", "options-screen", 1)      // Set second option of the menu screen
	.setSuccession("options-screen", "menu-screen")  // Loop back to menu screen
	.build();

```

In our render loop:

```java
...
if (screenController.update()) {  // Returns true iff screen has changed
	activeScreen = screenController.get();
}
activeScreen.render();
...
```

### Screens

```ScreenController``` recognises two kinds of screens:

```ITransitionScreen```s: Use if the screen in question will always be followed by the same screen, e.g. the loading screen above will always be followed by the main menu screen, once all the game assets have been loaded.

```java
public class MyLoadingScreen extends ScreenAdapter implements ITransitionScreen {

	...

	@Override
	public boolean isFinished() {
		return gameAssetManager().isFinishedLoading();
	}

	...
}
```

```IChoiceScreen```s: Use if the screen can be followed by different screens, e.g. the main menu screen can be followed by a game screen or an options screen according to user input.

```java
public class MyMenuScreen extends ScreenAdapter implements IChoiceScreen {

	...
	
	@Override
	public boolean isFinished() {  // IChoiceScreen is an extension of the ITransitionScreen interface
		return hasUserClickedAButton();
	}
	
	@Override
	public int getChoice() {
		if (playButton.isPressed()) {
			return 0;
		}
		else if (optionsButton.isPressed()) {
			return 1;
		}
		else {
			return -1;  // No button has been pressed
		}
	}
	
	...
}
```

### Screen Dependencies

Often our screens are dependent on objects not created until certain screens have finished processing, we address this with use of factories:

```java
final ScreenControllerBuilder screenControllerBuilder = new ScreenControllerbuilder();
final ScreenController = screenControllerBuilder
	// Register screens with string identifiers
	.register("loading-screen", new MyLoadingScreen())
	.register("game-screen", new ITransitionScreenFactory<MyGameScreen>() {
		@Override
		public MyGameScreen create() {
			return new MyGameScreen(
				screenControllerBuilder.get("loading-screen", MyLoadingScreen.class).getMyAsset()
			);
		}
	})
	...
	.setSuccession("loading-screen", "game-screen")
	...
	.build();

```

```.create()``` is called when the screen is first set as the active screen of the ```ScreenController``` (ie the return of ```.get()```), the created instance is used thereafter.


## Getting Started

### Gradle

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
	implementation 'com.github.LaurenceWarne:libgdx-screen-control:v1.0'
}
```
