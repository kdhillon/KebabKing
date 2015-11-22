package com.chuanrchef.game.Managers;

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
}
