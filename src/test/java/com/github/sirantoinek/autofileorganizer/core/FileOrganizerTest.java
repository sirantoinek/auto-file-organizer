package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class FileOrganizerTest
{
    @TempDir
    Path tempDir;

    @Test
    void testOrganizeByTypeShallow() throws IOException
    {
        Files.createFile(tempDir.resolve("document.pdf"));
        Files.createFile(tempDir.resolve("image.jpg"));
        Files.createFile(tempDir.resolve("video.mp4"));
        Files.createFile(tempDir.resolve("song.mp3"));
        Files.createFile(tempDir.resolve("archive.zip"));
        Files.createFile(tempDir.resolve("app.exe"));
        Files.createFile(tempDir.resolve("file"));

        int filesOrganized = FileOrganizer.organizeDirectory(tempDir.toString(), true, false, false);

        assertEquals(7, filesOrganized);
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.DOCUMENTS_FOLDER).resolve("document.pdf")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.IMAGES_FOLDER).resolve("image.jpg")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.VIDEOS_FOLDER).resolve("video.mp4")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.AUDIO_FOLDER).resolve("song.mp3")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.ARCHIVES_FOLDER).resolve("archive.zip")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.EXECUTABLES_FOLDER).resolve("app.exe")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.MISC_FOLDER).resolve("file")));
    }

    @Test
    void testOrganizeByDateShallow() throws IOException
    {
        Files.createFile(tempDir.resolve("text.txt"));

        YearMonth modifyDate = YearMonth.parse(FileAnalyzer.getFileModifyDate(tempDir.resolve("text.txt")));
        String year = String.format("%04d", modifyDate.getYear());
        String month = String.format("%02d", modifyDate.getMonthValue());

        int filesOrganized = FileOrganizer.organizeDirectory(tempDir.toString(), false, true, false);

        assertEquals(1, filesOrganized);
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(year).resolve(month).resolve("text.txt")));
    }

    @Test
    void testOrganizeByTypeAndDateShallow() throws IOException
    {
        Files.createFile(tempDir.resolve("image.png"));

        YearMonth modifyDate = YearMonth.parse(FileAnalyzer.getFileModifyDate(tempDir.resolve("image.png")));
        String year = String.format("%04d", modifyDate.getYear());
        String month = String.format("%02d", modifyDate.getMonthValue());

        int filesOrganized = FileOrganizer.organizeDirectory(tempDir.toString(), true, true, false);

        assertEquals(1, filesOrganized);
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(year).resolve(month).resolve(Constants.IMAGES_FOLDER).resolve("image.png")));
    }

    @Test
    void testOrganizeByTypeRecursive() throws IOException
    {
        Files.createFile(tempDir.resolve("document.doc"));
        Files.createFile(tempDir.resolve("audio.m4a"));

        Path subDir = tempDir.resolve("subFolder");
        Files.createDirectories(subDir);
        Files.createFile(subDir.resolve("nestedFile.pdf"));

        int filesOrganized = FileOrganizer.organizeDirectory(tempDir.toString(), true, false, true);

        assertEquals(3, filesOrganized);
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.DOCUMENTS_FOLDER).resolve("document.doc")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.AUDIO_FOLDER).resolve("audio.m4a")));
        assertTrue(Files.exists(tempDir.resolve("Organized").resolve(Constants.DOCUMENTS_FOLDER).resolve("nestedFile.pdf")));
    }

    @Test
    void testOrganizeWithDuplicateFileNames() throws IOException
    {
        Files.createFile(tempDir.resolve("picture.heic"));

        Path organizedImages = Files.createDirectories(tempDir.resolve("Organized").resolve(Constants.IMAGES_FOLDER));
        Files.createFile(organizedImages.resolve("picture.heic"));

        int filesOrganized = FileOrganizer.organizeDirectory(tempDir.toString(), true, false, false);

        assertEquals(1, filesOrganized);
        assertTrue(Files.exists(organizedImages.resolve("picture.heic")));
        assertTrue(Files.exists(organizedImages.resolve("picture (1).heic")));
    }

    @Test
    void testOrganizeWithFilesPreOrganized() throws IOException
    {
        Path organizedAudio = Files.createDirectories(tempDir.resolve("Organized").resolve(Constants.AUDIO_FOLDER));
        Files.createFile(organizedAudio.resolve("song.mp3"));
        Files.createFile(organizedAudio.resolve("audio.ogg"));

        Path organizedVideos = Files.createDirectories(tempDir.resolve("Organized").resolve(Constants.VIDEOS_FOLDER));
        Files.createFile(organizedVideos.resolve("movie.mp4"));
        Files.createFile(organizedVideos.resolve("video.mkv"));

        int filesOrganized = FileOrganizer.organizeDirectory(tempDir.toString(), true, false, true);

        assertEquals(0, filesOrganized);
        assertTrue(Files.exists(organizedAudio.resolve("song.mp3")));
        assertTrue(Files.exists(organizedAudio.resolve("audio.ogg")));
        assertTrue(Files.exists(organizedVideos.resolve("movie.mp4")));
        assertTrue(Files.exists(organizedVideos.resolve("video.mkv")));
    }

    @Test
    void testOrganizeWithMissingDirectory()
    {
        assertThrows(IllegalArgumentException.class, () -> FileOrganizer.organizeDirectory("/missing/path", true, false, false));
    }

    @Test
    void testOrganizeWithFileAsDirectory() throws IOException
    {
        Path filePath = tempDir.resolve("file.rtf");
        Files.createFile(filePath);

        assertThrows(IllegalArgumentException.class, () -> FileOrganizer.organizeDirectory(filePath.toString(), true, false, false));
    }

    @Test
    void testOrganizeWithEmptyDirectory() throws IOException
    {
        int filesOrganized = FileOrganizer.organizeDirectory(tempDir.toString(), true, false, true);

        assertEquals(0, filesOrganized);
        assertTrue(Files.exists(tempDir.resolve("Organized")));
    }
}