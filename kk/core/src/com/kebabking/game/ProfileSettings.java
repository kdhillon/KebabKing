package com.kebabking.game;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

/** Contains persistent settings that should be saved when user quits
 * 
 * @author Kyle
 *
 */
public class ProfileSettings {
	@Tag(201) boolean muteMusic = true; // unmuted by default
	@Tag(202) boolean muteSound = false; // unmuted by default
	
	public void initializeAfterLoad() {
		// initialize any fields that were empty at load.
	}
	
	public void muteMusic() {
		this.muteMusic = true;
		Assets.music.pause();
	}
	
	public void unmuteMusic() {
		this.muteMusic = false;
		Assets.music.play();
	}
	
	public void muteSound() {
		this.muteSound = true;
	}
	
	public void unmuteSound() {
		this.muteSound = false;
	}
}
