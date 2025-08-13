package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

// Scans directories for files to organize
public class FileScanner
{
    public static List<Path> scanDirectoryRecursive(Path folderPath)
    {
        Collection<File> files = FileUtils.listFiles(folderPath.toFile(), TrueFileFilter.INSTANCE, Constants.DIRECTORY_BLACKLIST);

        List<Path> fileList = new ArrayList<>();
        for (File file : files) fileList.add(file.toPath());

        return fileList;
    }

    public static List<Path> scanDirectoryShallow(Path folderPath)
    {
        Collection<File> files = FileUtils.listFiles(folderPath.toFile(), TrueFileFilter.INSTANCE, null); // no recursion

        List<Path> fileList = new ArrayList<>();
        for (File file : files) fileList.add(file.toPath());

        return fileList;
    }

    public static List<Path> scanSubfolders(Path folderPath)
    {
        Collection<File> subfolders = FileUtils.listFilesAndDirs(folderPath.toFile(), FalseFileFilter.FALSE, DirectoryFileFilter.DIRECTORY);

        List<Path> subfoldersList = new ArrayList<>();
        for (File subFolder : subfolders) subfoldersList.add(subFolder.toPath());

        // Sort subFoldersList by deepest first for safe and easy deletion of empty subfolders.
        subfoldersList.sort(Comparator.comparingInt(Path::getNameCount).reversed());
        subfoldersList.remove(folderPath); // Remove root folder from subfoldersList.

        return subfoldersList;
    }
}