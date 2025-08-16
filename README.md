# Auto File Organizer

A command-line tool to automatically sort files into a structured directory based on file type, modification date, or both. It includes features for recursive organization, duplicate handling, undoing the last operation, and scheduling automated runs (currently Windows only). Empty folders within the organized directory are cleaned up automatically after organizing.


<img src="https://github.com/user-attachments/assets/7da4f152-373d-4d9c-aac6-ef171041204d" width="600" alt="Auto File Organizer Run Simulation">

## Prerequisites 🛠️

-   Java 21 or later
-   [Apache Maven](https://maven.apache.org/) (for building from source)

## Installation 🚀
### Option 1: Use the Prebuilt Release (Recommended)
*(fastest way to get started)*

[![Download Latest Release](https://img.shields.io/github/v/release/sirantoinek/auto-file-organizer?label=Download%20Latest%20Release&style=for-the-badge)](../../releases/latest)

1. Go to the [Releases](../../releases) page.


2. Download:
    - The `.jar` file (`auto-file-organizer-x.x.x.jar`)
    - The `auto-file-organizer-startup.bat` file (Windows only, optional but recommended)


3. Decide how to run the application:
   - Option A: [Auto startup using the batch file (Windows only)](#automated-execution-on-windows)
   - Option B: [Run manually in the terminal](#usage)

### Option 2: Build from Source
*(for developers or if you want the latest changes)*
1.  Clone the repository:
    ```sh
    git clone https://github.com/sirantoinek/auto-file-organizer.git
    cd auto-file-organizer
    ```
    

2.  Build the project using Maven. This will create a runnable JAR file with all dependencies included.
    ```sh
    mvn clean package
    ```
    The JAR file will be located in the `target/` directory (e.g., `target/auto-file-organizer-x.x.x.jar`).

    The optional `auto-file-organizer-startup.bat` will be located in the `startup/windows/` directory.


3. Decide how to run the application:
    - Option A: [Auto startup using the batch file (Windows only)](#automated-execution-on-windows)
    - Option B: [Run manually in the terminal](#usage)
## Usage 💻

Run the application from your terminal using the following format:

```sh
java -jar auto-file-organizer-x.x.x.jar <folder-path> [flags]
```
- **Note:** When running from the terminal, make sure you are in the same directory as the `.jar`
  or specify its full path. (Also replace x.x.x with the current version number).

### Arguments

-   `<folder-path>`: (Required) The absolute path to the directory you want to organize. Enclose the path in quotes if it contains spaces.

### Flags

| Flag          | Description                                                                                                                                |
|---------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| `--by-type`   | Organizes files by their type into category folders (e.g., `Documents`, `Images`). (This is the default method).                           |
| `--by-date`   | Organizes files by their last modified date into a `YYYY/MM` structure.                                                                    |
| `--recursive` | Scans and organizes files in the target directory and all its subdirectories. (Only the top-level is organzied by default).                |
| `--undo`      | Reverts the last organization operation. Prompts for confirmation before proceeding.                                                       |
| `--auto`      | Enables automatic mode for scheduled tasks. The tool will only run if a set interval (default: 7 days) has passed since the last auto-run. |
| `--help`      | Displays the help message with usage instructions and a list of flags.                                                                     |

### Examples

-   **Organize the Downloads folder by file type (shallow) (default):**
    ```sh
    java -jar auto-file-organizer-0.5.0.jar "C:\Users\YourUser\Downloads"
    ```

-   **Organize the Downloads folder by both type and date (shallow):**
    ```sh
    java -jar auto-file-organizer-0.5.0.jar "C:\Users\YourUser\Downloads" --by-type --by-date
    ```
-   **Organize a folder by date only (recursive):**
    ```sh
    java -jar auto-file-organizer-0.5.0.jar "/path/to/your/folder" --by-date --recursive
    ```

-   **Undo the last organization run:**
    ```sh
    java -jar auto-file-organizer-0.5.0.jar --undo
    ```

### File Type Categories

| Category | Extensions                                                          |
|----------|---------------------------------------------------------------------|
| **Documents** | csv, doc, docx, ods, odt, odp, pdf, ppt, pptx, rtf, txt, xls, xlsx  |
| **Images** | bmp, gif, heic, heif, ico, jpg, jpeg, png, svg, tif, tiff, webp     |
| **Videos** | avi, flv, m4v, mkv, mov, mp4, qt, webm, wmv                         |
| **Audio** | aac, aiff, alac, m4a, mp3, opus, ogg, flac, wav, wma                |
| **Archives** | 7z, bz2, dmg, gz, iso, rar, tar, zip                                |
| **Executables** | apk, app, bat, cmd, com, dll, elf, exe, ipa, jar, msi, scr, sh, sys |
| **Miscellaneous** | Everything else                                                     |

## Automated Execution on Windows ⚙️

You can configure the tool to run automatically on Windows startup using the provided `.bat` script.

1.  Open `auto-file-organizer-startup.bat` in a text editor.


2.  **Modify the variables:**
    -   `JAR_PATH`: Set this to the absolute path of your `auto-file-organizer-x.x.x.jar` file.
    -   `ORGANIZE_FOLDER`: Set this to the absolute path of the folder you want to organize automatically.
    -   `FLAGS`: Set any desired organization flags (e.g., `--by-type`, `--recursive`).


3.  Save the file.


4.  Move or copy the modified `.bat` script to the Windows Startup folder `%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup`. (Can be opened by pressing `Win + R` and running `shell:startup`).

The script uses the `--auto` flag, which ensures that organization only runs once every 7 days and a summary of the previous run is saved at `%USERPROFILE%\auto-file-organizer-last-run.txt`. (Can be opened by pressing `Win + R` and running `%USERPROFILE%`).<br>
This interval can be configured in `src/main/java/com/github/sirantoinek/autofileorganizer/util/Constants.java`.

## How It Works 🔍

- **File Analysis**: The tool determines a file's category based on its extension (e.g., `.pdf` → `Documents`) and its modification date for date-based sorting.
- **Organization Logic**:
    - A new folder named `Organized` is created in the source directory to store the sorted files.
    - Based on the chosen flags, files are moved into a structured path. For example, a file organized by date and type will be moved to `.../Organized/2023/10/Documents/report.pdf`.
    - **Folder Blacklist**: Certain folders are automatically excluded from scanning to prevent moving system files or hidden directories. These include Unix hidden folders (prefix `.`) and key Windows system folders such as `Program Files`, `Program Files (x86)`, `System Volume Information`, `Windows`, `AppData`, `$Recycle.Bin`, and `Temp`. Any folders in this blacklist remain untouched during organization.
- **Undo Manager**: When an organization is run, an `auto-file-organizer-undo-log.txt` file is created in your user home directory. This log records every file move and folder creation/deletion. The `--undo` command reads this log in reverse to revert all changes. If a file conflict occurs during the undo process, the tool will pause and prompt you to resolve it manually before continuing.
- **Auto Run Manager**: When using the `--auto` flag, the tool checks for an `auto-file-organizer-last-run.txt` file in your user home directory. It compares the date of the last run with the current date to decide if it should proceed with the organization.

## License 📄
This project is licensed under the MIT License. See the [LICENSE](#license) file for details.

## Third-Party Licenses 📦📄

- Apache Commons IO (Apache License 2.0)
- JUnit 5 (Eclipse Public License 2.0)