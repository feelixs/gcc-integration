package gccintegration;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.commons.lang3.tuple.Pair;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import com.intellij.openapi.util.Key;
import java.util.ArrayList;
import java.util.List;

public class SysUtil {
    private static final Key<ConsoleView> CONSOLE_VIEW_KEY = new com.intellij.openapi.util.Key<>("gccConsole.ConsoleView");

    private static String getShortcutText() {
        ActionManager actionManager = ActionManager.getInstance();
        Keymap keymap = KeymapManager.getInstance().getActiveKeymap();
        Shortcut[] shortcuts = keymap.getShortcuts("GccCompileCurrentFile");
        String shortcutText = "the shortcut";

        for (Shortcut shortcut : shortcuts) {
            if (shortcut instanceof KeyboardShortcut) {
                KeyboardShortcut keyboardShortcut = (KeyboardShortcut) shortcut;
                shortcutText = KeymapUtil.getShortcutText(keyboardShortcut);
                break;
            }
        }
        return shortcutText;
    }

    public static ConsoleView getStoredConsole(Project project) {
        ConsoleView console = project.getUserData(CONSOLE_VIEW_KEY);
        if (console == null) {
            project.putUserData(CONSOLE_VIEW_KEY, new ConsoleViewImpl(project, true));
            console = project.getUserData(CONSOLE_VIEW_KEY);
            String shortcutText = getShortcutText();
            console.print("Welcome to GCC Integration!\n", ConsoleViewContentType.SYSTEM_OUTPUT);
            console.print("Use " + shortcutText + " to compile and run the current file.\n", ConsoleViewContentType.SYSTEM_OUTPUT);
            console.print("For help and documentation, visit: https://feelixs.github.io/gcc-integration/\n\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        }
        return console;
    }

    public static void clearConsole(Project project) {
        ConsoleView console = getStoredConsole(project);
        console.clear();
    }

    public static void consoleWrite(String words, Project project) {
        // must be called after 'actionPerformed' is run
        // because actionPerformed sets up thisProject variable

        ConsoleView console = getStoredConsole(project);
        ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow("Executable Build Output");
        if (window == null) {
            return;
        }
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(console.getComponent(), "", true);
        window.getContentManager().addContent(content);
        console.print(words, ConsoleViewContentType.NORMAL_OUTPUT);
        window.activate(null);
    }

    /**
     * @param project IDE's opened project
     * @return filepath of currently open file in the IDE editor
     */
    public static String getCurrentFilepath(Project project) {
        // GET CURRENT FILE's PATH
        // we need to get the current file as a
        // Document -> PsiFile -> String
        // to get the absolute filepath
        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(currentDoc);
        VirtualFile vFile = psiFile.getOriginalFile().getVirtualFile();
        return vFile.getPath();
    }

    /**
     * Isolate the filename from filepaths
     * @param filePaths String List of full filepaths
     * @return String List of filenames
     */
    public static List<String> getFileNames(List<String> filePaths) {
        List<String> fileNames = new ArrayList<>();
        for (String file : filePaths) {
            fileNames.add(getFileName(file));
        }
        return fileNames;
    }

    /**
     *
     * @param filepath String filepath
     * @return the filename referenced by the filepath
     */
    public static String getFileName(String filepath) {
        if (filepath.contains("/")) {
            // isolate the file's name
            String[] filePathList = filepath.split("/");
            return filePathList[filePathList.length - 1];
        }
        else {
            return filepath;
        }
    }

    /**
     *
     * @param sourceFiles the source files to compile
     * @param outputName the name of the output executable
     * @param cpp Is it a .cpp file? (default .c)
     * @return Pair of int, string (return code, std output)
     */
    public static Pair<Integer, String> runCompiler(List<String> sourceFiles, String outputName, Boolean cpp, Project project) {
        consoleWrite((cpp ? "Compiling using G++ " : "Compiling using GCC ") + sourceFiles + "\n", project);
        Integer exitCode = 0;
        StringBuilder ret = new StringBuilder();
        String mainSrcPath = sourceFiles.get(0);
        sourceFiles.remove(0);
        sourceFiles.add(0, getFileName(mainSrcPath));
        File workingDir = new File(mainSrcPath).getParentFile();
        sourceFiles.add(0, (cpp ? "g++" : "gcc"));
        sourceFiles.add("-o");
        sourceFiles.add(outputName);

        // convert the full command list to a string for printing
        String fullCmdString = String.join(" ", sourceFiles);
        consoleWrite("% " + fullCmdString + "\n", project);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(sourceFiles);
            processBuilder.directory(workingDir);
            processBuilder.redirectErrorStream(true); // we want to be able to print errors
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                ret.append(line).append("\n");
            }
            reader.close();
            exitCode = process.waitFor();

            String infoStr = "To add more source c/c++ files, add them ABOVE ALL LINES in the active file, as a comment: //+file.cpp (https://feelixs.github.io/gcc-integration/config.html#adding-additional-source-files)\n";

            String endstr = "\n";
            if (sourceFiles.size() == 4) {  // default length for 1 compiled file (contained all strs in command)
                endstr = " " + infoStr;
            }

            if (exitCode == 0) {
                ret.append("Compilation succeeded.").append(endstr);
            } else {
                ret.append("INFO: " + infoStr + "Compilation failed with exit code: ").append(exitCode).append("\n");
            }
        } catch (IOException | InterruptedException ex) {
            ret.append("Error executing ").append(cpp ? "g++" : "gcc").append(" command: ").append(ex.getMessage()).append("\n");
        }
        return Pair.of(exitCode, ret.toString());
    }

    public static void runExecutable(String exePath, List<String> params, Project project) {
        // run an executable and print to console while it's running
        try {
            File exeFile = new File(exePath);

            if (!exeFile.exists()) {
                consoleWrite("Error: Executable file not found at path: " + exePath + "\n", project);
                return;
            }

            File workingDirectory = exeFile.getParentFile();
            List<String> fullCmd = new ArrayList<>(params);
            fullCmd.add(0, exeFile.getAbsolutePath());

            // convert the full command list to a string for printing
            String fullCmdString = String.join(" ", fullCmd);

            String endstr = "\n% ";
            if (params.isEmpty()) {
                endstr = ". View docs on automatically adding parameters: https://feelixs.github.io/gcc-integration/config.html#adding-arguments-parameters\n% ";
            }
            consoleWrite("Running with parameters: " + params + endstr + fullCmdString + "\n", project);
            ProcessBuilder processBuilder = new ProcessBuilder(fullCmd);
            processBuilder.directory(workingDirectory);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                consoleWrite("\t" + line + "\n", project);
            }
            reader.close();
            // wait for the program to finish before retrieving the result
            int exitCode = process.waitFor();
            consoleWrite("Program finished with exit code: " + exitCode + "\n", project);

        } catch (IOException ex) {
            consoleWrite("Error while running program executable: " + ex.getMessage() + "\n", project);
        } catch (InterruptedException ex) {
            consoleWrite("Error while waiting for the process to finish: " + ex.getMessage() + "\n", project);
        }
    }
}
