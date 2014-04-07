package siosio;

import com.intellij.execution.filters.Filter;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleLinkFilter implements Filter {

    public static final String URI_PATTERN = "((https?://|file:/{1,3}|/)[-_.!~*\\\\'()a-zA-Z0-9;\\\\/?:\\\\@&=+\\\\$,%#\\[\\]]+)";
    private static final Pattern BEGINNING_OF_LINE = Pattern.compile("^" + URI_PATTERN);
    private static final Pattern IN_THE_MIDDLE_OF_LINE = Pattern.compile("\\s" + URI_PATTERN);

    @Override
    public Result applyFilter(String textLine, int endPoint) {
        int startPoint = endPoint - textLine.length();
        List<ResultItem> results = new ArrayList<ResultItem>();

        match(results, BEGINNING_OF_LINE, textLine, startPoint);
        match(results, IN_THE_MIDDLE_OF_LINE, textLine, startPoint);

        return new Result(results);
    }

    private void match(List<ResultItem> results, Pattern pattern, String textLine, int startPoint) {
        Matcher matcher = pattern.matcher(textLine);
        while (matcher.find()) {
            String link = matcher.group(1);
            results.add(new Result(
                    startPoint + matcher.start(1),
                    startPoint + matcher.end(1),
                    new OpenUrlHyperlinkInfo(link)
            ));
        }
    }
}
