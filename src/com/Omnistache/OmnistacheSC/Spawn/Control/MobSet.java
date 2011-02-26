package com.Omnistache.OmnistacheSC.Spawn.Control;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Zombie;

/*
 * MobSet is used by the EntityController thread
 * to check whether an entity is allowed in the world/should be removed
 */

public enum MobSet {

	/*
	 * I have defeated java warnings with inline initialization of arraylists with
	 * anonymous classes!!!!!!!!!
	 * This enum is retarded.
	*/
	
	All(new ArrayList<Class<? extends LivingEntity>>() {
		private static final long serialVersionUID = 1L;
	{
		add(Chicken.class); add(Cow.class); add(Pig.class); add(Sheep.class);
		add(Creeper.class); add(Ghast.class); add(Giant.class); add(PigZombie.class);
		add(Skeleton.class); add(Spider.class); add(Zombie.class); add(Squid.class);
		add(Slime.class);
	}}),
	None(),
	Animals(new ArrayList<Class<? extends LivingEntity>>() {
		private static final long serialVersionUID = 1L;
	{
		add(Chicken.class); add(Cow.class); add(Pig.class); add(Sheep.class);
	}}),
	Monsters(new ArrayList<Class<? extends LivingEntity>>() {
		private static final long serialVersionUID = 1L;
	{
		add(Creeper.class); add(Ghast.class); add(Giant.class); add(PigZombie.class);
		add(Skeleton.class); add(Spider.class); add(Zombie.class);
	}}),
	LandDwelling(new ArrayList<Class<? extends LivingEntity>>() {
		private static final long serialVersionUID = 1L;
	{
		add(Chicken.class); add(Cow.class); add(Pig.class);	add(Sheep.class);
		add(Creeper.class);	add(Giant.class); add(PigZombie.class); add(Skeleton.class);
		add(Spider.class); add(Zombie.class); add(Slime.class);
	}}),
	SeaDwelling(Squid.class),
	Flying(Ghast.class),
	
	Chickens(Chicken.class),
	Cows(Cow.class),
	Pigs(Pig.class),
	Sheeps(Sheep.class),

	Creepers(Creeper.class),
	Ghasts(Ghast.class),
	Giants(Giant.class),
	PigZombies(PigZombie.class),
	Skeletons(Skeleton.class),
	Spiders(Spider.class),
	Zombies(Zombie.class),
	
	Squids(Squid.class),
	Slimes(Slime.class);
	
	private HashSet<Class<? extends LivingEntity>> classList;

	private MobSet(){}

	private MobSet(Class<? extends LivingEntity> class1){
		classList = new HashSet<Class<? extends LivingEntity>>(1);
		classList.add(class1);
	}

	private MobSet(ArrayList<Class<? extends LivingEntity>> classArray){
		classList = new HashSet<Class<? extends LivingEntity>>(classArray.size());
		classList.addAll(classArray);
	}

	public boolean match(LivingEntity livingEntity){
		return classList.contains(livingEntity.getClass());
	}	
}
