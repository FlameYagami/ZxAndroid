package com.zx.bean;

/**
 * Created by 八神火焰 on 2017/9/8.
 */

public class DeckExBean
{
    private DeckBean deckBean;
    private int      count;

    public DeckExBean(DeckBean deckBean, int count) {
        this.deckBean = deckBean;
        this.count = count;
    }

    public DeckBean getDeckBean() {
        return deckBean;
    }

    public int getCount() {
        return count;
    }
}
