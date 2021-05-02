package application.model;


import java.util.HashMap;
import java.util.Map;

public class NotifiableStates {
    private Map<String, Boolean> states;

    public NotifiableStates() {
        states = new HashMap<>();
        loadDefaultStates();
    }

    private void loadDefaultStates() {
        states.put("startGame", false);
        states.put("bonusLife", false);     // manchi tu
        states.put("die", false);
        states.put("doorClosed", false);
        states.put("bagSpawn", false);
        states.put("lockedEnemies", false);
        states.put("money", false);
        states.put("pickBag", false);
        states.put("pickLvlBonus", false);
        states.put("win", false);
        states.put("newLvl", false);
    }

    public boolean getStateValue(String state) {
        return states.getOrDefault(state, false);
    }

    public boolean processState(String state) {
        return states.put(state, false) != null;
    }

    public boolean notifyState(String state) {
        return states.put(state, true) != null;
    }
}
