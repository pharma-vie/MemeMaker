package com.dinosilvestro.mememaker.anims;


import android.support.design.widget.FloatingActionButton;

public class Animations {

    // This animation will reduce the size of the button when tapped
    // and then resize it back to normal
    public static void fabButtonAnimate(FloatingActionButton fab) {
        fab.setScaleX(0);
        fab.setScaleY(0);
        fab.animate().scaleX(1).scaleY(1).start();
    }
}
