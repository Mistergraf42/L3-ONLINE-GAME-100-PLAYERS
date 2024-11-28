package physique;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioObstacle extends Thread {
    private String filePath;
    public AudioObstacle(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        File audioFile = new File(filePath);

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Format audio non pris en charge : " + format);
                return;
            }

            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.start();

            // Attendre la fin de la lecture
            Thread.sleep(audioClip.getMicrosecondLength() / 1000);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
