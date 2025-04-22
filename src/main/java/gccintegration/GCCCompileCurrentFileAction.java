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
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class GCCCompileCurrentFileAction extends AnAction {
    
    private String getShortcutText() {
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
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project thisProject = event.getProject(); // user's project instance
        if (thisProject == null) {
            return;
        }
        Editor editor = FileEditorManager.getInstance(thisProject).getSelectedTextEditor(); // editor session
        if (editor == null) {
            return;
        }

        VirtualFile[] openedFiles = FileEditorManager.getInstance(thisProject).getSelectedFiles();
        String curFileName = openedFiles[0].getName();
        String outname = null;
        String outpath = null;

        // retrieve the file type of the active file
        int lastIndex = curFileName.lastIndexOf('.');
        String curFileType = null;
        if (lastIndex > 0) {
            curFileType = curFileName.substring(lastIndex + 1);
        }
        String filePath = null;
        try {
            filePath = SysUtil.getCurrentFilepath(thisProject);
        }
        catch (NullPointerException ex) {
            return;
        }
        if (SystemInfo.isWindows) {
            // if it's a Windows system, executables should be .exe
            outname = curFileName.substring(0, curFileName.lastIndexOf('.')) + ".exe";
            outpath = filePath.substring(0, filePath.lastIndexOf('.')) + ".exe";
        } else {
            // otherwise just trim off the whole file type
            // in macOS & Linux, no file type usually means it's executable
            outname = curFileName.substring(0, curFileName.lastIndexOf('.'));
            outpath = filePath.substring(0, filePath.lastIndexOf('.'));
        }

        if (curFileType == null) {
            curFileType = "none";
        } else {
            curFileType = "." + curFileType;
        }

        // determine if we need to use GCC or G++ (.c or .cpp)
        Pair<Integer, String> cmdRet = null;
        SysUtil.clearConsole(thisProject);
        if ((curFileType.equals(".c")) | (curFileType.equals(".cpp"))) {
            List<String> sourceFiles = OptionParse.getChosenSourceFiles(thisProject, editor, filePath);
            cmdRet = SysUtil.runCompiler(sourceFiles, outname, curFileType.equals(".cpp"), thisProject);
        } else {
            String shortcutText = getShortcutText();
            SysUtil.consoleWriteInfo("Error: The `Run New Executable` plugin only works with .c and .cpp files. Current file type: " + curFileType + "\nPress " + shortcutText + " again with a valid active C/C++ file.\n", thisProject);
        }

        if (cmdRet != null) {
            Integer cmdCode = cmdRet.getLeft();
            String cmdOut = cmdRet.getRight();

            if (cmdRet.getLeft() == 0) { // refers to exit code
                SysUtil.consoleWrite(cmdOut, thisProject);
            } else {
                SysUtil.consoleWriteError(cmdOut, thisProject);
            }
            if (cmdCode == 0) {
                SysUtil.consoleWriteInfo("Saved compiled executable as " + outpath + "\n", thisProject);
                List<String> params = OptionParse.getChosenExeParams(thisProject, editor);
                SysUtil.runExecutable(outpath, params, thisProject);
            }
        } else {
            SysUtil.consoleWriteError("Failed to compile file(s): An unknown error occurred.", thisProject);
        }
    }
}
