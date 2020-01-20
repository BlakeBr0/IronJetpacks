package com.blakebr0.ironjetpacks.sound;

import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class JetpackSound extends TickableSound {
	private static final Map<Integer, JetpackSound> PLAYING_FOR = Collections.synchronizedMap(new HashMap<>());
	private final PlayerEntity player;
	
	public JetpackSound(PlayerEntity player) {
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
		BlockPos pos = this.player.getPosition();
		this.x = (float) pos.getX();
		this.y = (float) pos.getY() - 10;
		this.z = (float) pos.getZ();
		
		if (!JetpackUtils.isFlying(this.player)) {
			synchronized (PLAYING_FOR) {
				PLAYING_FOR.remove(this.player.getEntityId());
				this.donePlaying = true;
			}
		}
	}
}
