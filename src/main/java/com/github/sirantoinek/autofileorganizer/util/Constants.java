package com.github.sirantoinek.autofileorganizer.util;

import java.time.format.DateTimeFormatter;
import java.util.Set;


public class Constants
{
    private Constants() {}

    // Date format
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

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
            "pdf", "doc", "docx", "odt", "txt", "rtf", "xls", "xlsx", "ods", "ppt", "pptx", "odp", "csv"
    );

    public static final Set<String> IMAGE_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "svg", "webp", "ico", "tiff", "tif", "heif", "heic"
    );

    public static final Set<String> VIDEO_EXTENSIONS = Set.of(
            "mp4", "avi", "mov", "qt", "wmv", "mkv", "webm", "flv", "m4v"
    );

    public static final Set<String> AUDIO_EXTENSIONS = Set.of(
            "mp3", "wav", "flac", "aac", "ogg", "wma", "m4a", "alac", "aiff"
    );

    public static final Set<String> ARCHIVE_EXTENSIONS = Set.of(
            "zip", "rar", "7z", "tar", "gz", "bz2", "dmg", "iso"
    );

    public static final Set<String> EXECUTABLE_EXTENSIONS = Set.of(
            "exe", "dll", "msi", "bat", "cmd", "com", "scr", "sys", "elf", "app", "jar", "apk", "ipa"
    );
}