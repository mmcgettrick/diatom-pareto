package com.diatom.pareto.config;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public class OptimizerConfigurationItem {

	private String className;
	private Map<String,Object> options;
	
	public OptimizerConfigurationItem(String className, Map<String,Object> options) {
		this.className = className;
		this.options = options;
	}
	
	public OptimizerConfigurationItem(String className) {
		this(className,new TreeMap<String,Object>());
	}
	
	public OptimizerConfigurationItem() {
		this("",new TreeMap<String,Object>());
	}
	
	public String getClassName() {
		return className;
	}
	public Map<String, Object> getOptions() {
		return options;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object construct() throws Exception {
		Class c = Class.forName(className);
		Constructor constructor = c.getConstructor(Map.class);
		return constructor.newInstance(options);
	}
	
	
	
}
