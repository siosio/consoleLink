package siosio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intellij.execution.filters.Filter;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;
import com.intellij.openapi.editor.markup.TextAttributes;

public class ConsoleLinkFilter implements Filter {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "(https?://[-_.!~*\\\\'()a-zA-Z0-9;\\\\/?:\\\\@&=+\\\\$,%#]+)");

    @Override
    public Result applyFilter(String s, int endPoint) {
        int startPoint = endPoint - s.length();
        Matcher matcher = URL_PATTERN.matcher(s);
        if (matcher.find()) {
            return new Result(startPoint + matcher.start(),
                    startPoint + matcher.end(), new OpenUrlHyperlinkInfo(matcher.group(1)));
        } else {
            return new Result(startPoint, endPoint, null, new TextAttributes());
        }
    }
}
