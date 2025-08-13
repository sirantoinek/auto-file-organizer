package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

// Manages organization scheduling when --auto flag is set.
public class AutoRunManager
{
    // Checks if organization should run based on last run date.
    public static boolean shouldAutoRun()
    {
        try
        {
            Path lastRunFile = Paths.get(System.getProperty("user.home"), Constants.LAST_RUN_FILE_NAME);

            if (!Files.exists(lastRunFile)) return true; // First --auto Run.

            String lastRun;
            try (Stream<String> lines = Files.lines(lastRunFile))
            {
                lastRun = lines.findFirst().orElse("");
            }

            LocalDateTime lastRunTime = LocalDateTime.parse(lastRun, Constants.AUTO_RUN_DATE_FORMAT);
            long daysBetween = ChronoUnit.DAYS.between(lastRunTime, LocalDateTime.now());

            return daysBetween >= Constants.DAYS_BETWEEN_AUTO_RUN;
        }
        catch (Exception e)
        {
            System.err.println("Failed to read last auto run date: " + e.getMessage());
            return true; // Organize anyway so that the invalid lastRunTime is overwritten.
        }
    }

    // Records auto run time and summary to lastRunFile.
    public static void recordAutoRun(String summary)
    {
        try
        {
            Path lastRunFile = Paths.get(System.getProperty("user.home"), Constants.LAST_RUN_FILE_NAME);
            String lastRunDetails = LocalDateTime.now().format(Constants.AUTO_RUN_DATE_FORMAT) + System.lineSeparator() + summary;
            Files.writeString(lastRunFile, lastRunDetails);
        }
        catch (Exception e)
        {
            System.err.println("Failed to record auto run: " + e.getMessage());
        }
    }
}