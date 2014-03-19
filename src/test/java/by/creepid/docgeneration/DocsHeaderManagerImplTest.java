package by.creepid.docgeneration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import by.creepid.docgeneration.header.DocsHeaderManagerImpl;
import by.creepid.docsreporter.converter.DocFormat;

public class DocsHeaderManagerImplTest {

	private static final String path = "/C:/docums/file_name.docx";

	private DocsHeaderManagerImpl manager;

	@Before
	public void setUp() throws Exception {
		manager = new DocsHeaderManagerImpl(path);
	}

	@After
	public void tearDown() throws Exception {
		manager = null;
	}

	@Test
	public final void testGetFormatFromAccept() {
		DocFormat format = manager.getFormatFromAccept("application/pdf");
		assertSame(DocFormat.PDF, format);

	}

	@Test
	public final void testGetContentEntry() {
		Map.Entry<String, String> entry = manager
				.getContentEntry(DocFormat.PDF);

		assertEquals("Content-Disposition", entry.getKey());
		assertEquals("attachment; filename=file_name.pdf", entry.getValue());
	}

}
