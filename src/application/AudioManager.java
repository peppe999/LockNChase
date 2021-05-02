package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioManager {

    private HashMap <String, Media> collection;
    private Media media;
    private MediaPlayer primaryPlayer;
    private List<MediaPlayer> secondaryPlayers;
    private  boolean status;
    private int money;

    private static AudioManager instance = null;

    private AudioManager() {
        this.status = false;
        money = 0;
        collection = new HashMap<>();
        secondaryPlayers = new ArrayList<>();
        load();
    }

    private void load(){
        media = new Media(Paths.get("src/application/resources/sound/startGame.mp3").toUri().toString());
        collection.put("startGame", media);
        media = new Media(Paths.get("src/application/resources/sound/bonusLife.mp3").toUri().toString());
        collection.put("bonusLife", media);
        media = new Media(Paths.get("src/application/resources/sound/die.mp3").toUri().toString());
        collection.put("die", media);
        media = new Media(Paths.get("src/application/resources/sound/doorClosed.mp3").toUri().toString());
        collection.put("doorClosed", media);
        media = new Media(Paths.get("src/application/resources/sound/bagSpawn.mp3").toUri().toString());
        collection.put("bagSpawn", media);
        media = new Media(Paths.get("src/application/resources/sound/lockedEnemies.mp3").toUri().toString());
        collection.put("lockedEnemies", media);
        media = new Media(Paths.get("src/application/resources/sound/money0.mp3").toUri().toString());
        collection.put("money0", media);
        media = new Media(Paths.get("src/application/resources/sound/money1.mp3").toUri().toString());
        collection.put("money1", media);
        media = new Media(Paths.get("src/application/resources/sound/pickBag.mp3").toUri().toString());
        collection.put("pickBag", media);
        media = new Media(Paths.get("src/application/resources/sound/pickLvlBonus.mp3").toUri().toString());
        collection.put("pickLvlBonus", media);
        media = new Media(Paths.get("src/application/resources/sound/run.wav").toUri().toString());
        collection.put("run", media);
        media = new Media(Paths.get("src/application/resources/sound/win.mp3").toUri().toString());
        collection.put("win", media);
        media = new Media(Paths.get("src/application/resources/sound/beep.mp3").toUri().toString());
        collection.put("beep", media);
    }

    public static AudioManager getInstance() {
        if(instance == null)
            instance = new AudioManager();

        return instance;
    }

    private void stopAll() {
        System.out.println(secondaryPlayers.size());
        for(MediaPlayer secondaryPlayer : secondaryPlayers)
            secondaryPlayer.stop();
        secondaryPlayers.clear();
    }

    public void primaryPlay(String soundName, boolean autoRepeat, String nextSound, boolean stopAll) {
        if(primaryPlayer != null) {
            primaryPlayer.stop();

            if (stopAll)
                stopAll();
        }

        primaryPlayer = new MediaPlayer(collection.get(soundName));

        if(autoRepeat)
            primaryPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        primaryPlayer.play();

        if(!autoRepeat && nextSound != null && collection.containsKey(nextSound)) {
            primaryPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    primaryPlay(nextSound, true, null, false);
                }
            });
        }
    }

    public void secondaryPlay(String soundName, boolean stop) {
        if(stop) {
            stopAll();
        }

        if(soundName.equals("money")) {
            soundName = "money" + money++;

            if(money > 1)
                money = 0;
        }

        if(collection.containsKey(soundName)) {
            MediaPlayer secondaryPlayer = new MediaPlayer(collection.get(soundName));
            secondaryPlayers.add(secondaryPlayer);
            secondaryPlayer.play();

            secondaryPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    secondaryPlayers.remove(secondaryPlayer);
                }
            });
        }
    }

    /*public  void homeMedia(){
        if(status == false) {
            status = true;
            primaryPlayer = new MediaPlayer(collection.get("startGame"));
            primaryPlayer.setOnEndOfMedia( new Thread(new Runnable() {
                @Override
                public void run() {
                    primaryPlayer.seek(Duration.ZERO);
                }
            }));
            primaryPlayer.play();
        }
    }

    public  void runMedia(){
        primaryPlayer.stop();
        primaryPlayer = new MediaPlayer(collection.get("run"));
        primaryPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                //primaryPlayer.setStopTime(primaryPlayer.getTotalDuration().subtract(new Duration(3000)));
                primaryPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                primaryPlayer.play();

            }
        });*/
        /*primaryPlayer.setOnEndOfMedia( new Thread(new Runnable() {
            @Override
            public void run() {
                primaryPlayer.seek(primaryPlayer.getStartTime());
                //primaryPlayer.play();
            }
        }));*/
        //primaryPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        //primaryPlayer.play();
        /*primaryPlayer.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                primaryPlayer.seek(primaryPlayer.getStartTime());
            }
        });

    }

    public  void win(){
        secondaryPlayer = new MediaPlayer(collection.get("win"));
        secondaryPlayer.play();
    }

    public  void money(){
        secondaryPlayer = new MediaPlayer(collection.get("money" + money));
        secondaryPlayer.play();

        money++;
        if(money > 1)
            money = 0;
    }

    public  void doorClosed(){
        secondaryPlayer = new MediaPlayer(collection.get("doorClosed"));
        secondaryPlayer.play();
    }

    public  void idk(){
        secondaryPlayer = new MediaPlayer(collection.get("bagSpawn"));
        secondaryPlayer.play();
    }
    */

    public void pauseAll() {
        primaryPlayer.pause();
        for(MediaPlayer secondaryPlayer : secondaryPlayers)
            secondaryPlayer.pause();
    }

    public void resumeAll() {
        primaryPlayer.play();
        for(MediaPlayer secondaryPlayer : secondaryPlayers)
            secondaryPlayer.play();
    }
}
