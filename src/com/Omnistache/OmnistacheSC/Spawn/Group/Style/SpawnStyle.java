package com.Omnistache.OmnistacheSC.Spawn.Group.Style;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public interface SpawnStyle {

	/*
	 * Tries to spawn an entity,
	 * if not successful, returns null
	 */
	public LivingEntity spawnEntity(World world);
	
	/*
	 * This is the string your AI matches to
	 * in a config file i.e. NEAREST_PLAYER_SURFACE
	 */
	public String typeString();
	
}