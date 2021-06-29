package com.blakebr0.ironjetpacks.sound;

import com.blakebr0.ironjetpacks.init.ModSounds;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
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
		this.looping = true;
		PLAYING_FOR.put(player.getId(), this);
	}
	
	public static boolean playing(int entityId) {
		return PLAYING_FOR.containsKey(entityId) && PLAYING_FOR.get(entityId) != null && !PLAYING_FOR.get(entityId).isStopped();
	}

	@Override
	public void tick() {
		Vector3d pos = this.player.position();
		this.x = (float) pos.x();
		this.y = (float) pos.y() - 10;
		this.z = (float) pos.z();
		
		if (!JetpackUtils.isFlying(this.player)) {
			synchronized (PLAYING_FOR) {
				PLAYING_FOR.remove(this.player.getId());
				this.stop();
			}
		}
	}
}
