package siosio;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ConsoleLinkFilterProvider implements ConsoleFilterProvider {

    @NotNull
    @Override
    public Filter[] getDefaultFilters(@NotNull Project project) {
        Filter filter = new ConsoleLinkFilter();
        return new Filter[]{filter};
    }
}
