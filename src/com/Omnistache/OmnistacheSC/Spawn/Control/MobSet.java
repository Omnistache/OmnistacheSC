package com.Omnistache.OmnistacheSC.Spawn.Control;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.config.ConfigurationNode;

import com.Omnistache.OmnistacheSC.OmnistacheSC;

/*
 * MobSet is used by the EntityController thread
 * to check whether an entity is allowed in the world/should be removed
 * You can use a MobSet in place of CreatureType in the world.spawnCreature method
 * so you can spawn giants you normally could not
 * use a cast, and it will randomly select a mob type to spawn from the MobSet
 * if you're using MobSet.None, the world.spawnCreature method should return null
 */

public enum MobSet {
	
	All("Chicken", "Cow", "Pig", "Sheep", 
		"Creeper", "Ghast", "Giant", "PigZombie", 
		"Skeleton", "Spider", "Zombie", "Squid", 
		"Slime"),
	None(),
	Animals("Chicken", "Cow", "Pig", "Sheep"),
	Monsters("Creeper", "Ghast", "Giant", "PigZombie", 
		"Skeleton", "Spider", "Zombie"),
	LandDwelling("Chicken", "Cow", "Pig", 	"Sheep", 
		"Creeper", 	"Giant", "PigZombie", "Skeleton", 
		"Spider", "Zombie", "Slime"),
	SeaDwelling("Squid"),
	Flying("Ghast"),
	Chickens("Chicken"),
	Cows("Cow"),
	Pigs("Pig"),
	Sheeps("Sheep"),

	Creepers("Creeper"),
	Ghasts("Ghast"),
	Giants("Giant"),
	PigZombies("PigZombie"),
	Skeletons("Skeleton"),
	Spiders("Spider"),
	Zombies("Zombie"),
	
	Squids("Squid"),
	Slimes("Slime");
	
	private HashSet<String> types = new HashSet<String>();
	
	private MobSet(String... names){
		types.addAll(Arrays.asList(names));
	}
	
	public void or(MobSet mobSet){
		types.addAll(mobSet.types);
	}
	
	public void not(MobSet mobSet){
		types.removeAll(mobSet.types);
	}
	
	/**
	 * made public if you want to manually do what
	 * MobSet.fromConfiguration does
	 * @param stringList
	 * @return
	 */
	public static MobSet fromStringList(List<String> stringList){
		MobSet mobSet = MobSet.None;
		
		if(stringList == null){
			return mobSet;
		}
		
		if(stringList.isEmpty()){
			return mobSet;
		}
		
		//find not keywords and separate into two sets of mob types
		Set<String> notSet = new HashSet<String>();
		Set<String> orSet = new HashSet<String>();
		for(String type : stringList){
			if(type.toLowerCase().startsWith("not ")){
				notSet.add(type.substring(4).trim());
			} else {
				orSet.add(type.trim());
			}
		}
		if(orSet.isEmpty()){
			OmnistacheSC.logger.info("Could not specify MobSet from String List, only contained NOT keywords, using MobSet.None");
			return mobSet;
		}
		
		for(String type : orSet){
			try{
				MobSet orMobSet = MobSet.valueOf(type);
				mobSet.or(orMobSet);
			}
			catch (IllegalArgumentException e){
				OmnistacheSC.logger.info("Invalid MobSet type " + type + "!");
				e.printStackTrace();
			}
		}
		
		for(String type : notSet){
			try{
				MobSet notMobSet = MobSet.valueOf(type);
				mobSet.not(notMobSet);
			}
			catch (IllegalArgumentException e){
				OmnistacheSC.logger.info("Invalid MobSet type " + type + "!");
				e.printStackTrace();
			}
		}
		return mobSet;
	}
	/**
	 * takes a configuration node and a path to some data specifying mobs for a mobset
	 * data can either be a single string or a list of strings
	 * will create a MobSet from the strings at the designated configurationPath
	 * using the keyword "not" before a mob type in the configuration
	 * removes it from the MobSet
	 * @param configuration
	 * @param configurationPath
	 * @return
	 */
	public static MobSet fromConfiguration(ConfigurationNode configuration, String configurationPath){
		List<String> mobList = configuration.getStringList(configurationPath, null);
		if(mobList.isEmpty()){
			//could be a single string so try...
			String mob = configuration.getString(configurationPath);
			if(mob == null){
				//no data
				return MobSet.None;
			} //return pyramids
			return MobSet.fromString(mob);
		}       //are cool
		return MobSet.fromStringList(mobList);
	}

	/**
	 * made public so you can manually do
	 * what MobSet.fromConfiguration does
	 * @param mob
	 * @return
	 */
	public static MobSet fromString(String mob) {
		List<String> mobList = new ArrayList<String>();
		mobList.add(mob);
		return MobSet.fromStringList(mobList);
	}

	/**
	 * matches a livingEntity to this MobSet
	 * returns true iff the entity type is present in
	 * the MobSet
	 * @param livingEntity
	 * @return
	 */
	public boolean match(LivingEntity livingEntity){
		if(types.isEmpty()){
			return false;
		}
		
		String entityName = livingEntity.toString();
		if(entityName.startsWith("Craft")){
			entityName = entityName.substring(5);
			return types.contains(entityName);
		}
		return false;
		
	}
	/**
	 * Selects a random name from the MobSet
	 * @return
	 */
	private String randomCreatureTypeName(){
		if(types.isEmpty()){
			return null;
		}
		
		String[] typeArray = (String[]) types.toArray();
		int arrayLength = typeArray.length;
		
		int randomIndex = (new Random()).nextInt(arrayLength);
		String type = typeArray[randomIndex];
		
		return type;
		
	}
	
	/**
	 * this method is hacky, injecting a new
	 * name string into the creatureType so giants can spawn,
	 * if this throws exceptions I've set it to return a Zombie
	 * @return
	 */
	public CreatureType randomCreatureType(){
		String name = randomCreatureTypeName();
		CreatureType creatureType = CreatureType.fromName(name);
		if(creatureType == null){
			Field nameField;
			try {
				nameField = CreatureType.class.getDeclaredField("name");
				nameField.setAccessible(true);
				nameField.set(creatureType, name);
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return CreatureType.ZOMBIE;
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return CreatureType.ZOMBIE;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return CreatureType.ZOMBIE;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return CreatureType.ZOMBIE;
			}
		}
		return creatureType;
	}
}
