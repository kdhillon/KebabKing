package com.chuanrchef.game.Managers;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.chuanrchef.game.ChuanrC;

public class FileManagerDesktop implements FileManager {

	@Override
	public OutputStream getOutputStream() throws FileNotFoundException {
		FileHandle file = Gdx.files.local(ChuanrC.SAVE_FILENAME);
		return file.write(false);
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		FileHandle file = Gdx.files.local(ChuanrC.SAVE_FILENAME);
		return file.read();
	}

	@Override
	public boolean saveFileExists() {
		return Gdx.files.local(ChuanrC.SAVE_FILENAME).exists();
	}

	@Override
	public void deleteProfile() {
		 Gdx.files.local(ChuanrC.SAVE_FILENAME).delete();
	}
}
