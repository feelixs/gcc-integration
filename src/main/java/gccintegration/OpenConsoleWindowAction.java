package gccintegration;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class OpenConsoleWindowAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        // Get the tool window and activate it
        ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow("Run New Executable (C/C++)");
        if (window != null) {
            // Ensure the console view is initialized
            SysUtil.getStoredConsole(project);
            window.activate(null);
            
            // Show options popup
            showOptionsPopup(event, project);
        }
    }
    
    private void showOptionsPopup(AnActionEvent event, Project project) {
        DefaultActionGroup popupGroup = new DefaultActionGroup();
        
        // Add parameter configuration action
        popupGroup.add(new AnAction("Configure Parameters...") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                // Show parameter configuration dialog
                OptionParse.configureParameters(project);
            }
        });
        
        // Add source files configuration action
        popupGroup.add(new AnAction("Additional Source Files...") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                // Show source files configuration dialog
                OptionParse.configureSourceFiles(project);
            }
        });
        
        ListPopup popup = JBPopupFactory.getInstance()
                .createActionGroupPopup(
                        "Compilation Options",
                        popupGroup,
                        DataContext.EMPTY_CONTEXT,
                        JBPopupFactory.ActionSelectionAid.NUMBERING,
                        true
                );
        
        Component component = event.getInputEvent().getComponent();
        if (component != null) {
            popup.show(new RelativePoint(component, new Point(0, 0)));
        } else {
            // Fallback if component is null
            popup.showInFocusCenter();
        }
    }
}