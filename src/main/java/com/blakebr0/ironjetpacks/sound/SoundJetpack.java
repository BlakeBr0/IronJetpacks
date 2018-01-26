package com.blakebr0.ironjetpacks.sound;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.blakebr0.ironjetpacks.util.JetpackUtils;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundJetpack extends MovingSound {

	private static final Map<Integer, SoundJetpack> PLAYING_FOR = Collections.synchronizedMap(new HashMap<Integer, SoundJetpack>());
	
	private final EntityPlayer player;
	
	public SoundJetpack(EntityPlayer player) {
		super(ModSounds.soundJetpack, SoundCategory.PLAYERS);
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
	public void update() {
		this.xPosF = (float) this.player.posX;
		this.yPosF = (float) this.player.posY - 10;
		this.zPosF = (float) this.player.posZ;
		
		if (!JetpackUtils.isFlying(player)) {
			synchronized (PLAYING_FOR) {
				PLAYING_FOR.remove(player.getEntityId());
				this.donePlaying = true;
			}
		}
	}
}
