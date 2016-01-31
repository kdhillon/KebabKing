package com.kebabking.game.Managers;

import com.badlogic.gdx.files.FileHandle;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Kyle on 11/10/2015.
 */
public interface FileManager {

    public OutputStream getOutputStream() throws FileNotFoundException;

    public InputStream getInputStream() throws FileNotFoundException;

    public boolean saveFileExists();

    public void deleteProfile();

    public FileHandle getTempOutputHandle() throws FileNotFoundException;

    public InputStream getTempInputStream() throws FileNotFoundException;
}