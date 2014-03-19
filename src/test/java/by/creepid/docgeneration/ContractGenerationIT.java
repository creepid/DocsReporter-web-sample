package by.creepid.docgeneration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import by.creepid.docgeneration.model.FirmReg;
import by.creepid.docsreporter.ReportTemplate;
import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration("file:src/test/resources/ExampleConfigurationTests-context.xml")
public class ContractGenerationIT {

	@Autowired
	@Qualifier("regTemplate")
	private ReportTemplate regTemplate;

	private static final DocFormat format = DocFormat.DOCX;

	private static final String DOC_OUT = "src//test//resources//out."
			+ format.getExts()[0];
	
	private static final String CHOICE_IMG_PATH = "src//test//resources//img//choice.png";

	@Test
	public final void testGenerate() throws IOException {
		FirmReg reg = ModelHelper.createFirmRegSample();
		byte[] choice = FileUtil.getBytes(CHOICE_IMG_PATH);
		reg.setOfflineChoice(choice);
		
		FileOutputStream fop = null;
		File file;
		try {
			ByteArrayOutputStream out = (ByteArrayOutputStream) regTemplate
					.generateReport(format, reg, null);

			file = new File(DOC_OUT);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			fop.write(out.toByteArray());
			fop.flush();
			fop.close();
			
			assertTrue(file.exists() && file.length()!=0);
		} catch (ReportProcessingException e) {
			e.printStackTrace();
			
			Errors errors = e.getErrors();
			List<FieldError> fields = errors.getFieldErrors();
			for (FieldError fieldError : fields) {
				System.out.println(fieldError.getField());
				System.out.println(fieldError.getCode());
			}
			
			fail("ReportProcessingException occured");
			// TODO Auto-generated catch block
			
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
