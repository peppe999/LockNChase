package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class AudioSystem {
    private Media media;
    private MediaPlayer player;

    private static AudioSystem instance = null;

    private AudioSystem() {

    }

    public static AudioSystem getInstance() {
        if(instance == null)
            instance = new AudioSystem();

        return instance;
    }

    public void playStart() {
        /*try {
            media = new Media(getClass().getResource("/application/resources/sound/startGame.mp3").toString());
            player = new MediaPlayer(media);
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    player.play();
                    System.out.println(player.isMute());
                    System.out.println(player.getTotalDuration());
                    System.out.println(player.getVolume());
                    System.out.println(player.getStatus());
                    player.play();
                }
            });
            player.setOnPlaying(new Runnable() {
                @Override
                public void run() {
                    System.out.println("sto playandoo");
                }
            });
        } catch (MediaException e) {
            System.out.println("aaaaaa");
        }*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                media = new Media(getClass().getResource("/application/resources/sound/startGame.mp3").toString());
                player = new MediaPlayer(media);
                player.play();
            }
        }).start();

        //player.setAutoPlay(true);

    }
}
