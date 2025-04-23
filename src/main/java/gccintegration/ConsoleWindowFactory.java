package gccintegration;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConsoleWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // get the console view that was created in the startup activity
        ConsoleView gccConsole = SysUtil.getStoredConsole(project);
        
        // Create actions for toolbar
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        AnAction compileAction = ActionManager.getInstance().getAction("GccCompileCurrentFile");
        actionGroup.add(compileAction);
        
        // Create toolbar
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("GccConsoleToolbar", actionGroup, false);
        
        // Add popup menu trigger
        JComponent toolbarComponent = toolbar.getComponent();
        toolbarComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = e.getComponent();
                Component source = SwingUtilities.getDeepestComponentAt(component, e.getX(), e.getY());
                
                // Check if the click is on the compile action button
                if (source instanceof JComponent) {
                    JComponent sourceComponent = (JComponent) source;
                    if (sourceComponent.getToolTipText() != null && 
                        sourceComponent.getToolTipText().contains("Compile and Run")) {
                        // Right-click or long press should show options
                        if (e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {
                            showOptionsPopup(e, project);
                            e.consume();
                        }
                    }
                }
            }
        });
        
        // Create panel with toolbar and console
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(toolbar.getComponent(), BorderLayout.WEST);
        panel.add(gccConsole.getComponent(), BorderLayout.CENTER);
        ContentFactory contentFactory = ContentFactory.getInstance();
        toolWindow.getContentManager().addContent(contentFactory.createContent(panel, "Run", false));
    }
    
    private void showOptionsPopup(MouseEvent e, Project project) {
        DefaultActionGroup popupGroup = new DefaultActionGroup();
        
        // Add parameter configuration action
        popupGroup.add(new AnAction("Configure Parameters...") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent event) {
                // Show parameter configuration dialog
                OptionParse.configureParameters(project);
            }
        });
        
        // Add source files configuration action
        popupGroup.add(new AnAction("Additional Source Files...") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent event) {
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
        
        popup.show(new RelativePoint(e));
    }
}