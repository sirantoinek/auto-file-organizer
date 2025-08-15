package com.github.sirantoinek.autofileorganizer.core;

import com.github.sirantoinek.autofileorganizer.util.Constants;
import org.apache.commons.io.FileUtils;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

public class UndoManager
{
    public static boolean undoLastRun()
    {
        try
        {
            Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);
            if (!Files.exists(undoLog)) throw new FileNotFoundException("No undo log file found.");

            List<String> lines = Files.readAllLines(undoLog);
            Collections.reverse(lines);

            java.util.Scanner scanner = new java.util.Scanner(System.in);
            for (String line : lines)
            {
                if (line.startsWith("MOVE"))
                {
                    String[] splitLine = line.split(" \\| ");
                    String[] movePaths = splitLine[1].split(" -> ");
                    Path source = Paths.get(movePaths[0].trim());
                    Path destination = Paths.get(movePaths[1].trim());

                    // Throw an exception if any of the organized files are missing.
                    if (!Files.exists(destination)) throw new FileNotFoundException("Organized file missing: \"" + destination + "\".");

                    // Handle conflicting file names with user intervention.
                    while (Files.exists(source))
                    {
                        System.out.println("Filename Conflict detected at \"" + source + "\".");
                        System.out.println("Please rename or move this file before continuing.");
                        System.out.println("Press Enter when ready to continue...");
                        scanner.nextLine();
                    }

                    Files.createDirectories(source.getParent()); // Re-create any missing folders needed to move the file back to its original location.
                    Files.move(destination, source); // Reverse the original move.
                }
                else if (line.startsWith("CREATE"))
                {
                    String[] splitLine = line.split(" \\| ");
                    Path folder = Paths.get(splitLine[1].trim());

                    FileUtils.deleteDirectory(folder.toFile()); // Reverse the original folder creation.
                }
                else if (line.startsWith("DELETE"))
                {
                    String[] splitLine = line.split(" \\| ");
                    Path folder = Paths.get(splitLine[1].trim());

                    Files.createDirectories(folder); // Reverse the original folder deletion.
                }
                else throw new IllegalArgumentException("Invalid undo log entry: \"" + line + "\".");
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to undo the last organization run: " + e.getMessage());
            deleteLogFile();
            return false;
        }

        deleteLogFile();
        System.out.println("Undo complete. All changes from the last organization run have been reverted.");
        return true;
    }

    public static void logMoveFile(Path source, Path destination)
    {
        String moveDetails = source.toString() + " -> " + destination.toString();

        try
        {
            Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);
            Files.writeString(undoLog, "MOVE | " + moveDetails + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (Exception e)
        {
            System.err.println("Failed to log move: \"" + moveDetails + "\": " + e.getMessage());
        }
    }

    public static void logCreateFolder(Path folder)
    {
        try
        {
            Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);
            Files.writeString(undoLog, "CREATE | " + folder + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (Exception e)
        {
            System.err.println("Failed to log folder creation: \"" + folder + "\": " + e.getMessage());
        }
    }

    public static void logDeleteFolder(Path folder)
    {
        try
        {
            Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);
            Files.writeString(undoLog, "DELETE | " + folder + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (Exception e)
        {
            System.err.println("Failed to log folder deletion: \"" + folder + "\": " + e.getMessage());
        }
    }

    public static void deleteLogFile()
    {
        try
        {
            Path undoLog = Paths.get(System.getProperty("user.home"), Constants.UNDO_LOG_FILE_NAME);
            Files.deleteIfExists(undoLog);
        }
        catch (Exception e)
        {
            System.err.println("Failed to delete undo log: " + e.getMessage());
        }
    }
}