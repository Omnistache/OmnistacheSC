package com.Omnistache.OmnistacheSC.Spawn.Group;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.ConfigurationNode;

/*
 * Held inside a SpawnGroup, used to modify entities that were just spawned
 * set starting health, change damage, change immunity to sunlight
 */
public class EntityModifier extends EntityListener {

	private int startingHealth;
	private int damage;
	private HashSet<LivingEntity> modifiedEntities;
	private boolean immuneToSunlight;
	private boolean ignoreListener = false;
	//private Plugin plugin; save for when we can unregister listeners

	public EntityModifier(Plugin plugin, int startingHealth, int damage, int groupSize, boolean immuneToSunlight){
		this.startingHealth = startingHealth;
		this.damage = damage;
		this.modifiedEntities = new HashSet<LivingEntity>(groupSize * 2);
		this.immuneToSunlight = immuneToSunlight;
		//this.plugin = plugin;
		if(modifiesDamage()){
			plugin.getServer().getPluginManager().registerEvent(
					Type.ENTITY_DAMAGED, this, Priority.Normal, plugin);
		}
		if(immuneToSunlight){
			plugin.getServer().getPluginManager().registerEvent(
					Type.ENTITY_COMBUST, this, Priority.Normal, plugin);
		}
	}
	
	/*
	 * checks to see if this modifies the damage of the group of entities
	 */
	private boolean modifiesDamage() {
		return (damage != -1);
	}
	
	/*
	 * removes dead entities from the modifiedEntities list
	 */
	private void removeDeadEntities(){
		ArrayList<LivingEntity> remove = new ArrayList<LivingEntity>();
		synchronized(modifiedEntities){
			for(LivingEntity ent : modifiedEntities){
				if(ent.getHealth() == 0){
					remove.add(ent);
				}
			}
			modifiedEntities.removeAll(remove);
		}
	}

	public void applyToEntity(LivingEntity livingEntity){
		livingEntity.setHealth(startingHealth);
		if(modifiesDamage() || immuneToSunlight){
			synchronized(modifiedEntities){
				modifiedEntities.add(livingEntity);
			}
		}
	}
	
	@Override
	public void onEntityCombust(EntityCombustEvent event){
		if(ignoreListener)
			return;
		
		removeDeadEntities();
		
		if(modifiedEntities.contains(event.getEntity())){
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){
		if(ignoreListener)
			return;
		
		if(event instanceof EntityDamageByEntityEvent){
			removeDeadEntities();
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			if(modifiedEntities.contains(damageEvent.getDamager())){
				damageEvent.setDamage(damage);
			}
		}
	}

	public void disableAndFree() {
		//no way to unregister the event listener currently, compromise by setting a flag to return
		//when the listener is called
		ignoreListener = true;
		
		modifiedEntities.clear();
		modifiedEntities = null;
		//plugin = null;
	}
	
	public static EntityModifier fromConfiguration(ConfigurationNode configuration, int groupSize, Plugin plugin){
		if(configuration == null){
			return new EntityModifier(plugin, -1, -1, groupSize, false);
		}
		
		int damage = configuration.getInt("damage", -1);
		int health = configuration.getInt("health", -1);
		boolean immuneToSunlight = configuration.getBoolean("immuneToSunlight", false);
		
		EntityModifier entityModifier = new EntityModifier(plugin, health, damage, groupSize, immuneToSunlight);
		return entityModifier;
	}
	
}
