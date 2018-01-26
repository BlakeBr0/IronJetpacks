package com.blakebr0.ironjetpacks.handler;

import java.util.HashSet;
import java.util.Set;

public class SyncHandler {

	public static final Set<Integer> ACTIVE = new HashSet<>();
	
	public static void update(int entityId, boolean active) {
		if (active) {
			ACTIVE.add(entityId);
		} else {
			ACTIVE.remove(entityId);
		}
	}
}
