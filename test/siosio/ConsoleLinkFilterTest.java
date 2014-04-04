package siosio;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.intellij.execution.filters.Filter.Result;
import com.intellij.execution.filters.Filter.ResultItem;

import org.junit.Test;

public class ConsoleLinkFilterTest {
    ConsoleLinkFilter filter = new ConsoleLinkFilter();

    @Test
    public void emptyConsole() {
        Result result = filter.applyFilter("", 0);
        List<ResultItem> results = result.getResultItems();

        assertThat("not hyperlink", results.isEmpty(), is(true));
    }

    @Test
    public void plainText() {
        Result result = filter.applyFilter("John Smith", 10);
        List<ResultItem> results = result.getResultItems();

        assertThat("not hyperlink", results.isEmpty(), is(true));
    }

    @Test
    public void httpLink() {
        String text = "http://selenide.org";
        Result result = filter.applyFilter(text, text.length());
        List<ResultItem> results = result.getResultItems();

        assertThat(results.size(), is(1));
        assertThat(results.get(0).highlightStartOffset, is(0));
        assertThat(results.get(0).highlightEndOffset, is(text.length()));
        assertThat(results.get(0).hyperlinkInfo, is(notNullValue()));
    }

    @Test
    public void httpsLink() {
        String text = "https://selenide.org";
        Result result = filter.applyFilter(text, text.length());
        List<ResultItem> results = result.getResultItems();

        assertThat(results.size(), is(1));
        assertThat(results.get(0).highlightStartOffset, is(0));
        assertThat(results.get(0).highlightEndOffset, is(text.length()));
        assertThat(results.get(0).hyperlinkInfo, is(notNullValue()));
    }

    @Test
    public void fileLink() {
        String text = "file:///tmp/screenshot.png";
        Result result = filter.applyFilter(text, text.length());
        List<ResultItem> results = result.getResultItems();

        assertThat(results.size(), is(1));
        assertThat(results.get(0).highlightStartOffset, is(0));
        assertThat(results.get(0).highlightEndOffset, is(text.length()));
        assertThat(results.get(0).hyperlinkInfo, is(notNullValue()));
    }

    @Test
    public void fileLinkWithSingleSlash() {
        String text = "file:/tmp/screenshot.png";
        Result result = filter.applyFilter(text, text.length());
        List<ResultItem> results = result.getResultItems();

        assertThat(results.size(), is(1));
        assertThat(results.get(0).highlightStartOffset, is(0));
        assertThat(results.get(0).highlightEndOffset, is(text.length()));
        assertThat(results.get(0).hyperlinkInfo, is(notNullValue()));
    }

    @Test
    public void LinkWithSingleSlash() {
        String text = "/tmp/screenshot.png";
        Result result = filter.applyFilter(text, text.length());
        List<ResultItem> results = result.getResultItems();

        assertThat(results.size(), is(1));
        assertThat(results.get(0).highlightStartOffset, is(0));
        assertThat(results.get(0).highlightEndOffset, is(text.length()));
        assertThat(results.get(0).hyperlinkInfo, is(notNullValue()));
    }

    @Test
    public void MultiLinks() {
        StringBuilder text = new StringBuilder();
        text.append("http://selenide.org");
        text.append(" ");
        text.append("Some text");
        text.append(" ");
        text.append("file:/tmp/screenshot.png");
        text.append(" ");
        text.append("some more text");
        text.append(" ");
        text.append("https://gopalmer.co.uk");

        Result result = filter.applyFilter(text.toString(), text.length());

        List < ResultItem > results = result.getResultItems();
        assertThat("contain hyperlink must be 3", results.size(), is(3));

        //http://selenide.org
        assertThat(results.get(0).highlightStartOffset, is(0));
        assertThat(results.get(0).highlightEndOffset, is(19));
        assertThat(results.get(0).hyperlinkInfo, is(notNullValue()));


        //file:/tmp/screenshot.png
        ResultItem curResult = results.get(1);
        assertThat(results.get(1).highlightStartOffset, is(30));
        assertThat(results.get(1).highlightEndOffset, is(54));
        assertThat(results.get(1).hyperlinkInfo, is(notNullValue()));

        //https://gopalmer.co.uk
        assertThat(results.get(2).highlightStartOffset, is(70));
        assertThat(results.get(2).highlightEndOffset, is(92));
        assertThat(results.get(2).hyperlinkInfo, is(notNullValue()));
    }

    @Test
    public void jdbcUrlToPlainText() {
        String text = "jdbc:oracle:oracle:@localhost:1521/xe";
        Result result = filter.applyFilter(text, text.length());

        List<ResultItem> results = result.getResultItems();
        assertThat("not hyperlink", results.isEmpty(), is(true));
    }

    @Test
    public void classSchemePlainText() {
        String text = "classpath:hoge/fuga.xml";

        Result result = filter.applyFilter(text, text.length());

        List<ResultItem> results = result.getResultItems();
        assertThat("not hyperlink", results.isEmpty(), is(true));
    }


}
