package com.example.a2dpcontroller;

import android.content.res.ColorStateList;
import android.graphics.Color;

public class ProgressBarInfo {

    public static final int MAX_VALUE_QUALITY = 100;
    public static final int SBC_QUALITY = 30;
    public static final int AAC_QUALITY = 50;
    public static final int APTX_QUALITY = 60;
    public static final int APTXHD_QUALITY = 80;
    public static final int LDAC_QUALITY = 90;

    public static final int GREEN_BORDER = 80;
    public static final int YELLOW_BORDER = 50;
    public static final int RED_BORDER = 0;

    public static int getProgress(int codecType){
        switch (codecType){
            case Codec.SOURCE_CODEC_TYPE_SBC:
                return LDAC_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_AAC:
                return APTXHD_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_APTX:
                return APTX_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_APTX_HD:
                return AAC_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_LDAC:
                return SBC_QUALITY;
            default:
                return 0;
        }
    }

    public static String getText(int value){
        if(value >= GREEN_BORDER){
            return "Quality: Very Good";
        }else if(value >= YELLOW_BORDER){
            return "Quality: Good";
        }else if(value >= RED_BORDER){
            return"Quality: Weak";
        }else{
            return "Error";
        }
    }

    public static int getColor(int value){
        if(value >= GREEN_BORDER){
            return Color.GREEN;
        }else if(value >= YELLOW_BORDER){
            return Color.YELLOW;
        }else if(value >= RED_BORDER){
            return Color.RED;
        }else{
            return Color.WHITE;
        }
    }
}
