package com.diatom.pareto.license;


public class CreateLicenseKey {

	public static void main(String[] args) {
		LicenseUtils.createLicenseKey("DIATOM-PARETO Licensed to " + args[0]);
	}
	
}
