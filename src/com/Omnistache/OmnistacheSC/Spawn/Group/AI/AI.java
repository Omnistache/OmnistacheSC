package com.Omnistache.OmnistacheSC.Spawn.Group.AI;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.config.ConfigurationNode;

public interface AI {
	
	/*
	 * run the AI on a living entity, pathing, whatever
	 */
	public void runAI(LivingEntity livingEntity);

	public int initializeFromConfiguration(ConfigurationNode configuration);
}