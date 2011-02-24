package com.Omnistache.OmnistacheSC.Spawn.Group;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/*
 * original spagetti code class that
 * spawned zombies and ran their ai
 * kept to copy paste the ai and stuff
 */

public class OmnistacheSCSpawnTimer implements Runnable {

	private static final int MAX_ZOMBIES = 150;
	private Server server;
	private ArrayList<EntityZombie> zombies = new ArrayList<EntityZombie>();

	public OmnistacheSCSpawnTimer(Server server) {
		this.server = server;
	}

	@Override
	public void run() {
		for(org.bukkit.World world : server.getWorlds()){
			ArrayList<LivingEntity> removeUs = new ArrayList<LivingEntity>();
			for(LivingEntity ent :  world.getLivingEntities()){
				CraftLivingEntity cEnt = (CraftLivingEntity) ent;
				if((cEnt.getHandle() instanceof EntityMonster) &&
						!zombies.contains(cEnt.getHandle())){
					removeUs.add(ent);
				}
			}
			for(LivingEntity remove : removeUs)
				remove.setHealth(0);
		}
		
		Player[] players = server.getOnlinePlayers();
		
		for(Player player : players){
			//don't spawn during the day
			if(player.getWorld().getTime() < 12000)
				continue;

			if(zombies.size() > MAX_ZOMBIES)
				continue;
			
			SpawnZombieNearPlayer(player);			
		}
		
		ArrayList<EntityZombie> removes = new ArrayList<EntityZombie>();
		
		for(EntityZombie zombie : zombies){
			if(!zombie.dead){
				removes.add(zombie);
				continue;
			}
			PathToNearestPlayer(zombie, players);
		}
		
		for(EntityZombie remove : removes){
			zombies.remove(remove);
		}
		
	}

	private void PathToNearestPlayer(EntityZombie zombie, Player[] players) {
		//make a line to the nearest player, if they are farther than 16 blocks away, path
		//to the highest block along the line to them 8 away (along the line)
		org.bukkit.World zombieWorld = zombie.getBukkitEntity().getWorld();
		Vector zombieVector = zombie.getBukkitEntity().getLocation().toVector();
		
		Player closest = null;
		double closestDistance = 0;
		
		for(Player player : players){
			if(player.getWorld().equals(zombieWorld)){
				double playerDistance = zombieVector.distanceSquared(player.getLocation().toVector());
				if(closest == null){
					closest = player;
					closestDistance = playerDistance;
				}
				else {
					if(playerDistance < closestDistance){
						closest = player;
						closestDistance = playerDistance; 
					}
				}
			}
		}
		if(closest == null)
			return;
		
		if(closestDistance > 256){ //distance is squared
			Vector normal = closest.getLocation().toVector().clone().subtract(zombieVector).normalize();
			normal.multiply(8);
			normal.add(zombieVector);
			int destinationY = closest.getWorld().getHighestBlockYAt(normal.getBlockX(), normal.getBlockZ());

			zombie.d = ((CraftPlayer)closest).getHandle();		
			zombie.a = zombie.world.a(zombie, normal.getBlockX(), destinationY, normal.getBlockZ(), 16.0F); 
		}
		else {
			zombie.d = ((CraftPlayer)closest).getHandle();
			zombie.a = zombie.world.a(zombie, zombie.d, 16.0F);
		}
		
	}

	private void SpawnZombieNearPlayer(Player player) {
		
		Location spawnPoint = FindSuitableSpawnNearPlayer(player);
		
		if ((spawnPoint.getBlockY() - player.getLocation().getBlockY()) > 5){
			return;
		}
		
		CraftPlayer cPlayer = (CraftPlayer)player;
		CraftWorld cWorld = (CraftWorld)cPlayer.getWorld();
		net.minecraft.server.World world = (World)cWorld.getHandle();
		CraftCreature zombie = new CraftCreature(cWorld.getHandle().getServer(), new EntityZombie(world));
		zombie.teleportTo(spawnPoint);
		
		EntityCreature zombieEnt = zombie.getHandle();
		
		zombies.add((EntityZombie) zombieEnt);
		
		world.a(zombieEnt);
		
		zombieEnt.d = cPlayer.getHandle();
	}

	private Location FindSuitableSpawnNearPlayer(Player player) {
		Location origin = player.getLocation();
		org.bukkit.World world = player.getWorld();
		int x = origin.getBlockX();
		int z = origin.getBlockZ();
		
		Random random = new Random();
		int distance = random.nextInt(20) + 70;
		double radian = random.nextDouble() * 2 * Math.PI;
		
		x += Math.round(Math.cos(radian)*distance);
		z += Math.round(Math.sin(radian)*distance);
		
		int y = world.getHighestBlockYAt(x, z);
		
		return new Location(world, x, y, z);
	}

}
