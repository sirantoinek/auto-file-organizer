package com.github.sirantoinek.autofileorganizer;

// Handles CLI args and calls necessary functionality.
public class AutoFileOrganizerCLI
{
    public static void main(String[] args)
    {
        if (args.length == 0) printHelp();

        // TODO: add argument handling
    }

    private static void printHelp()
    {
        System.out.println("File Organizer v0.1.0");
        System.out.println("Usage:");
        System.out.println("  java -jar auto-file-organizer.jar [options] <folder-path>");
        System.out.println("\nOptions:");
        System.out.println("  --by-type    Organize by file type (default)");
        System.out.println("  --by-date    Organize by date (year/month)");
        System.out.println("  --auto       Auto mode for startup scheduling");
        System.out.println("  --undo       Undo last organize operation");
        System.out.println("  --help       Show this help message");
        System.out.println("\nExamples:");
        System.out.println("  java -jar auto-file-organizer.jar \"C:\\Users\\User\\Downloads\"");
        System.out.println("  java -jar auto-file-organizer.jar --by-type --by-date \"C:\\Users\\User\\Downloads\"");
        System.out.println("  java -jar auto-file-organizer.jar --undo");
    }
}