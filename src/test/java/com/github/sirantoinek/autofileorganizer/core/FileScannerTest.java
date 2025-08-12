package com.github.sirantoinek.autofileorganizer.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileScannerTest
{
    @TempDir
    Path tempDir;

    @Test
    void testScanDirectoryRecursive() throws IOException
    {
        // Setup test files and directories.
        Files.createFile(tempDir.resolve("note.txt"));
        Files.createFile(tempDir.resolve("screenshot.png"));

        Path subDir = tempDir.resolve("subFolder");
        Files.createDirectories(subDir);
        Files.createFile(subDir.resolve("nestedFile.pdf"));

        Path blacklistedDir = tempDir.resolve("AppData");
        Files.createDirectories(blacklistedDir);
        Files.createFile(blacklistedDir.resolve("ignored.docx"));

        List<Path> fileList = FileScanner.scanDirectoryRecursive(tempDir);

        assertEquals(3, fileList.size());
        assertTrue(containsFileName(fileList, "note.txt"));
        assertTrue(containsFileName(fileList, "screenshot.png"));
        assertTrue(containsFileName(fileList, "nestedFile.pdf"));
        assertFalse(containsFileName(fileList, "ignored.docx"));
    }

    @Test
    void testScanDirectoryRecursiveWithEmptyFolder()
    {
        // Directly scan empty tempDir.
        List<Path> fileList = FileScanner.scanDirectoryRecursive(tempDir);

        assertTrue(fileList.isEmpty());
    }

    @Test
    void testScanDirectoryShallow() throws IOException
    {
        Files.createFile(tempDir.resolve("archive.rar"));
        Files.createFile(tempDir.resolve("song.mp3"));

        Path subDir = tempDir.resolve("subFolder");
        Files.createDirectories(subDir);
        Files.createFile(subDir.resolve("nestedFile.txt"));

        List<Path> fileList = FileScanner.scanDirectoryShallow(tempDir);

        assertEquals(2, fileList.size());
        assertTrue(containsFileName(fileList, "archive.rar"));
        assertTrue(containsFileName(fileList, "song.mp3"));
        assertFalse(containsFileName(fileList, "nestedFile.txt"));
    }

    @Test
    void testScanDirectoryShallowWithEmptyFolder()
    {
        List<Path> fileList = FileScanner.scanDirectoryShallow(tempDir);

        assertTrue(fileList.isEmpty());
    }

    private boolean containsFileName(List<Path> fileList, String fileName)
    {
        for (Path file : fileList)
        {
            if (file.getFileName().toString().equals(fileName)) return true;
        }
        return false;
    }
}