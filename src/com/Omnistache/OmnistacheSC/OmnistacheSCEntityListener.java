package com.Omnistache.OmnistacheSC;

import org.bukkit.World;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityListener;

public class OmnistacheSCEntityListener extends EntityListener {

	public OmnistacheSCEntityListener(OmnistacheSC omnistacheZA){
		
	}
	
	@Override public void onEntityCombust(EntityCombustEvent event){
		World world = event.getEntity().getWorld();
		if(OmnistacheSCConfiguration.INSTANCE.getZombieSunburn(world) == false)
			if(event.getEntity() instanceof Zombie)
				event.setCancelled(true);
		
		if(OmnistacheSCConfiguration.INSTANCE.getSkeletonSunburn(world) == false)
			if(event.getEntity() instanceof Skeleton)
				event.setCancelled(true);
	}
}
