package com.github.sirantoinek.autofileorganizer.util;

import org.apache.commons.io.filefilter.*;
import org.apache.commons.io.IOCase;
import java.time.format.DateTimeFormatter;
import java.util.Set;


public class Constants
{
    private Constants() {}

    // Date format for file organization
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    // Date format for logging auto run time
    public static final DateTimeFormatter AUTO_RUN_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Interval between auto runs in days
    public static final int DAYS_BETWEEN_AUTO_RUN = 7;

    public static final String LAST_RUN_FILE_NAME = "auto-file-organizer-last-run.txt";
    public static final String UNDO_LOG_FILE_NAME = "auto-file-organizer-undo-log.txt";

    // Folder names
    public static final String DOCUMENTS_FOLDER = "Documents";
    public static final String IMAGES_FOLDER = "Images";
    public static final String VIDEOS_FOLDER = "Videos";
    public static final String AUDIO_FOLDER = "Audio";
    public static final String ARCHIVES_FOLDER = "Archives";
    public static final String EXECUTABLES_FOLDER = "Executables";
    public static final String MISC_FOLDER = "Miscellaneous";

    // File extensions by file type
    public static final Set<String> DOCUMENT_EXTENSIONS = Set.of(
            "csv", "doc", "docx", "ods", "odt", "odp", "pdf", "ppt", "pptx", "rtf", "txt", "xls", "xlsx"
    );

    public static final Set<String> IMAGE_EXTENSIONS = Set.of(
            "bmp", "gif", "heic", "heif", "ico", "jpg", "jpeg", "png", "svg", "tiff", "tif", "webp"
    );

    public static final Set<String> VIDEO_EXTENSIONS = Set.of(
            "avi", "flv", "m4v", "mkv", "mov", "mp4", "qt", "webm", "wmv"
    );

    public static final Set<String> AUDIO_EXTENSIONS = Set.of(
            "aac", "aiff", "alac", "m4a", "mp3", "opus", "ogg", "flac", "wav", "wma"
    );

    public static final Set<String> ARCHIVE_EXTENSIONS = Set.of(
            "7z", "bz2", "dmg", "gz", "iso", "rar", "tar", "zip"
    );

    public static final Set<String> EXECUTABLE_EXTENSIONS = Set.of(
            "apk", "app", "bat", "cmd", "com", "dll", "elf", "exe", "ipa", "jar", "msi", "scr", "sh", "sys"
    );

    public static final Set<String> VALID_FLAGS = Set.of(
            "--by-type", "--by-date", "--recursive", "--auto", "--undo", "--help"
    );

    // blacklist of folders to ignore in FileUtils.listFiles() (used in FileScanner)
    public static final IOFileFilter DIRECTORY_BLACKLIST = new NotFileFilter(
            new OrFileFilter(
                    new PrefixFileFilter("."), // Unix hidden folders
                    new NameFileFilter("$Recycle.Bin", IOCase.INSENSITIVE),
                    new NameFileFilter("Program Files", IOCase.INSENSITIVE),
                    new NameFileFilter("Program Files (x86)", IOCase.INSENSITIVE),
                    new NameFileFilter("System Volume Information", IOCase.INSENSITIVE),
                    new NameFileFilter("Windows", IOCase.INSENSITIVE),
                    new NameFileFilter("AppData", IOCase.INSENSITIVE),
                    new NameFileFilter("Temp", IOCase.INSENSITIVE)
            )
    );
}