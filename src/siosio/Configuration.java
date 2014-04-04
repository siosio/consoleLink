package siosio;

import com.intellij.ide.util.PropertiesComponent;

public class Configuration {

    private static final String CONSOLE_LINK_PATH_ENABLE = "console-link-path-enable";


    private Configuration() {
    }

    public static boolean isIncludeNativePath() {
        PropertiesComponent instance = PropertiesComponent.getInstance();
        return instance.getBoolean(CONSOLE_LINK_PATH_ENABLE, false);
    }

    public static void includeNativePath() {
        PropertiesComponent instance = PropertiesComponent.getInstance();
        instance.setValue(CONSOLE_LINK_PATH_ENABLE, "true");
    }

    public static void excludeNativePath() {
        PropertiesComponent instance = PropertiesComponent.getInstance();
        instance.setValue(CONSOLE_LINK_PATH_ENABLE, "false");
    }
}
