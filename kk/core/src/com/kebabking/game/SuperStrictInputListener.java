package com.kebabking.game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class SuperStrictInputListener extends InputListener {
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

	public void touchUp(InputEvent event, float x, float y,	int pointer, int button) {
		// only do this if finger still within bounds
		if (x < event.getListenerActor().getX()
				|| y < event.getListenerActor().getY()
				|| x > event.getListenerActor().getX() + event.getListenerActor().getWidth()
				|| y > event.getListenerActor().getY() + event.getListenerActor().getHeight()) {
//			KebabKing.print("x " + x + " y " + y + " getx " + event.getListenerActor().getX() + " get y " + event.getListenerActor().getY());
			return;
		}
		if (shouldCancel()) return;
		touch(event);
	}
	
	public void touch(InputEvent event) {
		// override this
	}
	
	public boolean shouldCancel() {
		return !inside;
	}
}
