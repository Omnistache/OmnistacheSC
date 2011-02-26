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

/*
 * Held inside a SpawnGroup, used to modify entities that were just spawned
 */
public class EntityModifier extends EntityListener {

	private int startingHealth;
	private int damage;
	private HashSet<LivingEntity> modifiedEntities;
	private boolean immuneToSunlight;

	public EntityModifier(Plugin plugin, int startingHealth, int damage, int groupSize, boolean immuneToSunlight){
		this.startingHealth = startingHealth;
		this.damage = damage;
		this.modifiedEntities = new HashSet<LivingEntity>(groupSize);
		this.immuneToSunlight = immuneToSunlight;
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
	 * removes dead entities from the modifiedentities list
	 */
	private void removeDeadEntities(){
		ArrayList<LivingEntity> remove = new ArrayList<LivingEntity>();
		for(LivingEntity ent : modifiedEntities){
			if(ent.getHealth() == 0){
				remove.add(ent);
			}
		}
		modifiedEntities.removeAll(remove);
	}

	public void applyToEntity(LivingEntity livingEntity){
		livingEntity.setHealth(startingHealth);
		if(modifiesDamage() || immuneToSunlight){
			modifiedEntities.add(livingEntity);
		}
	}
	
	@Override
	public void onEntityCombust(EntityCombustEvent event){
		
		removeDeadEntities();
		
		if(modifiedEntities.contains(event.getEntity())){
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){

		removeDeadEntities();
		
		if(event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			if(modifiedEntities.contains(damageEvent.getDamager())){
				damageEvent.setDamage(damage);
			}
		}
	}
	
	
}
