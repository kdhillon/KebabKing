package com.kebabking.game.Managers;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kebabking.game.KebabKing;
import com.kebabking.game.SocialMediaHandler;

public class FileManagerDesktop implements FileManager {

	@Override
	public OutputStream getOutputStream() throws FileNotFoundException {
		FileHandle file = Gdx.files.local(KebabKing.SAVE_FILENAME);
		return file.write(false);
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		FileHandle file = Gdx.files.local(KebabKing.SAVE_FILENAME);
		return file.read();
	}

	@Override
	public boolean saveFileExists() {
		System.out.println("Searching for save file on disk...");
		boolean saveExists = Gdx.files.local(KebabKing.SAVE_FILENAME).exists();
		if (!saveExists) System.out.println("No save found on disk!");
		else System.out.println("Save found on disk!");
		return saveExists;
	}

	@Override
	public void deleteProfile() {
		 Gdx.files.local(KebabKing.SAVE_FILENAME).delete();
	}

	@Override
	public FileHandle getTempOutputHandle() throws FileNotFoundException {
		FileHandle file = Gdx.files.local(SocialMediaHandler.FILENAME);
		return file;
	}

	@Override
	public InputStream getTempInputStream() throws FileNotFoundException {
		FileHandle file = Gdx.files.local(SocialMediaHandler.FILENAME);
		return file.read();
	}
}
