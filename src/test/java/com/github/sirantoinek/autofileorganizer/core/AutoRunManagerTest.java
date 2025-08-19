package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class AutoRunManagerTest
{
    @TempDir
    Path tempDir;

    @Test
    void testShouldAutoRunWhenNoLastRunFile()
    {
        String userHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());

        assertTrue(AutoRunManager.shouldAutoRun());

        System.setProperty("user.home", userHome);
    }

    @Test
    void testShouldAutoRunWhenLastRunRecent() throws Exception
    {
        String userHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());

        Path lastRunFile = tempDir.resolve(Constants.LAST_RUN_FILE_NAME);

        String lastRunTime = LocalDateTime.now().minusDays(Constants.DAYS_BETWEEN_AUTO_RUN - 1).format(Constants.AUTO_RUN_DATE_FORMAT);
        Files.writeString(lastRunFile, lastRunTime);

        assertFalse(AutoRunManager.shouldAutoRun());

        System.setProperty("user.home", userHome);
    }

    @Test
    void testShouldAutoRunWhenLastRunOld() throws Exception
    {
        String userHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());

        Path lastRunFile = tempDir.resolve(Constants.LAST_RUN_FILE_NAME);

        String lastRunTime = LocalDateTime.now().minusDays(Constants.DAYS_BETWEEN_AUTO_RUN + 1).format(Constants.AUTO_RUN_DATE_FORMAT);
        Files.writeString(lastRunFile, lastRunTime);

        assertTrue(AutoRunManager.shouldAutoRun());

        System.setProperty("user.home", userHome);
    }

    @Test
    void testRecordAutoRun() throws Exception
    {
        String userHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());

        String summary = "Organization summary.";

        AutoRunManager.recordAutoRun(summary);

        Path lastRunFile = tempDir.resolve(Constants.LAST_RUN_FILE_NAME);
        String[] lastRunFileLines = Files.readString(lastRunFile).split(System.lineSeparator());

        LocalDateTime recordedTime = LocalDateTime.parse(lastRunFileLines[0], Constants.AUTO_RUN_DATE_FORMAT);

        // LastRunTime should be within half a second of the current time.
        assertTrue(ChronoUnit.SECONDS.between(recordedTime, LocalDateTime.now()) < 0.5);
        assertEquals(summary, lastRunFileLines[1]);

        System.setProperty("user.home", userHome);
    }
}