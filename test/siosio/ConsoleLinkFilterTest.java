package siosio;

import com.intellij.execution.filters.Filter.Result;
import com.intellij.execution.filters.Filter.ResultItem;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConsoleLinkFilterTest {
    ConsoleLinkFilter filter = new ConsoleLinkFilter();

    @Test
    public void emptyConsole() {
        Result result = filter.applyFilter("", 0);
        List<ResultItem> results = result.getResultItems();

        assertEquals(0, results.size());
    }

    @Test
    public void plainText() {
        Result result = filter.applyFilter("John Smith", 10);
        List<ResultItem> results = result.getResultItems();

        assertEquals(0, results.size());
    }

    @Test
    public void httpLink() {
        Result result = filter.applyFilter("http://selenide.org", 19);
        List<ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        assertEquals(0, results.get(0).highlightStartOffset);
        assertEquals(19, results.get(0).highlightEndOffset);
        assertNotNull(results.get(0).hyperlinkInfo);
    }

    @Test
    public void httpsLink() {
        Result result = filter.applyFilter("https://selenide.org", 20);
        List<ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        assertEquals(0, results.get(0).highlightStartOffset);
        assertEquals(20, results.get(0).highlightEndOffset);
        assertNotNull(results.get(0).hyperlinkInfo);
    }

    @Test
    public void fileLink() {
        Result result = filter.applyFilter("file:///tmp/screenshot.png", 26);
        List<ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        assertEquals(0, results.get(0).highlightStartOffset);
        assertEquals(26, results.get(0).highlightEndOffset);
        assertNotNull(results.get(0).hyperlinkInfo);
    }

    @Test
    public void fileLinkWithSingleSlash() {
        Result result = filter.applyFilter("file:/tmp/screenshot.png", 24);
        List<ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        assertEquals(0, results.get(0).highlightStartOffset);
        assertEquals(24, results.get(0).highlightEndOffset);
        assertNotNull(results.get(0).hyperlinkInfo);
    }

    @Test
    public void LinkWithSingleSlash() {
        Result result = filter.applyFilter("/tmp/screenshot.png", 19);
        List<ResultItem> results = result.getResultItems();

        assertEquals(1, results.size());
        assertEquals(0, results.get(0).highlightStartOffset);
        assertEquals(19, results.get(0).highlightEndOffset);
        assertNotNull(results.get(0).hyperlinkInfo);
    }

    @Test
    public void MultiLinks() {
        Result result = filter.applyFilter(
                "http://selenide.org Some text " +
                "file:/tmp/screenshot.png some more text " +
                "https://gopalmer.co.uk", 92);

        List<ResultItem> results = result.getResultItems();

        //http://selenide.org
        ResultItem curResult = results.get(0);
        assertEquals(0, curResult.highlightStartOffset);
        assertEquals(19, curResult.highlightEndOffset);
        assertNotNull(curResult.hyperlinkInfo);

        //file:/tmp/screenshot.png
        curResult = results.get(1);
        assertEquals(30, curResult.highlightStartOffset);
        assertEquals(54, curResult.highlightEndOffset);
        assertNotNull(curResult.hyperlinkInfo);

        //https://gopalmer.co.uk
        curResult = results.get(2);
        assertEquals(70, curResult.highlightStartOffset);
        assertEquals(92, curResult.highlightEndOffset);
        assertNotNull(curResult.hyperlinkInfo);
    }
}
