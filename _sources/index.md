# JetBrains GCC Integration

This is a JetBrains IDE Plugin specifically designed for IDEs that do not have c/cpp support such as PyCharm. Its functionality is simple - it adds a keyboard shortcut that can run the GCC/G++ compiler on the active file (GCC must be already installed and in your system PATH).

The default keyboard shortcut is `ctrl + shift + G`.

---- 

## Dependencies

GCC must be installed separately. To download, follow these steps:

### Windows

1) Visit https://sourceforge.net/projects/mingw/ and download.
2) In the GUI under "basic setup", check off `mingw32` and `mingw32gcc-g++`
3) Click "Installation > Apply Changes"
4) Add MingW32 to your System path (`C:\MinGW\bin`)

### Mac

Run the command `brew install gcc`

----

## Installation

You can either install directly through PyCharm, or install from its GitHub releases.

- To install directly through PyCharm, open `PyCharm --> Settings --> Plugins` and search for it on the marketplace

## Manual Install

- To manually install, visit the [latest GitHub release](https://github.com/feelixs/gcc-integration/releases) and download the `.jar`. Open `PyCharm --> Settings --> Plugins`, click the Settings cog and choose `Install Plugin from disk...`

----

## Usage

If the file that's open in the editor is of type .c or .cpp, press `ctrl + shift + G` to send it straight to the GCC/G++ compiler in a new IDE Tool Window. If the file successfully compiles, this plugin will also run the created executable in the same toolwindow.

![preview](plugin-preview.png)

### Modifying the Plugin's Behavior

To view how to add optional settings to the plugin's behavior per each file, read [here](config.md)

