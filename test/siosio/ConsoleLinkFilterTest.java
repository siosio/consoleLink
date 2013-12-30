package siosio;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConsoleLinkFilterTest {
  ConsoleLinkFilter filter = new ConsoleLinkFilter();

  @Test
  public void emptyConsole() {
    assertEquals(0, filter.applyFilter("", 0).highlightStartOffset);
    assertEquals(0, filter.applyFilter("", 0).highlightEndOffset);
    assertNull(filter.applyFilter("", 0).hyperlinkInfo);
  }

  @Test
  public void plainText() {
    assertEquals(0, filter.applyFilter("John Smith", 10).highlightStartOffset);
    assertEquals(10, filter.applyFilter("John Smith", 10).highlightEndOffset);
    assertNull(filter.applyFilter("John Smith", 10).hyperlinkInfo);
  }

  @Test
  public void httpLink() {
    assertEquals(0, filter.applyFilter("http://selenide.org", 19).highlightStartOffset);
    assertEquals(19, filter.applyFilter("http://selenide.org", 19).highlightEndOffset);
    assertNotNull(filter.applyFilter("http://selenide.org", 19).hyperlinkInfo);
  }

  @Test
  public void httpsLink() {
    assertEquals(0, filter.applyFilter("https://selenide.org", 20).highlightStartOffset);
    assertEquals(20, filter.applyFilter("https://selenide.org", 20).highlightEndOffset);
    assertNotNull(filter.applyFilter("https://selenide.org", 20).hyperlinkInfo);
  }

  @Test
  public void fileLink() {
    assertEquals(0, filter.applyFilter("file:///tmp/screenshot.png", 26).highlightStartOffset);
    assertEquals(26, filter.applyFilter("file:///tmp/screenshot.png", 26).highlightEndOffset);
    assertNotNull(filter.applyFilter("file:///tmp/screenshot.png", 26).hyperlinkInfo);
  }
}
