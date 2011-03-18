package com.Omnistache.OmnistacheSC.Spawn.Group.AI;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.util.config.ConfigurationNode;

import com.Omnistache.OmnistacheSC.OmnistacheSC;

public class AIFactory {

	private static Map<String, Class<? extends AI>> aiMap = new HashMap<String, Class<? extends AI>>();
	
	/**
	 * add a class of an AI type to the Factory for creation
	 * with the name it will be referred to in configuration files
	 * with the type property
	 * @param name
	 * @param aiClass
	 */
	public static void register(String name, Class<? extends AI> aiClass){
		aiMap.put(name, aiClass);
		OmnistacheSC.logger.info("Successfully registered new class " + aiClass.getSimpleName() + " with name " + name);
	}
	
	/**
	 * remove an AI type class by its configuration type name
	 * @param name
	 */
	public static void unregister(String name){
		if(!aiMap.containsKey(name)){
			OmnistacheSC.logger.info("AI type " + name + " is not registered and cannot be unregistered in AIFactory");
			return;
		}
		Class<? extends AI> aiClass = aiMap.remove(name);
		OmnistacheSC.logger.info("Successfully unregistered class " + aiClass.getSimpleName() + " with name " + name);
	}
	
	/**
	 * create a new AI based on a configuration from a file or
	 * whatnot
	 * @param configuration
	 * @return
	 */
	public static AI fromConfiguration(ConfigurationNode configuration){
		String type = configuration.getString("type");
		
		if(type.equals("None")){
			return null;
		}
		
		if(!aiMap.containsKey(type)){
			OmnistacheSC.logger.info("Unknown AI type: " + type);
			return null;
		}
		
		
		try {
			AI ai = aiMap.get(type).getConstructor().newInstance();
			ai.initializeFromConfiguration(configuration);
			return ai;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OmnistacheSC.logger.info("AI type " + type + " may not have been created successfully");
		return null;
	}
}
