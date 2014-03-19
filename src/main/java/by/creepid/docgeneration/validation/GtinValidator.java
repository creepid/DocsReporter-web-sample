package by.creepid.docgeneration.validation;

import java.util.HashSet;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GtinValidator implements ConstraintValidator<GtinCheck, String> {

	private static final HashSet<Integer> LENGTHS = new HashSet<Integer>();

	static {
		LENGTHS.add(7);
		LENGTHS.add(13);
		LENGTHS.add(14);
	}

	private String getControlNumber(String instr) {

		int[] narr = new int[15];
		int step1, step2, step3, step4, step5;

		int index = 1;
		for (int i = instr.length(); i != 0; i--) {
			int ntemp = Integer.parseInt(instr.substring(i - 1, i));
			
			narr[++index] = ntemp;
		}

		step1 = narr[14] + narr[12] + narr[10] + narr[8] + narr[6] + narr[4] + narr[2];
		step2 = step1 * 3;
		step3 = narr[13] + narr[11] + narr[9] + narr[7] + narr[5] + narr[3];
		step4 = step2 + step3;
		step5 = step4 % 10;
		if (step5 != 0) {
			step5 = 10 - step5;
		}

		return Integer.toString(step5);
	}

	@Override
	public void initialize(GtinCheck constraintAnnotation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true;
		}

		try {
			int len = value.length();
			if (!LENGTHS.contains(len)) {
				return false;
			}

			if (getControlNumber(value.substring(0, len - 1)).equalsIgnoreCase(
					value.substring(len - 1))) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
