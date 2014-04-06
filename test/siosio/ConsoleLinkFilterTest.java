package siosio;

import com.intellij.execution.filters.Filter.Result;
import com.intellij.execution.filters.Filter.ResultItem;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ConsoleLinkFilterTest {
    ConsoleLinkFilter filter = new ConsoleLinkFilter();

    @Test
    public void emptyConsole() {
        assertNoLinksMatched("");
    }

    @Test
    public void plainText() {
        assertNoLinksMatched("John Smith");
    }

    @Test
    public void httpLink() {
        assertLink("http://selenide.org", "http://selenide.org");
    }

    @Test
    public void httpsLink() {
        assertLink("https://selenide.org", "https://selenide.org");
    }

    @Test
    public void fileLink() {
        assertLink("file:///tmp/screenshot.png", "file:///tmp/screenshot.png");
    }

    @Test
    public void fileLinkWithSingleSlash() {
        assertLink("file:/tmp/screenshot.png", "file:/tmp/screenshot.png");
    }

    @Test
    public void localFile() {
        assertLink("/tmp/screenshot.png", "/tmp/screenshot.png");
    }

    @Test
    public void multipleLinks() {
        Result result = filter.applyFilter(
                "http://selenide.org Some text " +
                "file:/tmp/screenshot.png some more text " +
                "https://gopalmer.co.uk", 92);

        List<ResultItem> results = result.getResultItems();
        assertResult(results.get(0), 0, "http://selenide.org");
        assertResult(results.get(1), 30, "file:/tmp/screenshot.png");
        assertResult(results.get(2), 70, "https://gopalmer.co.uk");
    }

    private void assertNoLinksMatched(String textLine) {
        Result result = filter.applyFilter(textLine, textLine.length());
        List<ResultItem> results = result.getResultItems();

        assertEquals(0, results.size());
    }

    private void assertLink(String expectedUrl, String textLine) {
        Result result = filter.applyFilter(textLine, textLine.length());
        List<ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        ResultItem item = results.get(0);
        assertResult(item, 0, expectedUrl);
    }

    private void assertResult(ResultItem item, int startIndex, String expectedUrl) {
        assertEquals(startIndex, item.highlightStartOffset);
        assertEquals(startIndex + expectedUrl.length(), item.highlightEndOffset);
        assertNotNull(item.hyperlinkInfo);
        assertTrue(item.hyperlinkInfo instanceof OpenUrlHyperlinkInfo);
    }
}
