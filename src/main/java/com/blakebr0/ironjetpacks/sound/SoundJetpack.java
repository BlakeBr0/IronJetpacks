package com.blakebr0.ironjetpacks.sound;

import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SoundJetpack extends TickableSound {
	private static final Map<Integer, SoundJetpack> PLAYING_FOR = Collections.synchronizedMap(new HashMap<Integer, SoundJetpack>());
	private final PlayerEntity player;
	
	public SoundJetpack(PlayerEntity player) {
		super(ModSounds.JETPACK, SoundCategory.PLAYERS);
		this.player = player;
		this.repeat = true;
		PLAYING_FOR.put(player.getEntityId(), this);
	}
	
	public static boolean playing(int entityId) {
		return PLAYING_FOR.containsKey(entityId) && PLAYING_FOR.get(entityId) != null && !PLAYING_FOR.get(entityId).donePlaying;
	}
	
	public static void stopPlaying() {
		PLAYING_FOR.clear();
	}

	@Override
	public void tick() {
		this.x = (float) this.player.posX;
		this.y = (float) this.player.posY - 10;
		this.z = (float) this.player.posZ;
		
		if (!JetpackUtils.isFlying(this.player)) {
			synchronized (PLAYING_FOR) {
				PLAYING_FOR.remove(this.player.getEntityId());
				this.donePlaying = true;
			}
		}
	}
}
