package com.Omnistache.OmnistacheSC.Spawn.Group;

import java.util.HashSet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.Plugin;

public class EntityModifier extends EntityListener {

	private int startingHealth;
	private int damage;
	private HashSet<LivingEntity> modifiedEntities;

	public EntityModifier(Plugin plugin, int startingHealth, int damage, int groupSize){
		this.startingHealth = startingHealth;
		this.damage = damage;
		this.modifiedEntities = new HashSet<LivingEntity>(groupSize);
		if(modifiesDamage()){
			plugin.getServer().getPluginManager().registerEvent(
					Type.ENTITY_DAMAGED, this, Priority.Normal, plugin);
		}
	}
	
	private boolean modifiesDamage() {
		return (damage != -1);
	}

	public void applyToEntity(LivingEntity livingEntity){
		livingEntity.setHealth(startingHealth);
		if(modifiesDamage()){
			modifiedEntities.add(livingEntity);
		}
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event){
		if(event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			if(modifiedEntities.contains(damageEvent.getDamager())){
				damageEvent.setDamage(damage);
			}
		}
	}
	
	
}
