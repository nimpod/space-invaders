package spaceinvaders.core;

import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound{

	private Clip soundClip;

	public Sound(String path) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new URL(path));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels()*2,
					baseFormat.getSampleRate(),
					false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			soundClip = AudioSystem.getClip();
			soundClip.open(dais);
		} catch(Exception e) { e.printStackTrace(); }
	}

	public void play() {
		if (soundClip == null) return;
		stop();
		soundClip.setFramePosition(0);
		soundClip.start();
	}

	public void close() {
		stop();
		soundClip.close();
	}

	public void stop() {
		soundClip.stop();
		soundClip.setFramePosition(1);
	}

	public void loop() {
		soundClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

}