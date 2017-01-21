package com.zx.event;

import com.zx.bean.CardBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 八神火焰 on 2016/12/23.
 */

public class CardListEvent implements Serializable
{
    private List<CardBean> cardList = new ArrayList<>();

    public List<CardBean> getCardList() {
        return cardList;
    }

    public CardListEvent(List<CardBean> cardList) {
        this.cardList.addAll(cardList);
    }
}
