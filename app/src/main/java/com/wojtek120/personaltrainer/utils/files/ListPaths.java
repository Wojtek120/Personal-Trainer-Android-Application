package com.wojtek120.personaltrainer.utils.files;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.util.ArrayList;

@EBean(scope = EBean.Scope.Singleton)
public class ListPaths {

    /**
     * Returns paths to directories which are in given path
     *
     * @param path - path where to look for directories
     * @return list with paths of directories
     */
    public ArrayList<String> getPathsToDirectoriesFromPath(String path) {

        ArrayList<String> pathsToDirectories = new ArrayList<>();
        File file = new File(path);

        for (File fileFromList : file.listFiles()) {
            if (fileFromList.isDirectory()) {
                pathsToDirectories.add(fileFromList.getAbsolutePath());
            }
        }

        return pathsToDirectories;
    }


    /**
     * Returns paths to files which are in given path
     *
     * @param path - path where to look for files
     * @return list with paths of files
     */
    public ArrayList<String> getPathsToFilesFromPath(String path) {

        ArrayList<String> pathsToFiles = new ArrayList<>();
        File file = new File(path);


        for (File fileFromList : file.listFiles()) {
            if (fileFromList.isFile() && isFileImage(fileFromList.getAbsolutePath())) {
                pathsToFiles.add(fileFromList.getAbsolutePath());
            }
        }

        return pathsToFiles;
    }


    /**
     * Checks if file is image
     *
     * @param path - path to file
     * @return true if is image
     */
    private boolean isFileImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        return options.outWidth != -1 && options.outHeight != -1;
    }

}
