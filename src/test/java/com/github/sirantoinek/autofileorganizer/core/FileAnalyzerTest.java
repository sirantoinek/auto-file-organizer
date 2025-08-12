package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileAnalyzerTest
{
    @Test
    void testGetFileCategoryWithFileNames()
    {
        assertEquals(Constants.DOCUMENTS_FOLDER, FileAnalyzer.getFileCategory(Paths.get("file.docx")));
        assertEquals(Constants.IMAGES_FOLDER, FileAnalyzer.getFileCategory(Paths.get("image.jpeg")));
        assertEquals(Constants.VIDEOS_FOLDER, FileAnalyzer.getFileCategory(Paths.get("video.mp4")));
        assertEquals(Constants.AUDIO_FOLDER, FileAnalyzer.getFileCategory(Paths.get("sound.mp3")));
        assertEquals(Constants.ARCHIVES_FOLDER, FileAnalyzer.getFileCategory(Paths.get("archive.zip")));
        assertEquals(Constants.EXECUTABLES_FOLDER, FileAnalyzer.getFileCategory(Paths.get("program.exe")));
        assertEquals(Constants.MISC_FOLDER, FileAnalyzer.getFileCategory(Paths.get("unknown.xyz")));
    }

    @Test
    void testGetFileCategoryWithFullPaths()
    {
        // Ensure that using full path also works.
        assertEquals(Constants.DOCUMENTS_FOLDER, FileAnalyzer.getFileCategory(Paths.get("C:/downloads/report.docx")));
        assertEquals(Constants.IMAGES_FOLDER, FileAnalyzer.getFileCategory(Paths.get("/home/user/picture.jpeg")));
        assertEquals(Constants.VIDEOS_FOLDER, FileAnalyzer.getFileCategory(Paths.get("/movies/video.mov")));
        assertEquals(Constants.AUDIO_FOLDER, FileAnalyzer.getFileCategory(Paths.get("C:/music/track01.m4a")));
        assertEquals(Constants.ARCHIVES_FOLDER, FileAnalyzer.getFileCategory(Paths.get("C:/Users/User/downloads/backup.7z")));
        assertEquals(Constants.EXECUTABLES_FOLDER, FileAnalyzer.getFileCategory(Paths.get("/files/app.apk")));
        assertEquals(Constants.MISC_FOLDER, FileAnalyzer.getFileCategory(Paths.get("./log/file.xyz")));
    }

    @Test
    void testGetFileModifyDate() throws IOException
    {
        Path tempFile = java.nio.file.Files.createTempFile("test", ".txt");
        try
        {
            // Because date is in middle of month, user timezone will not change expected yyyy-MM result.
            FileTime modifyTime = java.nio.file.attribute.FileTime.fromMillis(1626307200000L); // July 15, 2021
            java.nio.file.Files.setLastModifiedTime(tempFile, modifyTime);

            String modifyDate = FileAnalyzer.getFileModifyDate(tempFile);

            assertEquals("2021-07", modifyDate);

            modifyTime = java.nio.file.attribute.FileTime.fromMillis(1673740800000L); // January 15, 2023
            java.nio.file.Files.setLastModifiedTime(tempFile, modifyTime);

            modifyDate = FileAnalyzer.getFileModifyDate(tempFile);

            assertEquals("2023-01", modifyDate);
        }
        finally
        {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }
}