package com.chinalwb.are.models;

import java.io.Serializable;

/**
 * Created by wliu on 2018/2/4.
 */

public class AtItem implements Serializable {
    public int mIconId;
    public String mName;
    public String mKey;
    public int color;

    public AtItem(int iconId, String name) {
        this.mIconId = iconId;
        this.mName = name;
        this.mKey = String.valueOf(iconId);
    }
}
