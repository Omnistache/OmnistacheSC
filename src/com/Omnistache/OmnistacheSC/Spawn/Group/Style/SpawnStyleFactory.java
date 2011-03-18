package com.Omnistache.OmnistacheSC.Spawn.Group.Style;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.util.config.ConfigurationNode;

import com.Omnistache.OmnistacheSC.OmnistacheSC;

public class SpawnStyleFactory {

	private static Map<String, Class<? extends SpawnStyle>> spawnStyleMap = new HashMap<String, Class<? extends SpawnStyle>>();
	
	/**
	 * add a class of a SpawnStyle type to the Factory for creation
	 * with the name it will be referred to in configuration files
	 * with the type property
	 * @param name
	 * @param spawnStyleClass
	 */
	public static void register(String name, Class<? extends SpawnStyle> spawnStyleClass){
		spawnStyleMap.put(name, spawnStyleClass);
		OmnistacheSC.logger.info("Registered new class " + spawnStyleClass.getSimpleName() + " with name " + name);
	}
	
	/**
	 * remove a SpawnStyle type class by its configuration type name
	 * @param name
	 */
	public static void unregister(String name){
		if(!spawnStyleMap.containsKey(name)){
			OmnistacheSC.logger.info("SpawnStyle type " + name + " is not registered and cannot be unregistered in SpawnStyleFactory");
			return;
		}
		Class<? extends SpawnStyle> spawnStyleClass = spawnStyleMap.remove(name);
		OmnistacheSC.logger.info("Successfully unregistered class " + spawnStyleClass.getSimpleName() + " with name " + name);
	}
	
	/**
	 * create a new SpawnStyle based on a configuration from a file or
	 * whatnot
	 * @param configuration
	 * @return
	 */
	public static SpawnStyle fromConfiguration(ConfigurationNode configuration){
		String type = configuration.getString("type");
		
		if(!spawnStyleMap.containsKey(type)){
			OmnistacheSC.logger.info("Unknown SpawnStyle type: " + type);
			return null;
		}
		
		try {
			SpawnStyle spawnStyle = spawnStyleMap.get(type).getConstructor().newInstance();
			spawnStyle.initializeFromConfiguration(configuration);
			return spawnStyle;
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
		OmnistacheSC.logger.info("SpawnStyle type " + type + " may not have been created successfully");
		return null;
	}
}
