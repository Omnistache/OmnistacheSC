package com.Omnistache.OmnistacheSC.Spawn.Group.AI;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.util.config.ConfigurationNode;

public class AIFactory {

	private static Map<String, Class<? extends AI>> aiMap = new HashMap<String, Class<? extends AI>>();
	
	/**
	 * add a class of an AI type to the Factory for creation
	 * with the name it will be referred to in configuration files
	 * @param name
	 * @param aiClass
	 */
	public static void register(String name, Class<? extends AI> aiClass){
		aiMap.put(name, aiClass);
		Logger.getLogger("Minecraft").info("Registered new class " + aiClass.getSimpleName() + " with name " + name);
	}
	
	/**
	 * remove an AI type class by its config name
	 * @param name
	 */
	public static void unregister(String name){
		if(!aiMap.containsKey(name)){
			Logger.getLogger("Minecraft").info("AI type " + name + " is not registered and cannot be unregistered");
			return;
		}
		Class<? extends AI> aiClass = aiMap.remove(name);
		Logger.getLogger("Minecraft").info("Unregistered class " + aiClass.getSimpleName() + " with name " + name);
	}
	
	public static AI fromConfiguration(ConfigurationNode configuration){
		String type = configuration.getString("type");
		
		if(type.equals("None")){
			return null;
		}
		
		if(!aiMap.containsKey(type)){
			Logger.getLogger("Minecraft").info("Unknown AI type: " + type);
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
		Logger.getLogger("Minecraft").info("AI type " + type + " may not have been created successfully");
		return null;
	}
}
