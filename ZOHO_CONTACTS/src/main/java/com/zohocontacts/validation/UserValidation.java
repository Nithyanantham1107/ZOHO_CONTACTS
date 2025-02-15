package com.zohocontacts.validation;

public class UserValidation {
	
	
	public static boolean validateUserPassword(String password) {
	
		if(password.length() <=72  && containsLowercase(password) && containsUppercase(password) && containsNumber(password)) {
			
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

  

}
