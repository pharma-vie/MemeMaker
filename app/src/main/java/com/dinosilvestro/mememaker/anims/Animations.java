package com.dinosilvestro.mememaker.anims;


import android.support.design.widget.FloatingActionButton;

public class Animations {

    public static void fabButtonAnimate(FloatingActionButton fab) {
        fab.setScaleX(0);
        fab.setScaleY(0);
        fab.animate().scaleX(1).scaleY(1).start();
    }
}
