package by.creepid.docgeneration.validation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import by.creepid.docgeneration.model.FirmReg;

@Service("firmRegValidator")
public class FirmRegValidator implements
		org.springframework.validation.Validator {

	private Map<Class<? extends Annotation>, String> CODES = new HashMap<>();

	@PostConstruct
	private void fillCodes() {
		CODES.put(NotEmpty.class, "required");
		CODES.put(NotNull.class, "required");
		CODES.put(Size.class, "invalid.length");
		CODES.put(Max.class, "invalid.value.max");
		CODES.put(Min.class, "invalid.value.min");
		CODES.put(Pattern.class, "pattern.match");
		CODES.put(GtinCheck.class, "invalid.gtin");
	}

	private String getCode(ConstraintViolation<FirmReg> constraintViolation) {
		StringBuilder builder = new StringBuilder();
		
		Class<? extends Annotation> annot = constraintViolation
				.getConstraintDescriptor().getAnnotation().annotationType();

		if (CODES.containsKey(annot)) {
			builder.append(constraintViolation.getPropertyPath().toString())
					.append(".").append(CODES.get(annot));
		}

		return builder.toString();

	}

	@Autowired
	private Validator validator;

	@Override
	public boolean supports(Class<?> clazz) {
		return FirmReg.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		FirmReg target = (FirmReg) obj;

		Set<ConstraintViolation<FirmReg>> violations = validator.validate(target);
		
		for (ConstraintViolation<FirmReg> constraintViolation : violations) {
			
			errors.rejectValue(
					constraintViolation.getPropertyPath().toString(),
					getCode(constraintViolation),
					constraintViolation.getMessage());

		}
		
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
