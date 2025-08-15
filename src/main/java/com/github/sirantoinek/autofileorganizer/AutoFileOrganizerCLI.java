package com.github.sirantoinek.autofileorganizer;

import com.github.sirantoinek.autofileorganizer.core.AutoRunManager;
import com.github.sirantoinek.autofileorganizer.core.FileOrganizer;
import com.github.sirantoinek.autofileorganizer.core.UndoManager;
import com.github.sirantoinek.autofileorganizer.util.Constants;
import java.io.IOException;

// Handles CLI args and calls necessary functionality.
public class AutoFileOrganizerCLI
{
    public static void main(String[] args) throws IOException
    {
        String invalidFlag = hasInvalidFlag(args);
        if (invalidFlag != null)
        {
            printInvalidFlagError(invalidFlag);
            return;
        }

        if (args.length == 0 || hasFlag(args, "--help"))
        {
            printHelp();
            return;
        }

        boolean auto = hasFlag(args, "--auto");

        if (auto && !AutoRunManager.shouldAutoRun())
        {
            return; // Skip auto organization if it shouldn't run.
        }

        if (hasFlag(args, "--undo"))
        {
            boolean undoSucceeded = handleUndo();

            if (auto)
            {
                String summary = undoSucceeded ? "Undo complete. Last organization run has been reverted." : "Failed to undo the last organization run.";
                AutoRunManager.recordAutoRun(summary);
            }
            return;
        }

        boolean byType = hasFlag(args, "--by-type") || !hasFlag(args, "--by-date"); // default sorting method
        boolean byDate = hasFlag(args, "--by-date");
        boolean recursive = hasFlag(args, "--recursive");

        int filesOrganized = FileOrganizer.organizeDirectory(args[0], byType, byDate, recursive); // args[0] = folderPath
        String summary = returnSummary(filesOrganized, args[0], byType, byDate, recursive);

        if (auto)
        {
            AutoRunManager.recordAutoRun(summary);
            return;
        }

        System.out.print(summary);
    }

    private static boolean hasFlag(String[] args, String flag)
    {
        for (String arg : args) if (arg.equals(flag)) return true;
        return false;
    }

    private static String hasInvalidFlag(String[] args)
    {
        for (int i = 1; i < args.length; i++) if (!Constants.VALID_FLAGS.contains(args[i])) return args[i];
        return null;
    }

    private static boolean handleUndo()
    {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String input;
        do
        {
            System.out.print("Are you sure you want to undo the last organization run? (y/n): ");
            input = scanner.nextLine().trim().toLowerCase();
        }
        while (!input.equals("y") && !input.equals("n") && !input.equals("yes") && !input.equals("no"));

        if (input.equals("y") || input.equals("yes"))
        {
            return UndoManager.undoLastRun();
        }

        System.out.println("Undo cancelled.");
        return false;
    }

    private static void printInvalidFlagError(String invalidFlag)
    {
        System.err.println("Error: Unrecognized flag '" + invalidFlag + "'.");
        System.err.println("Use --help to see the list of valid flags.");
    }

    private static void printHelp()
    {
        System.out.println("File Organizer v0.4.0");
        System.out.println("Usage:");
        System.out.println("  java -jar auto-file-organizer.jar <folder-path> [flags]");
        System.out.println("\nFlags:");
        System.out.println("  --by-type      Organize by file type (default)");
        System.out.println("  --by-date      Organize by date (year/month)");
        System.out.println("  --recursive    Organize files in all subfolders");
        System.out.println("  --auto         Auto mode for startup scheduling");
        System.out.println("  --undo         Undo last organize operation");
        System.out.println("  --help         Show this help message");
        System.out.println("\nExamples:");
        System.out.println("  java -jar auto-file-organizer.jar \"C:\\Users\\User\\Downloads\"");
        System.out.println("  java -jar auto-file-organizer.jar \"C:\\Users\\User\\Downloads\" --by-type --by-date");
        System.out.println("  java -jar auto-file-organizer.jar --undo");
    }

    private static String returnSummary(int filesOrganized, String folderPath, boolean byType, boolean byDate, boolean recursive)
    {
        return String.format(
                "Organized %d file%s in \"%s\" %s using %s organization.%n",
                filesOrganized,
                filesOrganized == 1 ? "" : "s",
                folderPath,
                (byType && byDate) ? "by type and date" : byType ? "by type" : "by date",
                recursive ? "recursive" : "shallow"
        );
    }
}