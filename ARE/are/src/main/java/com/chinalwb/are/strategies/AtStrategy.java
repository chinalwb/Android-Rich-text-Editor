package com.chinalwb.are.strategies;

import com.chinalwb.are.models.AtItem;

public interface AtStrategy {
    /**
     * In this method, you want to start an activity to show a list for user to pick up.
     */
    void openAtPage();

    /**
     * ARE has a default behaviour for handling the event when user selects an {@AtItem}.
     *
     * If you want to customize, you can do it in this method and return true;
     * or else if you want to use the default implementation, just return false, then ARE
     * will take care the event.
     *
     * @param item
     * @return
     */
    boolean onItemSelected(AtItem item);
}
