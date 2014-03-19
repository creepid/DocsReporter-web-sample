package by.creepid.docgeneration.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String REQUEST_MESSAGE = "InvalidRequest";
	
	
	private FieldErrorResource getFieldErrorResource(FieldError fieldError){
		FieldErrorResource fieldErrorResource = new FieldErrorResource();
		
		fieldErrorResource.setResource(fieldError.getObjectName());
		fieldErrorResource.setField(fieldError.getField());
		fieldErrorResource.setCode(fieldError.getCode());
		fieldErrorResource.setMessage(fieldError.getDefaultMessage());
		
		return fieldErrorResource;
	}

	@ExceptionHandler({ InvalidRequestException.class })
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e,
			WebRequest request) {
		InvalidRequestException ire = (InvalidRequestException) e;

		List<FieldErrorResource> fieldErrorResources = new ArrayList<>();

		if (ire.getErrors() != null) {
			List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				FieldErrorResource fieldErrorResource = getFieldErrorResource(fieldError);
				fieldErrorResources.add(fieldErrorResource);
			}
		}

		ErrorResource error = new ErrorResource(REQUEST_MESSAGE, ire.getMessage());
		error.setFieldErrors(fieldErrorResources);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return handleExceptionInternal(e, error, headers,
				HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

}
