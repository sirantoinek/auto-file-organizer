package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Scans directories for files to organize
public class FileScanner
{
    public static List<Path> scanDirectoryRecursive(Path folderPath)
    {
        File directory = folderPath.toFile();
        Collection<File> files = FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, Constants.DIRECTORY_BLACKLIST);

        List<Path> fileList = new ArrayList<>();
        for (File file : files) fileList.add(file.toPath());

        return fileList;
    }

    public static List<Path> scanDirectoryShallow(Path folderPath)
    {
        File directory = folderPath.toFile();
        Collection<File> files = FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, null); // no recursion

        List<Path> fileList = new ArrayList<>();
        for (File file : files) fileList.add(file.toPath());

        return fileList;
    }
}