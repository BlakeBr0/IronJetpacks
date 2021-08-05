package com.blakebr0.ironjetpacks.sound;

import com.blakebr0.ironjetpacks.init.ModSounds;
import com.blakebr0.ironjetpacks.util.JetpackUtils;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class JetpackSound extends AbstractTickableSoundInstance {
	private static final Map<Integer, JetpackSound> PLAYING_FOR = Collections.synchronizedMap(new HashMap<>());
	private final Player player;
	
	public JetpackSound(Player player) {
		super(ModSounds.JETPACK, SoundSource.PLAYERS);
		this.player = player;
		this.looping = true;
		PLAYING_FOR.put(player.getId(), this);
	}
	
	public static boolean playing(int entityId) {
		return PLAYING_FOR.containsKey(entityId) && PLAYING_FOR.get(entityId) != null && !PLAYING_FOR.get(entityId).isStopped();
	}

	@Override
	public void tick() {
		var pos = this.player.position();

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
