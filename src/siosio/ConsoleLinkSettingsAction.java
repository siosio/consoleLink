package siosio;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConsoleLinkSettingsAction extends ActionGroup {


    public ConsoleLinkSettingsAction() {
        super("hyperlink Setting", true);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent event) {
        if (Configuration.isIncludeNativePath()) {
            return new AnAction[] {new Off()};
        } else {
            return new AnAction[] {new On()};
        }
    }

    public static class On extends AnAction {

        public On() {
            super("enable local path");
        }

        @Override
        public void actionPerformed(AnActionEvent event) {
            Configuration.includeNativePath();
        }
    }

    public static class Off extends AnAction {

        public Off() {
            super("disable local path");
        }

        @Override
        public void actionPerformed(AnActionEvent event) {
            Configuration.excludeNativePath();
        }
    }
}
