package com.kebabking.game;

import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	static float MUTE_VOL = 0f;
	
	static float SIZZLE_VOL = 0.4f;
	static float MUSIC_VOL = 0.5f;
	static float ORDER_VOL = 1f;
	
//	static float SIZZLE_VOL = 0.0f;
//	static float MUSIC_VOL = 0.0f;
//	static float ORDER_VOL = 0f;	
	
//	static boolean getSettings().muteMusic;
//	static boolean getSettings().muteMusic;
	
	static KebabKing master;
	
	static boolean sizzlePlaying = false;
	static boolean musicPlaying = false;
	
	static float soundsVolume;
	
	public static void init(KebabKing masterIn) {
		master = masterIn;
	}
	
	public static ProfileSettings getSettings() {
		return master.profile.settings;
	}
	
	public static void playSizzle() {
		if (!sizzlePlaying) {
			if (getSettings().muteSound)
				Assets.sizzle.loop(MUTE_VOL);
			else 
				Assets.sizzle.loop(SIZZLE_VOL);

			sizzlePlaying = true;
		}
	}
	
	public static void stopSizzle() {
		if (sizzlePlaying) {
			Assets.sizzle.stop();
			sizzlePlaying = false;
		}
	}
	
	public static void muteSound() {
		KebabKing.print("Mute sound");
		getSettings().muteSound = true;
		Assets.sizzle.setVolume(0, MUTE_VOL);
		soundsVolume = MUTE_VOL;
	}
	
	public static void unmuteSound() {
		KebabKing.print("unMute sound");
		getSettings().muteSound = false;
		Assets.sizzle.setVolume(0, SIZZLE_VOL);
		soundsVolume = ORDER_VOL;
	}
	
	public static void muteMusic() {
		KebabKing.print("Mute music");
		getSettings().muteMusic = true;
		Assets.mainTheme.setVolume(MUTE_VOL);
	}

	public static void unmuteMusic() {
		KebabKing.print("unMute music");
		getSettings().muteMusic = false;
		Assets.mainTheme.setVolume(MUSIC_VOL);
	}
	
	public static void playCoin() {
		Assets.coinSound.play(soundsVolume);
	}
	
	public static void playCash() {
		Assets.cashSound.play(soundsVolume * 0.7f);
	}

	public static void playDayComplete() {
		Assets.dayCompleteSound.play(soundsVolume);
	}
	
	public static void playShutdown() {
		Assets.policemanSound.play(soundsVolume);
	}
	
	public static void playDayStart() {
		Assets.dayStartSound.play(soundsVolume);
	}
	
	public static void playLevelUp() {
		Assets.levelUpSound.play(soundsVolume);
	}
	
	public static void playTrash() {
		Assets.trash.play(soundsVolume * 1.2f);
	}
	
	public static void playUnlock() {
		Assets.unlockSound.play(soundsVolume);
	}
	
	public static void playButtonClick() {
		Assets.buttonClickSound.play(soundsVolume);
	}
	
	public static void pauseAll() {
		Assets.mainTheme.pause();
		Assets.sizzle.pause();
	}
	
	public static void resumeAll() {
		Assets.mainTheme.play();
		Assets.sizzle.resume();
	}
	
	public static void playMusic() {
		if (!musicPlaying) {
			Assets.mainTheme.play();
			musicPlaying = true;
		}
	}
	
	// Shouldn't ever be called
//	public static void stopMusic() {
//		if (musicPlaying) {
//			Assets.mainTheme.pause();
//			musicPlaying = false;
//		}
//	}
	
	public static void playOrderSound(Customer.CustomerType type) {
		Sound toPlay = null;
		switch(type) {
		case POLICE:
			toPlay = Assets.policemanSound;
			break;
		case FOREIGNER:
			toPlay = Assets.danSound;
			break;
//		case FOREIGNER:
//			toPlay = Assets.fatAmericanSound;
//			break;
		default:
			return;
		}
		
		toPlay.play(soundsVolume);
	}
	
	public static void playLeavingSound(Customer.CustomerType type, int satisfaction, boolean sick) {
		Sound toPlay = null;
//		if (satisfaction == 5) {
//			toPlay = Assets.veryYum;
//		}
//		else if (satisfaction == 4) {
//			toPlay = Assets.yum;
//		}	
		
//		switch(type) {
//		case POLICE:
//			if (rating > )
//			toPlay = Assets.policemanSound;
//			break;
//		case FOREIGNER:
//			toPlay = Assets.danSound;
//			break;
//		case TOURIST:
//			toPlay = Assets.fatAmericanSound;
//			break;
//		default:
//			return;
//		}
		
		if (sick) toPlay = Assets.sick;

		if (toPlay != null)
			toPlay.play(soundsVolume);
	}
}
