package com.chuanrchef.game;

import com.badlogic.gdx.Screen;

public class ScreenTemplate implements Screen {

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("show(): " + this.getClass().toString());
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		System.out.println("hide(): " + this.getClass().toString());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		System.out.println("pause(): " + this.getClass().toString());
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		System.out.println("resume(): " + this.getClass().toString());
	}

	@Override
	public void dispose() {
		System.out.println("dispose(): " + this.getClass().toString());

		System.out.println("deleting temp resources");
		Assets.deleteTempResources();
	}
}
