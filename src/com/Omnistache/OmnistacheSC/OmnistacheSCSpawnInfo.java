package com.Omnistache.OmnistacheSC;

import java.util.ArrayList;


/*
 * Contains spawn information for the EntityConroller
 * i.e.
 * Entities to remove/allow
 * SpawnGroups to spawn
 */
public class OmnistacheSCSpawnInfo {

	private ArrayList<OmnistacheSCSpawnGroup> spawnGroups = new ArrayList<OmnistacheSCSpawnGroup>();
	private OmnistacheSCMobSet mobRemovalSet = OmnistacheSCMobSet.None;
	
}
