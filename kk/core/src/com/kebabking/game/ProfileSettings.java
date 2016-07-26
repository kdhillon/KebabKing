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

	@Tag(203) boolean dontShowAgain = false;

	public void initializeAfterLoad() {
		// initialize any fields that were empty at load.

		if (KebabKing.START_MUTED) {
			muteMusic = true;
			muteSound = true;
		}
	
		if (muteMusic) {
			SoundManager.muteMusic();
		}
		else SoundManager.unmuteMusic();
		
		if (muteSound) {
			SoundManager.muteSound();
		}
		else SoundManager.unmuteSound();
	}

	public void setDontShowAgain() {
		dontShowAgain = true;
	}
}
