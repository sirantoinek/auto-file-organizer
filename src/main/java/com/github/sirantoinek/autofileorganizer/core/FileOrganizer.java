package com.github.sirantoinek.autofileorganizer.core;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;
import java.nio.file.*;
import java.time.YearMonth;
import java.util.List;

// Handles file organization using FileAnalyzer, FileScanner, and flags set in the CLI.
public class FileOrganizer
{
    public static int organizeDirectory(String folderPathStr, boolean byType, boolean byDate, boolean recursive) throws IOException
    {
        Path sourceDir = Paths.get(folderPathStr);
        // folderPathStr validity check.
        if (!Files.exists(sourceDir) || !Files.isDirectory(sourceDir))
        {
            throw new IllegalArgumentException("Source path must be a valid directory");
        }

        // Scan files to be organized. Shallow is default unless --recursive flag was set.
        List<Path> files = recursive ? FileScanner.scanDirectoryRecursive(sourceDir) : FileScanner.scanDirectoryShallow(sourceDir);

        // Create organized folder.
        Path organizedDir = sourceDir.resolve("Organized");
        Files.createDirectories(organizedDir);

        int filesOrganized = 0;
        for (Path file : files)
        {
            try
            {
                // If a file is successfully organized, increment filesOrganized.
                if (organizeFile(file, organizedDir, byType, byDate)) filesOrganized++;
            }
            catch (Exception e)
            {
                System.err.println("Failed to organize \"" + file + "\": " + e.getMessage());
            }
        }

        // Scan all subfolders within organizedDir for deletion of empty subfolders (keeps organizedDir clean).
        List<Path> subfolders = FileScanner.scanSubfolders(organizedDir);

        for (Path subFolder : subfolders)
        {
            try
            {
                if (FileUtils.isEmptyDirectory(subFolder.toFile())) FileUtils.deleteDirectory(subFolder.toFile());
            }
            catch (Exception e)
            {
                System.err.println("Failed to delete empty folder \"" + subFolder + "\" from ...\\Organized: " + e.getMessage());
            }
        }

        return filesOrganized;
    }

    private static boolean organizeFile(Path file, Path organizedDir, boolean byType, boolean byDate) throws IOException
    {
        Path organizedPath = findOrganizedPath(file, organizedDir, byType, byDate);

        // file is already properly organized. Return false and do not move the file or increment filesOrganized.
        if (organizedPath.toString().equals(file.toString())) return false;

        // Create file's organized parent folders if they do not already exist.
        Files.createDirectories(organizedPath.getParent());

        // Handle duplicate file names.
        if (Files.exists(organizedPath))
        {
            String baseName = FilenameUtils.getBaseName(organizedPath.toString());
            String extension = FilenameUtils.getExtension(organizedPath.toString());
            int i = 1;

            do
            {
                organizedPath = organizedPath.getParent().resolve(baseName + " (" + i + ")." + extension);
                i++;
            }
            while (Files.exists(organizedPath)); // "i" is incremented in " (i)" after the baseName until a unique name is found.
        }

        Files.move(file, organizedPath);
        return true;
    }

    // Finds and returns organized path for a file depending on set flags.
    private static Path findOrganizedPath(Path file, Path organizedDir, boolean byType, boolean byDate) throws IOException
    {
        // organizedDir represents the path ".../Organized/".
        Path targetDir = organizedDir;

        // If both "--by-Type" and "--by-Date" flags are set, the file will be organized as ".../yyyy/MM/fileCategory/file".
        if (byDate)
        {
            YearMonth modifyDate = YearMonth.parse(FileAnalyzer.getFileModifyDate(file));
            String year = String.format("%04d", modifyDate.getYear());
            String month = String.format("%02d", modifyDate.getMonthValue());

            targetDir = targetDir.resolve(year).resolve(month);
        }

        if (byType)
        {
            String fileCategory = FileAnalyzer.getFileCategory(file);
            targetDir = targetDir.resolve(fileCategory);
        }

        return targetDir.resolve(file.getFileName());
    }
}