package uk.ac.soton.comp1206.network;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Multimedia {

    /** The logger for this class, used to output information to the console */
    private static final Logger logger = LogManager.getLogger(Multimedia.class);

    /** The property to determine if audio is enabled */
    private final BooleanProperty audioEnabledProperty = new SimpleBooleanProperty(true);

    /** The audio player for sound effects */
    private MediaPlayer audioPlayer;

    /** The audio player for background music */
    private MediaPlayer musicPlayer;

    /** Get the audio enabled property */
    public boolean getAudioEnabled() {
        return audioEnabledProperty.get();
    }


    /**
     * Set the audio enabled property
     *
     * @param enabled the value to set the property to
     */
    public void setAudioEnabled(boolean enabled) {
        logger.info("Audio enabled set to: " + enabled);
        audioEnabledProperty().set(enabled);
    }

    /**
     * Get the audio enabled property
     *
     * @return the audio enabled property
     */
    public BooleanProperty audioEnabledProperty() {
        return audioEnabledProperty;
    }

    /**
     * Play an audio file
     *
     * @param f the filename of the audio file
     */
    public void playSoundEffect(String f) {
        // If audio is not enabled, return
        if (!getAudioEnabled()) return;
        // Get the URL of the audio file
        String audio = Multimedia.class.getResource("/sounds/" + f).toExternalForm();
        logger.info("Starting to play audio" + audio);
        // Try to play the audio file
        try {
            var toPlay = new Media(audio);
            audioPlayer = new MediaPlayer(toPlay);
            audioPlayer.play();
        } catch (Exception e) {
            // If the audio file cannot be played, disable audio
            setAudioEnabled(false);
            e.printStackTrace();
            logger.error("Unable to play audio file, disabling audio");
        }
    }
    /**
     * Play a music file
     *
     * @param f the filename of the music file
     */

    public void playBackgroundMusic(String f) {
        // If audio is not enabled, return
        if (!getAudioEnabled()) return;
        // Get the URL of the music file
        String music = Multimedia.class.getResource("/sounds/" + f).toExternalForm();
        logger.info("Starting to play music" + music);

        // Try to play the music file
        try {
            var toPlay = new Media(music);
            musicPlayer = new MediaPlayer(toPlay);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.play();
        } catch (Exception e) {
            // If the music file cannot be played, disable audio
            setAudioEnabled(false);
            logger.error("Unable to play audio file, disabling audio");
            e.printStackTrace();
        }

    }
    public boolean getMusicEnabled() {
        if (musicPlayer != null){
            return true;
        }else {
            return false;
        }

    }
    public void stop() {
        logger.info("Stopping the current music");
        musicPlayer.stop();
    }
    public void pause() {
        musicPlayer.pause();
    }
    public void start() {
        musicPlayer.play();
    }
}