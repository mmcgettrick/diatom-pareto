package com.diatom.pareto.examples.wedding;

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
public class SeatingConfiguration extends Resource {


	Properties props = new Properties(); // Mill properties

	public SeatingConfiguration(Map<String, Object> options) {
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
	 * get an integer-valued seating property
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
	 * Get a double-valued seating property
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
		options.put("name", "seating");
		options.put("filename", "conf/wedding/seating.cfg");
		SeatingConfiguration layout  = new SeatingConfiguration(options);

		System.out.println("Number of tables: "+layout.getIntProperty("max_tables"));
		System.out.println("VIP tables      : "+layout.getIntProperty("vip_tables"));
		System.out.println("Seats per table : "+layout.getIntProperty("seats_per_table"));
	}
	
}
