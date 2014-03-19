package by.creepid.docgeneration.header;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Map;

import by.creepid.docsreporter.converter.DocFormat;

public class DocsHeaderManagerImpl implements DocsHeaderManager {
	
    public static final Map.Entry<String, String> ATTACH_PATTERN_ENTRY = new AbstractMap.SimpleEntry<>("Content-Disposition", "attachment; filename=%s");

	private DocFormat templateFormat;
	private String templatePath;

	public DocsHeaderManagerImpl(String templatePath) {
		this.templatePath = templatePath;
		templateFormat = DocFormat.getFormat(templatePath);
	}

	public DocFormat getFormatFromAccept(String acceptHeader) {

		if (acceptHeader == null || acceptHeader.isEmpty()) {
			return templateFormat;
		}

		DocFormat[] formats = DocFormat.values();
		for (DocFormat docFormat : formats) {

			if (acceptHeader.equalsIgnoreCase(docFormat.getMimeType())) {
				return docFormat;
			}
		}

		return templateFormat;

	}
	
	public Map.Entry<String, String> getContentEntry(DocFormat targetFormat){
        //if contain ":" path without "/", otherwise - without slash
        int beginIndex = (templatePath.indexOf(":") != -1 && templatePath.startsWith("/")) ? 1 : 0;
		
		Path path = Paths.get(templatePath.substring(beginIndex));
		String templateFilename = path.getFileName().toString();
		
		String value = targetFormat.changeExt(templateFilename);
		
		Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(ATTACH_PATTERN_ENTRY.getKey(), 
				String.format(ATTACH_PATTERN_ENTRY.getValue(), value));
		
		return entry;
		
		
	}
	
	

}
