package com.zohocontacts.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {

	public static boolean validateUserPassword(String password) {

		if (password.length() <= 72 && containsLowercase(password) && containsUppercase(password)
				&& containsNumber(password)) {

			return true;
		}

		return false;
	}

	private static boolean containsLowercase(String str) {
		return str.matches(".*[a-z].*");
	}

	private static boolean containsUppercase(String str) {
		return str.matches(".*[A-Z].*");
	}

	private static boolean containsNumber(String str) {
		return str.matches(".*[0-9].*");
	}

	private static boolean isvalidEmail(String mail) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(mail);
		if (matcher.matches()) {

			return true;
		} else {
			return false;
		}

	}

	public static boolean validatePhoneNumber(String phoneNumber) {
		String phoneRegex = "^\\d+";

		Pattern pattern = Pattern.compile(phoneRegex);

		Matcher matcher = pattern.matcher(phoneNumber);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidEmail(String email) {

		if (isvalidEmail(email)) {

			return true;
		}

		return false;

	}

	public static boolean validateEmail(String[] emails) {
		for (String email : emails) {

			if (!isvalidEmail(email)) {

				return false;
			}

		}

		return true;

	}

	public static boolean isNumberValid(String number) {

		if (validatePhoneNumber(number)) {

			return true;
		}

		return false;

	}

	public static boolean validatePhoneNumber(String[] phoneNumber) {
		String phoneRegex = "^\\d+";
		for (String phone : phoneNumber) {

			Pattern pattern = Pattern.compile(phoneRegex);

			Matcher matcher = pattern.matcher(phone);

			if (!matcher.matches()) {

				return false;
			}

		}
		return true;

	}
}
