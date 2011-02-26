package com.Omnistache.OmnistacheSC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;

import com.Omnistache.OmnistacheSC.Event.Event;
import com.Omnistache.OmnistacheSC.Event.Phase;

public class OmnistacheSCConfiguration {

	public final static OmnistacheSCConfiguration INSTANCE = new OmnistacheSCConfiguration();
	private HashMap<World, Configuration> worldConfig = new HashMap<World, Configuration>();
	private Configuration phasesConfig;
	private OmnistacheSC omnistacheSC;
	private ArrayList<Phase> phases = new ArrayList<Phase>();
	private HashMap<World, ArrayList<Event>> events = new HashMap<World,ArrayList<Event>>();
	
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
				e.printStackTrace();
			}
		}
		
		//Load World configurations
		for(World world : omnistacheSC.getServer().getWorlds()){

			File configFile = new File(omnistacheSC.getDataFolder(), world.getName()+"_config.yml");

			if(!configFile.exists()){
				worldConfig.put(world, null);
			}
			else {
				worldConfig.put(world, new Configuration(configFile));
			}
		}
		
		for(Configuration configuration : worldConfig.values()){
			if(configuration != null)
				configuration.load();
		}

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

	public void reload(){
		initialize(omnistacheSC);
	}
	
	public boolean hasConfig(World world){
		if(!worldConfig.containsKey(world))
			return false;
		
		return worldConfig.get(world) != null;
	}

	public ArrayList<Event> getEvents(World world){
		return events.get(world);
	}
	
	public ArrayList<Phase> getPhases(){
		return phases;
	}
}
