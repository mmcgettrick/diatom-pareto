package com.diatom.pareto.license;

import java.io.File;

public class IsLicenseKeyValid {

	public static void main(String[] args) {
		try {
			if(LicenseUtils.isValidKey(new File(args[0]))) {
				System.out.println("VALID");
			}
			else {
				System.out.println("INVALID");
			}
		}
		catch(Exception e) {
			System.out.println("INVALID");
		}
		
	}
	
}
