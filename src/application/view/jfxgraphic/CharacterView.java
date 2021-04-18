package application.view.jfxgraphic;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class CharacterView {
    public static final Integer UP_DIR = 0;
    public static final Integer RIGHT_DIR = 1;
    public static final Integer DOWN_DIR = 2;
    public static final Integer LEFT_DIR = 3;
    public static final Integer PL_WIN = 4;
    public static final Integer PL_DIE = 5;
    public static final Integer CAR_PL = 6;

    public static final Integer ENEMY_VIEW = 0;
    public static final Integer PLAYER_VIEW = 1;

    private ArrayList<ArrayList<Image>> images;
    private Integer state;
    private Integer type;
    private int index;
    private Image currentImage;
    int dim;
    private int animationTick;
    private boolean move;

    public CharacterView(String name, Integer type) {
        this.type = type;
        images = new ArrayList<>();
        dim = 16;
        move = false;
        index = 0;
        animationTick = 0;
        state = PL_WIN;
        for(int i = 0; i <= 3; i++) {
            ArrayList<Image> imageBlock = new ArrayList<>();
            for(int j = 0; j < 2; j++) {
                Image img = new Image(getClass().getResourceAsStream("/application/resources/" + name + "/" + i + j + ".png"));
                imageBlock.add(img);
            }
            images.add(imageBlock);
        }

        int l;
        if(type.equals(ENEMY_VIEW))
            l = 3;
        else
            l = 2;

        ArrayList<Image> imageBlock = new ArrayList<>();
        for(int j = 0; j < l; j++) {
            Image img = new Image(getClass().getResourceAsStream("/application/resources/" + name + "/4" + j + ".png"));
            imageBlock.add(img);
        }

        images.add(imageBlock);

        if(type.equals(PLAYER_VIEW)) {
            l = 10;
            for(int i = 5; i <= 6; i++) {
                imageBlock = new ArrayList<>();
                if(i == 6)
                    l = 2;

                for(int j = 0; j < l; j++) {
                    Image img = new Image(getClass().getResourceAsStream("/application/resources/" + name + "/" + i + j + ".png"));
                    imageBlock.add(img);
                }

                images.add(imageBlock);
            }
        }

        currentImage = images.get(state).get(0);
    }

    public void move() {
        move = true;
    }

    public void stop() {
        move = false;
    }

    public void update() {
        animationTick++;

        int refreshTick = (state.equals(PL_DIE)) ? 8 : 4;

        if(animationTick > refreshTick)
            animationTick = 0;

        if(animationTick != 0)
            return;

        if(move) {
            index++;
            if(index >= images.get(state).size()) {
                if(state.equals(PL_DIE)) {
                    currentImage = null;
                    return;
                }
                else
                    index = 0;
            }
        } else {
            index = 0;
        }

        currentImage = images.get(state).get(index);
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void setState(Integer state) {
        if(!this.state.equals(state))
            index = 0;
        this.state = state;
    }

    public Integer getState() {
        return state;
    }
}
