package com.androidcourse.toktik.player;

public enum MediaSourceType {
    NATIVE_RESOURCE(1),
    URI(2),
    LINK(3);

    int val;

    MediaSourceType(int val) {
        this.val = val;
    }
}
