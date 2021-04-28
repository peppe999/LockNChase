package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.HashMap;

public class AudioManager {

    private HashMap <String, Media> collezione;
    private Media media;
    private MediaPlayer mediaPlayerPrincipal, secondary;
    private  boolean status;

    private static AudioManager instance = null;

    private AudioManager() {
        this.status = false;
        collezione = new HashMap<>();
        load();
    }

    private void load(){
        media = new Media(Paths.get("src/application/resources/sound/startGame.mp3").toUri().toString());
        collezione.put("home", media);
        media = new Media(Paths.get("src/application/resources/sound/doorClosed.mp3").toUri().toString());
        collezione.put("doorClosed", media);
        media = new Media(Paths.get("src/application/resources/sound/idk.mp3").toUri().toString());
        collezione.put("idk", media);
        media = new Media(Paths.get("src/application/resources/sound/money.mp3").toUri().toString());
        collezione.put("money", media);
        media = new Media(Paths.get("src/application/resources/sound/run.mp3").toUri().toString());
        collezione.put("run", media);
        media = new Media(Paths.get("src/application/resources/sound/win.mp3").toUri().toString());
        collezione.put("win", media);
    }

    public static AudioManager getInstance() {
        if(instance == null)
            instance = new AudioManager();

        return instance;
    }

    public  void homeMedia(){
        if(status == false){
            status = true;
            mediaPlayerPrincipal = new MediaPlayer(collezione.get("home"));
            mediaPlayerPrincipal.setOnEndOfMedia( new Thread(new Runnable() {
                @Override
                public void run() {
                    mediaPlayerPrincipal.seek(Duration.ZERO);
                }
            }));
            mediaPlayerPrincipal.play();
        }
    }

    public  void runMedia(){
        mediaPlayerPrincipal = new MediaPlayer(collezione.get("run"));
        mediaPlayerPrincipal.setOnEndOfMedia( new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayerPrincipal.seek(Duration.ZERO);
            }
        }));
        mediaPlayerPrincipal.play();
    }

    public  void win(){
        secondary = new MediaPlayer(collezione.get("win"));
        secondary.play();
    }

    public  void money(){
        secondary = new MediaPlayer(collezione.get("money"));
        secondary.play();
    }

    public  void doorClosed(){
        secondary = new MediaPlayer(collezione.get("doorClosed"));
        secondary.play();
    }

    public  void idk(){
        secondary = new MediaPlayer(collezione.get("idk"));
        secondary.play();
    }
    
}
