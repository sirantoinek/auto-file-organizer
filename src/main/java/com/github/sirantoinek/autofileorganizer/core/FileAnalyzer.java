package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.apache.commons.io.FilenameUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;

// Handles the retrieval of file info used in organization
public class FileAnalyzer
{
    // Returns the folder a file should belong to if sorted by type
    public static String getFileCategory(Path file)
    {
        String extension = FilenameUtils.getExtension(file.toString());

        // contains() runs in avg O(1) time complexity since these sets are hash-based
        if (Constants.DOCUMENT_EXTENSIONS.contains(extension)) return Constants.DOCUMENTS_FOLDER;
        else if (Constants.IMAGE_EXTENSIONS.contains(extension)) return Constants.IMAGES_FOLDER;
        else if (Constants.VIDEO_EXTENSIONS.contains(extension)) return Constants.VIDEOS_FOLDER;
        else if (Constants.AUDIO_EXTENSIONS.contains(extension)) return Constants.AUDIO_FOLDER;
        else if (Constants.ARCHIVE_EXTENSIONS.contains(extension)) return Constants.ARCHIVES_FOLDER;
        else if (Constants.EXECUTABLE_EXTENSIONS.contains(extension)) return Constants.EXECUTABLES_FOLDER;
        else return Constants.MISC_FOLDER;
    }

    // Returns file last modified date in "yyyy-MM" format
    public static String getFileModifyDate(Path file) throws IOException
    {
        Instant instant = Files.getLastModifiedTime(file).toInstant();
        return Constants.DATE_FORMAT.format(instant.atZone(ZoneId.systemDefault()).toLocalDate());
    }
}