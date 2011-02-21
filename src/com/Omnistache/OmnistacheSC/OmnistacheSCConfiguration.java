package com.Omnistache.OmnistacheSC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;

public class OmnistacheSCConfiguration {

	public final static OmnistacheSCConfiguration INSTANCE = new OmnistacheSCConfiguration();
	private HashMap<World, Configuration> worldConfig = new HashMap<World, Configuration>();
	private Configuration phasesConfig;
	private OmnistacheSC omnistacheSC;
	private ArrayList<OmnistacheSCPhase> phases = new ArrayList<OmnistacheSCPhase>();
	private HashMap<World, ArrayList<OmnistacheSCEvent>> events = new HashMap<World,ArrayList<OmnistacheSCEvent>>();
	
	private OmnistacheSCConfiguration(){} //singleton
	
	public void initialize(OmnistacheSC omnistacheSC){
		this.omnistacheSC = omnistacheSC;
		
		//Load World configurations
		for(World world : omnistacheSC.getServer().getWorlds()){

			File configFile = new File(omnistacheSC.getDataFolder(), world.getName()+"_config.yml");

			if(!configFile.exists()){
				configFile.getParentFile().mkdirs();
				
				File defaultConfig = new File(omnistacheSC.getDataFolder(), "defaultworldconfig.yml");
				if(!defaultConfig.exists()){ //if there's no custom default configuration then make it
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
				
				//then copy from the defaultconfig.yml to the new world's config
				
				FileInputStream in;
				FileOutputStream out;
				try {
					in = new FileInputStream(defaultConfig);
					out = new FileOutputStream(configFile);
					out.getChannel().transferFrom(
							in.getChannel(), 0, in.getChannel().size());

					in.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			worldConfig.put(world, new Configuration(configFile));
		}
		for(Configuration configuration: worldConfig.values())
			configuration.load();

		//Load phases

		File phasesFile = new File(omnistacheSC.getDataFolder(), "phases.yml");

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
	
	public boolean useSpawnControl(World world){
		return worldConfig.get(world).getBoolean("UseSpawnControl", true);
	}

	public ArrayList<OmnistacheSCEvent> getEvents(World world){
		return events.get(world);
	}
	
	public ArrayList<OmnistacheSCPhase> getPhases(){
		return phases;
	}

	public long getEventCycle(World world) {
		return worldConfig.get(world).getInt("EventCycle", 100);
	}

	public boolean getSkeletonSunburn(World world) {
		return worldConfig.get(world).getBoolean("SkeletonSunburn", false);
	}

	public long getMaxEventDelay(World world) {
		return worldConfig.get(world).getInt("MaxEventDelay", 12000);
	}

	public boolean getZombieSunburn(World world) {
		return worldConfig.get(world).getBoolean("ZombieSunburn", false);
	}
}
