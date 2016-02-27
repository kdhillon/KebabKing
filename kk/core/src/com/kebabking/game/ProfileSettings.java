package com.kebabking.game;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/** Contains persistent settings that should be saved when user quits
 * 
 * @author Kyle
 *
 */
public class ProfileSettings {
	@Tag(201) boolean muteMusic = false; // unmuted by default
	@Tag(202) boolean muteSound = false; // unmuted by default
	
	public void initializeAfterLoad() {
		// initialize any fields that were empty at load.
		if (muteMusic) {
			muteMusic();
		}
		else unmuteMusic();
	}
	
	public void muteMusic() {
		this.muteMusic = true;
		SoundManager.muteAll();
	}
	
	public void unmuteMusic() {
		this.muteMusic = false;
		SoundManager.unmuteAll();
	}
	
	public void muteSound() {
		this.muteSound = true;
	}
	
	public void unmuteSound() {
		this.muteSound = false;
	}
}
