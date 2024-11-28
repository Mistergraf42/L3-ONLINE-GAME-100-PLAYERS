package physique;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.File;

public class Musique {


    static Clip clip;

    Musique() {

    }


    /**
     * Lance une musique
     * @param filePath chemin relatif du fichier sur lequel la musique est 
     */
    public static void playMusic(String filePath) {
        try {
            if (clip != null) {
                clip.stop();
            }
            File soundFile = new File(filePath);
            if(soundFile.exists()){
                System.out.println("Le fichier existe");
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            // si fin
            // clip.addLineListener(new LineListener() {
            //     @Override
            //     public void update(LineEvent event) {
            //         if (event.getType() == LineEvent.Type.STOP) {
            //             // :( c la fin
            //             clip.close();
                        
            //         }
            //     }
            // });
            // clip.start();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Joue un son et ne coupe pas la musique 
     * @param filePath Chemin relatif du son
     */
    public static void playSound(String filePath) {
        try {
            if (clip != null) {
                System.out.println("avant : false");

            } else {
                System.out.println("avant : true");

            }
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip c = AudioSystem.getClip();
            c.open(audioIn);
            // si fin
            c.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        // :( c la fin
                        c.close();
                    }
                }
            });

            c.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}