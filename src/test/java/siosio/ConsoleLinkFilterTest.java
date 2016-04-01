package siosio;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.Filter.Result;
import com.intellij.execution.filters.Filter.ResultItem;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.NonStrictExpectations;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ConsoleLinkFilterTest {

    ConsoleLinkFilter filter = new ConsoleLinkFilter();

    @Before
    public void setUp() throws Exception {
        new NonStrictExpectations(EditorColorsManager.class) {{
            final EditorColorsManager mock = EditorColorsManager.getInstance();
        }};
    }

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
        assertLink("http://selenide.org", 0, "http://selenide.org");
        assertLink("   http://selenide.org", 3, "http://selenide.org");
    }

    @Test
    public void httpsLink() {
        assertLink("https://selenide.org", 0, "https://selenide.org");
        assertLink("   https://selenide.org", 3, "https://selenide.org");
    }

    @Test
    public void fileLink() {
        assertLink("file:///tmp/screenshot.png", 0, "file:///tmp/screenshot.png");
    }

    @Test
    public void fileLinkWithSingleSlash() {
        assertLink("file:/tmp/screenshot.png", 0, "file:/tmp/screenshot.png");
    }

    @Test
    public void localFile() {
        assertLink("/tmp/screenshot.png", 0, "/tmp/screenshot.png");
    }

    @Test
    public void multipleLinks() {
        Result result = filter.applyFilter(
                "http://selenide.org Some text " +
                "file:/tmp/screenshot.png some more text " +
                "https://gopalmer.co.uk", 92);

        List<ResultItem> results = result.getResultItems();
        assertEquals(3, results.size());
        assertResult(results.get(0), 0, "http://selenide.org");
        assertResult(results.get(1), 30, "file:/tmp/screenshot.png");
        assertResult(results.get(2), 70, "https://gopalmer.co.uk");
    }

    @Test
    public void linkShouldBePrefixedWithSeparator() {
        String link = " http://www.ee http://google.com bla?http://not.me";
        Result result = filter.applyFilter(link, link.length());
        List<ResultItem> results = result.getResultItems();
        assertEquals(2, results.size());
        assertResult(results.get(0), 1, "http://www.ee");
        assertResult(results.get(1), 15, "http://google.com");
    }

    @Test
    public void urlWithSquareBraces() {
        String link = "http://ci.org/job/123/artifact/build/reports/checksDialogext[1]/1394746367500.10.png";
        String textLine = "Screenshot: http://ci.org/job/123/artifact/build/reports/checksDialogext[1]/1394746367500.10.png";
        Filter.Result result = filter.applyFilter(textLine, textLine.length());
        List<Filter.ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        assertLink(textLine, "Screenshot: ".length(), link);
    }

    @Test
    public void urlWithBraces() {
        assertNoLinksMatched("Module web is available (/home/xp/work/play/./modules/web-1.4.6)");
    }

    @Test
    public void consoleOutputWithSlashesButNotLinks() {
        assertNoLinksMatched(
                "INFO 13.03.14 9:50:liquibase: db/custom.xml: db/generic.xml::prevent_ddl_locking::Codeborne: Custom SQL executed");
    }

    private void assertNoLinksMatched(String textLine) {
        Result result = filter.applyFilter(textLine, textLine.length());
        List<ResultItem> results = result.getResultItems();

        assertEquals(0, results.size());
    }

    private void assertLink(String textLine, int startIndex, String expectedUrl) {
        Result result = filter.applyFilter(textLine, textLine.length());
        List<ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        ResultItem item = results.get(0);
        assertResult(item, startIndex, expectedUrl);
    }

    private void assertResult(ResultItem item, int startIndex, String expectedUrl) {
        assertEquals(startIndex, item.highlightStartOffset);
        assertEquals(startIndex + expectedUrl.length(), item.highlightEndOffset);
        assertNotNull(item.hyperlinkInfo);
        assertTrue(item.hyperlinkInfo instanceof OpenUrlHyperlinkInfo);
    }
}
