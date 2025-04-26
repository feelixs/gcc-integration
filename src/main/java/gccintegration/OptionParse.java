package gccintegration;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPlainText;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OptionParse {
    private static final Pattern COMMENT_PATTERN = Pattern.compile("^//.*");
    // pattern must occur at the start of a line (^) because we want lines that are only comments
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("^\\s*$");
    private static final Pattern SOURCEFILE_PATTERN = Pattern.compile("\\s?\\+.*");
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\s?\\[.*]");

    /**
     * Return commented lines above all code of the active file
     */
    // is the lines only composed of spaces?
    public static List<String> getBeginComments(Project project, Editor editor) {
        // return commented lines above all code of the active file
        List<String> comments = new ArrayList<>();
        try {
            PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
            Document document = editor.getDocument();
            PsiFile psiFile = psiDocumentManager.getPsiFile(document);

            if (psiFile == null) {
                SysUtil.consoleWriteError("Error: Could not parse user comment options: Unable to read the active file.\n", project);
            } else {
                for (PsiElement element : psiFile.getChildren()) {
                    if (element instanceof PsiPlainText) {
                        String[] lines = element.getText().split("\\r?\\n");
                        // split file into a list of its lines
                        // \r is optional as sometimes windows will put \r\n to signify newlines
                        for (String line : lines) {
                            if (COMMENT_PATTERN.matcher(line).find()) {
                                // is the line a comment?
                                String[] parts = line.trim().split("//", 2);
                                if (parts.length > 1) {
                                    comments.add(parts[1]); // remove the "//" from the comment
                                }
                            } else if (WHITESPACE_PATTERN.matcher(line).find()) {
                                // line is not a comment, but it's an empty line
                                // empty lines --> keep reading comments
                                continue;
                            } else {
                                // non-comment line --> comments are over
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Error err) {
            SysUtil.consoleWriteError("Error: Could not parse user comment options: " + err + "\n", project);
        }
        return comments;
    }

    /**
     * Return a list of params specified in the current file formatted
     * // [param1, param2, etc]
     */
    public static List<String> getChosenExeParams(Project project, Editor editor) {
        List<String> allComments = getBeginComments(project, editor);
        for (String comment : allComments) {
            if (PARAM_PATTERN.matcher(comment).find()) {
                // if the line matches "// [param1, param2, ...]
                // parse it and return

                // replace "[", "]", " ", with ""
                comment = comment.replace("[", "").replace("]", "").replace(" ", "");
                // transform the string "param1,param2" into a list
                // \\s*,\\s* splits along commas and ignores whitespaces
                return Arrays.asList(comment.split("\\s*,\\s*"));
            }
        }
        return new ArrayList<>();  // return nothing if user doesnt specify any params
    }

    /**
     * @return List of starting comments that specify source files
     * Must be formatted
     *
     * // source1, source2, etc
     *
     */
    public static List<String> getChosenSourceFiles(Project project, Editor editor, String mainSrcPath) {
        List<String> returnList = new ArrayList<>();
        List<String> allComments = getBeginComments(project, editor);
        try {
            for (String comment : allComments) {
                if (SOURCEFILE_PATTERN.matcher(comment).find()) {
                    comment = comment.replace(" ", "").replace("+", "");
                    returnList.addAll(Arrays.asList(comment.split("\\s*,\\s*")));
                    // we need to remove the main source file from the "returnList" so we don't input it twice into gcc
                    // if the user has included the main source file in their comment
                    String[] mainSrcFilenames = mainSrcPath.split("/");
                    String mainSrcFilename = mainSrcFilenames[mainSrcFilenames.length - 1];
                    ;
                    returnList.remove(mainSrcFilename); // if it doesn't contain mainSrcFilename, .remove() does nothing
                }
            }
            returnList.add(0, mainSrcPath);
        } catch (Error err) {
            SysUtil.consoleWriteError("Error: Could not parse user comment options: " + err + "\n", project);
        }
        return returnList;
    }
    
    /**
     * Show dialog to configure parameters for the executable
     */
    public static void configureParameters(Project project) {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            SysUtil.consoleWriteError("Error: No active editor found.\n", project);
            return;
        }
        
        // Get current parameters
        List<String> currentParams = getChosenExeParams(project, editor);
        
        // Show dialog to edit parameters
        ParameterDialog dialog = new ParameterDialog(project, currentParams);
        if (dialog.showAndGet()) {
            // User confirmed - apply changes
            List<String> newParams = dialog.getParameters();
            applyParametersToFile(project, editor, newParams);
        }
    }
    
    /**
     * Show dialog to configure additional source files
     */
    public static void configureSourceFiles(Project project) {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            SysUtil.consoleWriteError("Error: No active editor found.\n", project);
            return;
        }
        
        try {
            String filePath = SysUtil.getCurrentFilepath(project);
            List<String> currentFiles = getChosenSourceFiles(project, editor, filePath);
            // Remove the main source file which is always at index 0
            if (!currentFiles.isEmpty()) {
                currentFiles.remove(0);
            }
            
            // Show dialog to edit source files
            SourceFilesDialog dialog = new SourceFilesDialog(project, currentFiles);
            if (dialog.showAndGet()) {
                // User confirmed - apply changes
                List<String> newFiles = dialog.getSourceFiles();
                applySourceFilesToFile(project, editor, newFiles);
            }
        } catch (NullPointerException ex) {
            SysUtil.consoleWriteError("Error: Could not get current file path.\n", project);
        }
    }
    
    /**
     * Apply parameters to the source file as a comment
     */
    private static void applyParametersToFile(Project project, Editor editor, List<String> params) {
        if (params.isEmpty()) {
            return;
        }
        
        Document document = editor.getDocument();
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        PsiFile psiFile = psiDocumentManager.getPsiFile(document);
        
        if (psiFile == null) {
            SysUtil.consoleWriteError("Error: Unable to modify the active file.\n", project);
            return;
        }
        
        // Format parameters as a comment line
        StringBuilder paramsLine = new StringBuilder("// [");
        for (int i = 0; i < params.size(); i++) {
            paramsLine.append(params.get(i));
            if (i < params.size() - 1) {
                paramsLine.append(", ");
            }
        }
        paramsLine.append("]");
        
        // Check if there's already a parameters comment
        List<String> comments = getBeginComments(project, editor);
        final boolean[] hasParamLine = {false};
        for (String comment : comments) {
            if (PARAM_PATTERN.matcher(comment).find()) {
                hasParamLine[0] = true;
                break;
            }
        }
        
        // Apply changes in a write command action
        psiDocumentManager.commitDocument(document);
        
        // Use WriteCommandAction for document modifications
        com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction(project, "Update Parameters", null, () -> {
            if (hasParamLine[0]) {
                // Replace existing parameter line
                String text = document.getText();
                String[] lines = text.split("\\r?\\n");
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].trim().startsWith("//") && PARAM_PATTERN.matcher(lines[i]).find()) {
                        int lineStart = document.getLineStartOffset(i);
                        int lineEnd = document.getLineEndOffset(i);
                        document.replaceString(lineStart, lineEnd, paramsLine.toString());
                        break;
                    }
                }
            } else {
                // Add new parameter line at the beginning
                document.insertString(0, paramsLine.toString() + "\n");
            }
            
            psiDocumentManager.commitDocument(document);
        });
    }
    
    /**
     * Apply source files to the source file as a comment
     */
    private static void applySourceFilesToFile(Project project, Editor editor, List<String> sourceFiles) {
        if (sourceFiles.isEmpty()) {
            return;
        }
        
        Document document = editor.getDocument();
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        PsiFile psiFile = psiDocumentManager.getPsiFile(document);
        
        if (psiFile == null) {
            SysUtil.consoleWriteError("Error: Unable to modify the active file.\n", project);
            return;
        }
        
        // Format source files as a comment line
        StringBuilder sourcesLine = new StringBuilder("// +");
        for (int i = 0; i < sourceFiles.size(); i++) {
            sourcesLine.append(sourceFiles.get(i));
            if (i < sourceFiles.size() - 1) {
                sourcesLine.append(", ");
            }
        }
        
        // Check if there's already a source files comment
        List<String> comments = getBeginComments(project, editor);
        final boolean[] hasSourcesLine = {false};
        for (String comment : comments) {
            if (SOURCEFILE_PATTERN.matcher(comment).find()) {
                hasSourcesLine[0] = true;
                break;
            }
        }
        
        // Apply changes in a write command action
        psiDocumentManager.commitDocument(document);
        
        // Use WriteCommandAction for document modifications
        com.intellij.openapi.command.WriteCommandAction.runWriteCommandAction(project, "Update Source Files", null, () -> {
            if (hasSourcesLine[0]) {
                // Replace existing source files line
                String text = document.getText();
                String[] lines = text.split("\\r?\\n");
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].trim().startsWith("//") && SOURCEFILE_PATTERN.matcher(lines[i]).find()) {
                        int lineStart = document.getLineStartOffset(i);
                        int lineEnd = document.getLineEndOffset(i);
                        document.replaceString(lineStart, lineEnd, sourcesLine.toString());
                        break;
                    }
                }
            } else {
                // Add new source files line at the beginning
                document.insertString(0, sourcesLine.toString() + "\n");
            }
            
            psiDocumentManager.commitDocument(document);
        });
    }
    
    /**
     * Dialog for editing parameters
     */
    private static class ParameterDialog extends DialogWrapper {
        private final DefaultTableModel tableModel;
        private final JBTable paramsTable;
        
        public ParameterDialog(Project project, List<String> currentParams) {
            super(project);
            setTitle("Configure Parameters");
            
            // Create table model
            tableModel = new DefaultTableModel();
            tableModel.addColumn("Parameter");
            
            // Add current parameters
            for (String param : currentParams) {
                tableModel.addRow(new Object[]{param});
            }
            
            // Create table
            paramsTable = new JBTable(tableModel);
            
            init();
        }
        
        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            
            // Add table with scroll pane
            JBScrollPane scrollPane = new JBScrollPane(paramsTable);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            // Add buttons for adding/removing parameters
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addButton = new JButton("Add");
            JButton removeButton = new JButton("Remove");
            
            addButton.addActionListener(e -> tableModel.addRow(new Object[]{""}));
            removeButton.addActionListener(e -> {
                int selectedRow = paramsTable.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                }
            });
            
            buttonPanel.add(addButton);
            buttonPanel.add(removeButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
            
            panel.setPreferredSize(new Dimension(400, 300));
            return panel;
        }
        
        @NotNull
        public List<String> getParameters() {
            List<String> params = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String param = (String) tableModel.getValueAt(i, 0);
                if (param != null && !param.trim().isEmpty()) {
                    params.add(param.trim());
                }
            }
            return params;
        }
    }
    
    /**
     * Dialog for editing source files
     */
    private static class SourceFilesDialog extends DialogWrapper {
        private final DefaultTableModel tableModel;
        private final JBTable filesTable;
        private final Project project;
        
        public SourceFilesDialog(Project project, List<String> currentFiles) {
            super(project);
            this.project = project;
            setTitle("Configure Additional Source Files");
            
            // Create table model
            tableModel = new DefaultTableModel();
            tableModel.addColumn("Source File Path");
            
            // Add current files
            for (String file : currentFiles) {
                tableModel.addRow(new Object[]{file});
            }
            
            // Create table
            filesTable = new JBTable(tableModel);
            
            init();
        }
        
        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            
            // Add table with scroll pane
            JBScrollPane scrollPane = new JBScrollPane(filesTable);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            // Add buttons for browsing/removing files
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton browseButton = new JButton("Browse...");
            JButton removeButton = new JButton("Remove");
            
            browseButton.addActionListener(e -> {
                // Use the IntelliJ file chooser API to select files
                VirtualFile initialDir = SysUtil.getCurrentFileDirectory(project);
                
                FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, true)
                    .withTitle("Select Additional Source Files")
                    .withDescription("Choose C/C++ source files to include in compilation")
                    .withFileFilter(file -> {
                        String extension = file.getExtension();
                        return extension != null && (extension.equals("c") || extension.equals("cpp") || 
                                                     extension.equals("h") || extension.equals("hpp"));
                    });
                
                if (initialDir != null) {
                    descriptor.setRoots(initialDir);
                }
                
                VirtualFile[] chosenFiles = FileChooser.chooseFiles(descriptor, project, initialDir);
                for (VirtualFile file : chosenFiles) {
                    tableModel.addRow(new Object[]{file.getPath()});
                }
            });
            
            removeButton.addActionListener(e -> {
                int selectedRow = filesTable.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                }
            });
            
            buttonPanel.add(browseButton);
            buttonPanel.add(removeButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);
            
            panel.setPreferredSize(new Dimension(500, 300));
            return panel;
        }
        
        @NotNull
        public List<String> getSourceFiles() {
            List<String> files = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String file = (String) tableModel.getValueAt(i, 0);
                if (file != null && !file.trim().isEmpty()) {
                    files.add(file.trim());
                }
            }
            return files;
        }
    }
}
