package com.dab.zx.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 八神火焰 on 2016/12/21.
 */

public class DeckPreviewBean {
    private String deckName;
    private String statusMain;
    private String statusExtra;
    private String playerPath;
    private String startPath;
    private List<String> numberExList = new ArrayList<>();

    public DeckPreviewBean(String deckName, String statusMain, String statusExtra, String playerPath, String startPath, List<String> numberExList) {
        this.deckName = deckName;
        this.statusMain = statusMain;
        this.statusExtra = statusExtra;
        this.playerPath = playerPath;
        this.startPath = startPath;
        this.numberExList = numberExList;
    }

    public String getStatusExtra() {
        return statusExtra;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getStatusMain() {
        return statusMain;
    }

    public String getPlayerPath() {
        return playerPath;
    }

    public String getStartPath() {
        return startPath;
    }

    public List<String> getNumberExList() {
        return numberExList;
    }
}
