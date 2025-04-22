package gccintegration;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;

public class ConsoleWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // get the console view that was created in the startup activity
        ConsoleView gccConsole = SysUtil.getStoredConsole(project);
        
        // Create actions for toolbar
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(ActionManager.getInstance().getAction("GccCompileCurrentFile"));
        
        // Create toolbar
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(
                "GccConsoleToolbar", actionGroup, false);
        
        // Create panel with toolbar and console
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(toolbar.getComponent(), BorderLayout.WEST);
        panel.add(gccConsole.getComponent(), BorderLayout.CENTER);
        ContentFactory contentFactory = ContentFactory.getInstance();
        toolWindow.getContentManager().addContent(contentFactory.createContent(panel, "Run", false));
    }
}