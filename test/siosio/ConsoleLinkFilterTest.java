package siosio;

import com.intellij.execution.filters.Filter.Result;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConsoleLinkFilterTest {
  ConsoleLinkFilter filter = new ConsoleLinkFilter();

  @Test
  public void emptyConsole() {
    Result result = filter.applyFilter("", 0);
    assertEquals(0, result.highlightStartOffset);
    assertEquals(0, result.highlightEndOffset);
    assertNull(result.hyperlinkInfo);
  }

  @Test
  public void plainText() {
    Result result = filter.applyFilter("John Smith", 10);
    assertEquals(0, result.highlightStartOffset);
    assertEquals(10, result.highlightEndOffset);
    assertNull(result.hyperlinkInfo);
  }

  @Test
  public void httpLink() {
    Result result = filter.applyFilter("http://selenide.org", 19);
    assertEquals(0, result.highlightStartOffset);
    assertEquals(19, result.highlightEndOffset);
    assertNotNull(result.hyperlinkInfo);
  }

  @Test
  public void httpsLink() {
    Result result = filter.applyFilter("https://selenide.org", 20);
    assertEquals(0, result.highlightStartOffset);
    assertEquals(20, result.highlightEndOffset);
    assertNotNull(result.hyperlinkInfo);
  }

  @Test
  public void fileLink() {
    Result result = filter.applyFilter("file:///tmp/screenshot.png", 26);
    assertEquals(0, result.highlightStartOffset);
    assertEquals(26, result.highlightEndOffset);
    assertNotNull(result.hyperlinkInfo);
  }

  @Test
  public void fileLinkWithSingleSlash() {
    Result result = filter.applyFilter("file:/tmp/screenshot.png", 24);
    assertEquals(0, result.highlightStartOffset);
    assertEquals(24, result.highlightEndOffset);
    assertNotNull(result.hyperlinkInfo);
  }
}
