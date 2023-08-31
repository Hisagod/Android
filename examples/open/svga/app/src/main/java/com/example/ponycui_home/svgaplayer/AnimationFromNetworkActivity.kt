package com.example.ponycui_home.svgaplayer

import android.app.Activity
import android.os.Bundle
import com.opensource.svgaplayer.SVGAImageView

class AnimationFromNetworkActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_from_network)
        //        animationView = new SVGAImageView(this);
//        animationView.setBackgroundColor(Color.GRAY);
//        setContentView(animationView);
//        loadAnimation();
    }

    private fun loadAnimation() {
        try { // new URL needs try catch.
//            SVGAParser svgaParser = SVGAParser.Companion.shareParser();
//            svgaParser.setFrameSize(100,100);
//            svgaParser.decodeFromURL(new URL("https://github.com/yyued/SVGA-Samples/blob/master/posche.svga?raw=true"), new SVGAParser.ParseCompletion() {
//                @Override
//                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
//                    Log.d("##","## FromNetworkActivity load onComplete");
//                    animationView.setVideoItem(videoItem);
//                    animationView.startAnimation();
//                }
//                @Override
//                public void onError() {
//
//                }
//
//
//            },null);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}