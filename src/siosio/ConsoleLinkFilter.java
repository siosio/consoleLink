package siosio;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.execution.filters.Filter;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;

public class ConsoleLinkFilter implements Filter {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "((https?://|file:/{1,3})[-_.!~*\\\\'()a-zA-Z0-9;\\\\/?:\\\\@&=+\\\\$,%#]+)");

    private static final Pattern URL_PATTERN_INCLUDE_NATIVE_FILE_PATH = Pattern.compile(
            "((https?://|file:/{1,3}|([ ]/|^/))[-_.!~*\\\\'()a-zA-Z0-9;\\\\/?:\\\\@&=+\\\\$,%#]+)");

    @Override
    public Result applyFilter(String s, int endPoint) {
        int startPoint = endPoint - s.length();
        List<ResultItem> results = new ArrayList<ResultItem>();

        Matcher matcher;
        if (Configuration.isIncludeNativePath()) {
            matcher = URL_PATTERN_INCLUDE_NATIVE_FILE_PATH.matcher(s);
        } else {
            matcher = URL_PATTERN.matcher(s);
        }
        while (matcher.find()) {
            results.add(new Result(startPoint + matcher.start(),
                    startPoint + matcher.end(), new OpenUrlHyperlinkInfo(matcher.group(1))));
        }

        return new Result(results);
    }
}
