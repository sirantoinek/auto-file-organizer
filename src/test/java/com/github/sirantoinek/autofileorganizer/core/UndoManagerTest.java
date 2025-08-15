package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UndoManagerTest
{
    @TempDir
    Path tempDir;

    @Test
    void testLogMoveFile() throws IOException
    {
        UndoManager.deleteLogFile();

        Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);

        Path source = tempDir.resolve("note.txt");
        Path destination = tempDir.resolve("Organized").resolve(Constants.DOCUMENTS_FOLDER).resolve("note.txt");

        UndoManager.logMoveFile(source, destination);

        List<String> lines = Files.readAllLines(undoLog);
        assertEquals(1, lines.size());
        assertEquals(lines.getFirst(), "MOVE | " + source + " -> " + destination);

        UndoManager.deleteLogFile();
    }

    @Test
    void testLogCreateFolder() throws IOException
    {
        UndoManager.deleteLogFile();

        Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);

        Path folder = tempDir.resolve("NewFolder");

        UndoManager.logCreateFolder(folder);

        List<String> lines = Files.readAllLines(undoLog);
        assertEquals(1, lines.size());
        assertEquals(lines.getFirst(), "CREATE | " + folder);

        UndoManager.deleteLogFile();
    }

    @Test
    void testLogDeleteFolder() throws IOException
    {
        UndoManager.deleteLogFile();

        Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);

        Path folder = tempDir.resolve("DeletedFolder");

        UndoManager.logDeleteFolder(folder);

        List<String> lines = Files.readAllLines(undoLog);
        assertEquals(1, lines.size());
        assertEquals(lines.getFirst(), "DELETE | " + folder);

        UndoManager.deleteLogFile();
    }

    @Test
    void testUndoLastRun() throws IOException
    {
        // Creating initial directory
        Path organizedFolder = tempDir.resolve("Organized");
        Files.createDirectories(organizedFolder);

        Path oldFolder = organizedFolder.resolve("OldFolder");
        Files.createDirectories(oldFolder);

        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Path file2 = Files.createFile(oldFolder.resolve("file2.txt"));

        // Simulating an organization run
        Path documentsFolder = organizedFolder.resolve(Constants.DOCUMENTS_FOLDER);
        Files.createDirectories(documentsFolder);
        UndoManager.logCreateFolder(documentsFolder);

        Path movedFile1 = documentsFolder.resolve("file1.txt");
        Files.move(file1, movedFile1);
        UndoManager.logMoveFile(file1, movedFile1);

        Path movedFile2 = documentsFolder.resolve("file2.txt");
        Files.move(file2, movedFile2);
        UndoManager.logMoveFile(file2, movedFile2);

        FileUtils.deleteDirectory(oldFolder.toFile());
        UndoManager.logDeleteFolder(oldFolder);

        // Undoing the simulated organization run using logged actions
        UndoManager.undoLastRun();

        assertTrue(Files.exists(organizedFolder));
        assertTrue(Files.exists(oldFolder));
        assertTrue(Files.exists(file1));
        assertTrue(Files.exists(file2));
        assertFalse(Files.exists(documentsFolder));
        assertFalse(Files.exists(movedFile1));
        assertFalse(Files.exists(movedFile2));
    }
}