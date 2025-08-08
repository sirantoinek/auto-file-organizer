package com.github.sirantoinek.autofileorganizer;

// Handles CLI args and calls necessary functionality.
public class AutoFileOrganizerCLI
{
    public static void main(String[] args)
    {
        if (args.length == 0 || args[0].equals("--help"))
        {
            printHelp();
            return;
        }

        if (args[0].equals("--undo"))
        {
            // TODO: add undo feature
            return;
        }

        // TODO: add validity check for folder-path

        boolean byType = hasFlag(args, "--by-type") || args.length == 1; // default sorting method
        boolean byDate = hasFlag(args, "--by-date");
        boolean recursive = hasFlag(args, "--recursive");

        if (hasFlag(args, "--auto"))
        {
            // TODO: add auto feature
            return;
        }

        // TODO: add file organization using (args[0] folderPath, byType, byDate, recursive)

        // temporary log for testing
        System.out.println("Organize: " + args[0] + " byType: " + byType + " byDate: " + byDate + " recursive: " + recursive);
    }

    private static boolean hasFlag(String[] args, String flag)
    {
        for (String arg : args) if (arg.equals(flag)) return true;
        return false;
    }

    private static void printHelp()
    {
        System.out.println("File Organizer v0.2.0");
        System.out.println("Usage:");
        System.out.println("  java -jar auto-file-organizer.jar [flags] <folder-path>");
        System.out.println("\nFlags:");
        System.out.println("  --by-type      Organize by file type (default)");
        System.out.println("  --by-date      Organize by date (year/month)");
        System.out.println("  --recursive    Organize files in all subfolders");
        System.out.println("  --auto         Auto mode for startup scheduling");
        System.out.println("  --undo         Undo last organize operation");
        System.out.println("  --help         Show this help message");
        System.out.println("\nExamples:");
        System.out.println("  java -jar auto-file-organizer.jar \"C:\\Users\\User\\Downloads\"");
        System.out.println("  java -jar auto-file-organizer.jar --by-type --by-date \"C:\\Users\\User\\Downloads\"");
        System.out.println("  java -jar auto-file-organizer.jar --undo");
    }
}