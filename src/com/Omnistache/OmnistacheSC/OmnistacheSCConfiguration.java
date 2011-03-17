package com.Omnistache.OmnistacheSC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.World;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.Omnistache.OmnistacheSC.Spawn.Control.EntityController;
import com.Omnistache.OmnistacheSC.Spawn.Control.MobSet;
import com.Omnistache.OmnistacheSC.Spawn.Group.SpawnGroup;

public class OmnistacheSCConfiguration {

	public final static OmnistacheSCConfiguration INSTANCE = new OmnistacheSCConfiguration();
	private HashMap<World, Configuration> worldConfig = new HashMap<World, Configuration>();
	private Configuration phasesConfig;
	private OmnistacheSC omnistacheSC;
	
	private OmnistacheSCConfiguration(){} //singleton
	
	public void initialize(OmnistacheSC omnistacheSC){
		this.omnistacheSC = omnistacheSC;
		
		//Check/create the defaultworldconfig
		
		File defaultConfig = new File(omnistacheSC.getDataFolder(), "defaultworldconfig.yml");
		
		if(!defaultConfig.exists()){ //if there's no custom default configuration then make it
			defaultConfig.getParentFile().mkdirs();
			
			FileInputStream in = (FileInputStream) getClass().getResourceAsStream("/defaultworldconfig.yml");
			FileOutputStream out;
			try {
				out = new FileOutputStream(defaultConfig);
				out.getChannel().transferFrom(
						in.getChannel(), 0, in.getChannel().size());

				in.close();
				out.close();
			} catch (IOException e) {
				omnistacheSC.getServer().getLogger().info("Failed to copy default world configuration to plugin directory");
				e.printStackTrace();
			}
		}
		
		//Load World configurations
		for(World world : omnistacheSC.getServer().getWorlds()){

			File configFile = new File(omnistacheSC.getDataFolder(), world.getName()+"_config.yml");

			if(configFile.exists()){
				addConfiguration(world, new Configuration(configFile));
			}
		}
		
		reloadConfigurations();

		//Load phases

		File phasesFile = new File(omnistacheSC.getDataFolder(), "phases.yml");

		//copy over the default phases file if no phases file exists!
		if(!phasesFile.exists()){
			phasesFile.getParentFile().mkdirs();

			FileInputStream in = (FileInputStream) getClass().getResourceAsStream("/defaultphases.yml");
			FileOutputStream out;
			try {
				out = new FileOutputStream(phasesFile);
				out.getChannel().transferFrom(
						in.getChannel(), 0, in.getChannel().size());

				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		phasesConfig = new Configuration(phasesFile);
		phasesConfig.load();
	}
	
	public void reInitialize(){
		worldConfig.clear();
		initialize(omnistacheSC);
	}
	
	public void reloadConfigurations(){
		for(Configuration configuration : worldConfig.values()){
			if(configuration != null)
				configuration.load();
		}
	}
	
	public boolean hasConfig(World world){
		if(!worldConfig.containsKey(world))
			return false;
		
		if(worldConfig.get(world) == null)
			return false;
		
		return true;
	}
	
	
	/**
	 * Associates a configuration object to a world,
	 * can be used by other plugins to manually create configurations
	 * @param world
	 * @param configuration
	 */
	public void addConfiguration(World world, Configuration configuration){
		if(world == null){
			omnistacheSC.getServer().getLogger().info(getClass().getSimpleName() + ": cannot associate config to null world");
			return;
		}
		if(configuration == null){
			omnistacheSC.getServer().getLogger().info(getClass().getSimpleName() + ": associating null config with world " + world.getName());
		}
		
		worldConfig.put(world, configuration);
	}

	/**
	 * creates a new EntityController based on the configuration of the given world
	 * inserts the static spawn groups from the configuration file
	 * 
	 * @param world
	 * @return EntityController
	 */
	public EntityController entityControllerFromConfiguration(World world) {
		EntityController entityController = new EntityController(omnistacheSC, world);
		Configuration config = worldConfig.get(world);
	
		Map<String, ConfigurationNode> nodeMap = config.getNodes("StaticGroups");
	
		for(String name : nodeMap.keySet()){
			SpawnGroup spawnGroup = SpawnGroup.fromConfiguration(nodeMap.get(name), name, world, omnistacheSC);
			if(spawnGroup == null){
				omnistacheSC.getServer().getLogger().info("Spawn Group " + name + " could not be loaded.");
			}
			entityController.addSpawnGroup(spawnGroup);
			entityController.resetUnwantedMobs();
		}
		return entityController;
	}

	/**
	 * reads the default removeMobs from the world's config file.
	 * returns None if there's no config, or if the value in the config is invalid.
	 * @param world
	 * @return
	 */
	public MobSet getDefaultUnwantedMobs(World world) {
		if(!worldConfig.containsKey(world)){
			return MobSet.None;
		}
		String removeMobString = worldConfig.get(world).getString("RemoveMobs", "None");
		MobSet unwantedMobs = MobSet.valueOf(removeMobString);
		if(unwantedMobs == null){
			omnistacheSC.getServer().getLogger().info("RemoveMobs was unreadable: " + removeMobString);
			return MobSet.None;
		}
		return unwantedMobs;
	}
}