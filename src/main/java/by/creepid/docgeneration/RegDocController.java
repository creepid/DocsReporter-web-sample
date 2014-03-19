package by.creepid.docgeneration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import by.creepid.docgeneration.converters.ConnectionTypeConverter;
import by.creepid.docgeneration.dao.ListStreetTypeDAO;
import by.creepid.docgeneration.dao.RegionsDAO;
import by.creepid.docgeneration.header.DocsHeaderManager;
import by.creepid.docgeneration.model.ConnectionType;
import by.creepid.docgeneration.model.FirmReg;
import by.creepid.docgeneration.model.services.FirmRegService;
import by.creepid.docgeneration.validation.InvalidRequestException;
import by.creepid.docsreporter.ReportTemplate;
import by.creepid.docsreporter.context.validation.ReportProcessingException;
import by.creepid.docsreporter.converter.DocFormat;

@Controller
@RequestMapping(value = "/reg")
public class RegDocController {

	private static final Logger logger = LoggerFactory
			.getLogger(RegDocController.class);

	@Autowired
	@Qualifier("regTemplate")
	private ReportTemplate regTemplate;

	@Autowired
	@Qualifier("regHeaderManager")
	private DocsHeaderManager headerManager;

	@Autowired
	@Qualifier("firmRegValidator")
	private Validator firmRegValidator;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private FirmRegService firmRegService;

	private FirmReg jsonIgnoreMapping(FirmReg reg) {

		firmRegService.fillChoice(reg);
		firmRegService.fillRegionStr(reg);
		firmRegService.fillStreet_typeStr(reg);

		return reg;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(ConnectionType.class,
				new ConnectionTypeConverter());
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @throws IOException
	 * @throws ReportProcessingException
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST, headers = { "Content-Type=application/json" })
	@ResponseBody
	public void create(@RequestBody FirmReg firmReg,
			@RequestHeader("Accept") String acceptType,
			HttpServletResponse response, Locale locale) {

		Errors fieldErrors = new BeanPropertyBindingResult(firmReg, "firmReg");
		firmRegValidator.validate(firmReg, fieldErrors);

		if (fieldErrors.hasErrors()) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			String errorMessage = messageSource.getMessage("error.validation",
					null, locale);
			throw new InvalidRequestException(errorMessage, fieldErrors);
		}

		DocFormat targetFormat = headerManager.getFormatFromAccept(acceptType);

		ByteArrayOutputStream output;
		try {
				output = (ByteArrayOutputStream) regTemplate.generateReport(
						targetFormat, jsonIgnoreMapping(firmReg), null);
		} catch (ReportProcessingException ex) {
			ex.printStackTrace();
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			String errorMessage = messageSource.getMessage(
					"error.validation.template", null, locale);
			throw new InvalidRequestException(errorMessage, ex.getErrors());
		}
		
		response.setContentType(targetFormat.getMimeType());

		Map.Entry<String, String> attachEntry = headerManager
				.getContentEntry(targetFormat);
		response.addHeader(attachEntry.getKey(), attachEntry.getValue());

		try {
			ServletOutputStream stream = response.getOutputStream();

			response.setContentLength(output.size());
			stream.write(output.toByteArray());

		} catch (IOException ex) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			String errorMessage = messageSource.getMessage(
					"error.servlet.output", null, locale);
			throw new InvalidRequestException(errorMessage, null);
		}

		response.setStatus(HttpServletResponse.SC_CREATED);

	}

	public void setRegTemplate(ReportTemplate regTemplate) {
		this.regTemplate = regTemplate;
	}

	public void setHeaderManager(DocsHeaderManager headerManager) {
		this.headerManager = headerManager;
	}

	public void setFirmRegValidator(Validator firmRegValidator) {
		this.firmRegValidator = firmRegValidator;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setFirmRegService(FirmRegService firmRegService) {
		this.firmRegService = firmRegService;
	}
}
