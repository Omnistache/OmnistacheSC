package com.Omnistache.OmnistacheSC.Spawn.Control;

import java.util.ArrayList;
import java.util.Arrays;

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

public enum MobSet {

	All(Chicken.class, Cow.class, Pig.class,
	     Sheep.class, Creeper.class, Ghast.class,
	     Giant.class, PigZombie.class, Skeleton.class,
	     Spider.class, Zombie.class, Squid.class,
	     Slime.class),
	None(),
	Animals(Chicken.class, Cow.class, Pig.class, Sheep.class),
	Monsters(Creeper.class, Ghast.class,
		     Giant.class, PigZombie.class, Skeleton.class,
		     Spider.class, Zombie.class),
	LandDwelling(Chicken.class, Cow.class, Pig.class,
		     Sheep.class, Creeper.class,
		     Giant.class, PigZombie.class, Skeleton.class,
		     Spider.class, Zombie.class,
		     Slime.class),
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
	
	private ArrayList<Class<? extends LivingEntity>> classList = new ArrayList<Class<? extends LivingEntity>>();
	
	private MobSet(Class<? extends LivingEntity>... classArray){
		classList.addAll(Arrays.asList(classArray));
	}

	public boolean match(LivingEntity livingEntity){
		return classList.contains(livingEntity.getClass());
	}	
}
