package by.creepid.docgeneration.header;

import java.util.Map;

import by.creepid.docsreporter.converter.DocFormat;

public interface DocsHeaderManager {
		
	public DocFormat getFormatFromAccept(String acceptHeader);
	
	public Map.Entry<String, String> getContentEntry(DocFormat targetFormat);

}
