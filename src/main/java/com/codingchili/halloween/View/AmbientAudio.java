package com.codingchili.halloween.view;

/** 
 * @author Robin Duda
 * @version 1.0
 * @date 2014-10-26
 * 
 * View.AnimationState: animations for the Card Flip.
 */

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public abstract class AmbientAudio implements Runnable {

	//plays ambient.wav on repeat.
    public static void Play(Object obj) throws Exception {
        Clip clip = AudioSystem.getClip();
        InputStream is =  obj.getClass().getClassLoader().getResourceAsStream("ambient.wav");
        AudioInputStream ais = AudioSystem.getAudioInputStream(is);
        clip.open(ais);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
