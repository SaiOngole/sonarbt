package com.sonar.bt;

import android.content.Context;
import android.media.MediaPlayer;

public class Util {
	
	private static Util utility;
	private MediaPlayer mediaPlayer;
	public static Util getInstance() {
		if(utility == null); {
		utility = new Util();
		}
		return utility;
	}
	public void startPlaying(Context context, MediaPlayer mediaPlayer) {
		if(this.mediaPlayer == null) {
			this.mediaPlayer = mediaPlayer;
			mediaPlayer.start();
		}
		else {
			this.mediaPlayer.start();
		}
	}
}
