package by.creepid.docgeneration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.AbstractMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import by.creepid.docgeneration.RegDocController;
import by.creepid.docgeneration.dao.ListStreetTypeDAO;
import by.creepid.docgeneration.dao.RegionsDAO;
import by.creepid.docgeneration.dao.entity.ListStreetType;
import by.creepid.docgeneration.dao.entity.Regions;
import by.creepid.docgeneration.header.DocsHeaderManager;
import by.creepid.docgeneration.model.FirmReg;
import by.creepid.docgeneration.model.services.FirmRegService;
import by.creepid.docgeneration.validation.InvalidRequestException;
import by.creepid.docsreporter.ReportTemplate;
import by.creepid.docsreporter.converter.DocFormat;

public class RegDocControllerTest {

	private static final String REG_TEST_PATH = "src//test//resources//registration_test.docx";
	private static final String CHOICE_IMG_PATH = "src//test//resources//img//choice.png";

	private ReportTemplate regTemplate;
	private DocsHeaderManager headerManager;
	private Validator firmRegValidator;
	private MessageSource messageSource;
	private FirmRegService firmRegService;

	private FirmReg reg;

	@Before
	public void setUp() throws Exception {
		reg = ModelHelper.createFirmRegSample();

		messageSource = mock(MessageSource.class);
		when(messageSource.getMessage("error.validation", null, Locale.ENGLISH))
				.thenReturn("Validation Errors");
		when(
				messageSource.getMessage("error.validation.template", null,
						Locale.ENGLISH)).thenReturn(
				"Template document fields validation errors");
		when(
				messageSource.getMessage("error.servlet.output", null,
						Locale.ENGLISH)).thenReturn("Servlet response error");

		final RegionsDAO regionsDAO = mock(RegionsDAO.class);
		when(regionsDAO.findById(reg.getRegion())).thenAnswer(
				new Answer<Regions>() {

					private final String region = "Гродненская обл.";

					@Override
					public Regions answer(InvocationOnMock invocation)
							throws Throwable {
						Regions regions = new Regions();
						regions.setId((Long) invocation.getArguments()[0]);
						regions.setValue(region);

						reg.setRegionStr(region);
						return regions;
					}
				});


		final ListStreetTypeDAO listStreetTypeDAO = mock(ListStreetTypeDAO.class);
		when(listStreetTypeDAO.findById(reg.getStreet_type())).thenAnswer(
				new Answer<ListStreetType>() {

					private final String streetType = "пл.";

					@Override
					public ListStreetType answer(InvocationOnMock invocation)
							throws Throwable {
						ListStreetType type = new ListStreetType();
						type.setId((Long) invocation.getArguments()[0]);
						type.setValue(streetType);

						reg.setStreet_typeStr(streetType);
						return type;
					}
				});


		firmRegService = mock(FirmRegService.class);
		final byte[] choice = FileUtil.getBytes(CHOICE_IMG_PATH);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				FirmReg reg = (FirmReg) invocation.getArguments()[0];
				switch (reg.getType_connection()) {
				case web:
					reg.setWebChoice(choice);
					break;
				case offline:
					reg.setOfflineChoice(choice);
					break;
				default:
					break;
				}

				return null;
			}
		}).when(firmRegService).fillChoice(any(FirmReg.class));

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				FirmReg reg = (FirmReg) invocation.getArguments()[0];

				reg.setRegionStr(regionsDAO.findById(reg.getRegion())
						.getValue());

				return null;
			}
		}).when(firmRegService).fillRegionStr(any(FirmReg.class));

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				FirmReg reg = (FirmReg) invocation.getArguments()[0];

				reg.setStreet_typeStr(listStreetTypeDAO.findById(
						reg.getStreet_type()).getValue());

				return null;
			}
		}).when(firmRegService).fillStreet_typeStr(any(FirmReg.class));

		regTemplate = mock(ReportTemplate.class);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] data = FileUtil.getBytes(REG_TEST_PATH);
		output.write(data);

		when(regTemplate.generateReport(DocFormat.DOCX, reg, null)).thenReturn(
				output);

		headerManager = mock(DocsHeaderManager.class);
		when(headerManager.getFormatFromAccept(anyString())).thenReturn(
				DocFormat.DOCX);

		Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(
				"Content-Disposition",
				"attachment; filename=registration_template.docx");
		when(headerManager.getContentEntry(DocFormat.DOCX)).thenReturn(entry);

		firmRegValidator = mock(Validator.class);

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Errors fieldErrors = (Errors) invocation.getArguments()[1];

				fieldErrors.rejectValue("gln", "invalid.gtin", "invalid field");

				return null;
			}
		}).when(firmRegValidator).validate(any(FirmReg.class),
				any(Errors.class));

	}

	@After
	public void tearDown() throws Exception {
		regTemplate = null;
		headerManager = null;
		firmRegValidator = null;
		messageSource = null;
		firmRegService = null;
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test(expected = InvalidRequestException.class)
	public final void testCreate() {

		HttpServletResponse response = new MockHttpServletResponse();
		Locale locale = Locale.ENGLISH;

		RegDocController controller = new RegDocController();

		ReflectionTestUtils.setField(controller, "regTemplate", regTemplate);
		ReflectionTestUtils
				.setField(controller, "headerManager", headerManager);
		ReflectionTestUtils.setField(controller, "firmRegValidator",
				firmRegValidator);
		ReflectionTestUtils
				.setField(controller, "messageSource", messageSource);
		ReflectionTestUtils.setField(controller, "firmRegService", firmRegService);

		try {
			controller
					.create(reg,
							"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
							response, locale);

			fail("No exception caught");
		} catch (InvalidRequestException ex) {
			Errors errors = ex.getErrors();

			FieldError gtinError = errors.getFieldErrors().get(0);
			assertEquals("invalid.gtin", gtinError.getCode());
			assertEquals("invalid field", gtinError.getDefaultMessage());
			assertEquals("firmReg", gtinError.getObjectName());

			throw ex;
		}

		thrown.expect(InvalidRequestException.class);
		thrown.expectMessage("Validation Errors");
	}

}
