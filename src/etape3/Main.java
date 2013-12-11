package etape3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import libs.*;

public class Main {
	public static Boolean isPlaying = false;

	public static double laTemperature = 0;
	public static AdvancedPlayer player;

	public static void main(String[] args) {
		System.out.println("DEBUT...");
		try {
			File file = new File("track.mp3");
			FileInputStream fis = new FileInputStream(file);
		} catch (Exception e) {
			// Handle exception.
		}
		
		Etape1 e1 = new Etape1(1000);
		e1.addCapteurListener(new CapteurListener() {
			@Override
			public void TemperatureChanged(double temperature, double humidite) {
				Main.laTemperature = temperature;
				//System.out.println("Temperature : " + temperature + " ; Humidite : " + humidite);
			}
		});
		e1.run();

		

		FaceDetection f = new FaceDetection();

		f.addWebcamListener(new WebcamListener() {

			@Override
			public void SomebodyDisappear() {
				isPlaying = false;
				System.out.println("Plus personne n'est la..");
				player.stop();
			}

			@Override
			public void SomebodyAppear(int numberOfPeople) {
				System.out.println("Temperature : " + laTemperature);
				if (isPlaying) {
					return;
				}
				try {
					player.play();
				} catch (JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		f.run();
	}

}
