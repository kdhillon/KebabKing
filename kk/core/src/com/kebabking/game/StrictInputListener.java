package com.kebabking.game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class StrictInputListener extends InputListener {
	boolean inside;

	@Override
	public void enter (InputEvent event, float x, float y, int pointer, Actor toActor) {
		inside = true;
	}

	@Override
	public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
		inside = false;
	}
	
	public boolean touchDown(InputEvent event, float x,	float y, int pointer, int button) {
		return true;
	}
	
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		if (shouldCancel()) return;
		SoundManager.playButtonClick();
		touch(event);
	}
	
	// override this shit
	public void touch(InputEvent event) {
		
	}

	public boolean shouldCancel() {
		return !inside;
	}
}
