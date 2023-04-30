package com.diatom.pareto.examples.mill;

import com.diatom.pareto.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * A collection of orders
 * @author John
 *
 */
public class MillConfiguration extends Resource {
	
	
	Properties props = new Properties(); // Mill properties
	
	public MillConfiguration(Map<String, Object> options) {
		super(options);
		String filename = (String) options.get("filename");
		read(filename);
	}
	
	/**
	 * Get a (string-valued) mill property
	 * @param key
	 * @return
	 */
	public String getProperty(String key)
	{
		return props.getProperty(key);
	}
	
	/**
	 * get an integer-valued mill property
	 * @param key
	 * @return
	 */
	public int getIntProperty(String key)
	{
		try
		{
			return (new Integer(getProperty(key))).intValue();
		} catch (Exception e)
		{
			return 0;
		}
	}
	
	/**
	 * Get a double-valued mill property
	 * @param key
	 * @return
	 */
	public double getDoubleProperty(String key)
	{
		try
		{
			return (new Double(getProperty(key))).doubleValue();
		} catch (Exception e)
		{
			return 0.0;
		}
	}
	

	/**
	 * Read mill configuration
	 * @param filename
	 */
	public void read(String filename) {
		try
		{
			File propertiesFile = new File(filename);
			FileInputStream in = new FileInputStream(propertiesFile);
			props.load(in);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	
	public static void main(String[] args)
	{
		Map<String,Object> options = new HashMap<String,Object>();
		options.put("name", "mill");
		options.put("filename", "conf/mill.cfg");
		MillConfiguration mill  = new MillConfiguration(options);

		System.out.println("Number of machines: "+mill.getIntProperty("machines"));
		System.out.println("Setup Time        : "+mill.getIntProperty("setup_time"));
	}
	
}
