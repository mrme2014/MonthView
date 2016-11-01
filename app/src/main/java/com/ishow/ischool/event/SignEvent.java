package com.ishow.ischool.event;

/**
 * Created by mini on 16/11/1.
 */

public class SignEvent {
    public int updatePosition;

    public SignEvent(int updatePosition) {
        this.updatePosition = updatePosition;
    }

    public int getUpdatePosition() {
        return updatePosition;
    }
}
