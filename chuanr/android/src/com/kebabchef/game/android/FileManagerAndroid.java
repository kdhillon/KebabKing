package com.kebabchef.game.android;

import android.content.Context;

import com.chuanrchef.game.ChuanrC;
import com.chuanrchef.game.Managers.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Kyle on 11/10/2015.
 */
public class FileManagerAndroid implements FileManager {
    AndroidLauncher androidLauncher;
    public FileManagerAndroid(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public OutputStream getOutputStream() throws FileNotFoundException {
        Context ctx = androidLauncher.getContext();
        return ctx.openFileOutput(ChuanrC.SAVE_FILENAME, Context.MODE_PRIVATE);
    }

    public InputStream getInputStream() throws FileNotFoundException {
        Context ctx = androidLauncher.getContext();
        return ctx.openFileInput(ChuanrC.SAVE_FILENAME);
    }

    @Override
    public boolean saveFileExists() {
        Context ctx = androidLauncher.getContext();
        File file = ctx.getFileStreamPath(ChuanrC.SAVE_FILENAME);
        if (file == null || !file.exists()) return false;
        return true;
    }

    @Override
    public void deleteProfile() {
        Context ctx = androidLauncher.getContext();
        ctx.deleteFile(ChuanrC.SAVE_FILENAME);
    }
}
