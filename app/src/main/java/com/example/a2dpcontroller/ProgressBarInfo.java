package com.example.a2dpcontroller;

import android.graphics.Color;

public class ProgressBarInfo {

    public static final int MAX_VALUE_QUALITY = 100;
    public static final int SBC_QUALITY = 70;
    public static final int AAC_QUALITY = 82;
    public static final int APTX_QUALITY = 87;
    public static final int APTXHD_QUALITY = 100;
    public static final int LDAC_QUALITY = 0;

    public static final int DARK_GREEN_BORDER = 95;
    public static final int LIGHT_GREEN_BORDER = 80;
    public static final int YELLOW_BORDER = 50;
    public static final int RED_BORDER = 10;

    public static int getProgress(int codecType){
        switch (codecType){
            case Codec.SOURCE_CODEC_TYPE_LDAC:
                return LDAC_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_APTX_HD:
                return APTXHD_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_APTX:
                return APTX_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_AAC:
                return AAC_QUALITY;
            case Codec.SOURCE_CODEC_TYPE_SBC:
                return SBC_QUALITY;
            default:
                return 0;
        }
    }

    public static String getText(int value){
        if(value >= DARK_GREEN_BORDER){
            return "Quality: Excellent";
        }else if(value >= LIGHT_GREEN_BORDER){
            return "Quality: Good";
        }else if(value >= YELLOW_BORDER){
            return "Quality: Fine";
        }else if(value >= RED_BORDER){
            return "Quality: Weak";
        }else if(value == 0){
            return "Quality: Unavailable";
        }else{
            return "Error";
        }
    }

    public static int getColor(int value){
        if(value >= DARK_GREEN_BORDER){
            return Color.rgb(46,125,50);
        }else if(value >= LIGHT_GREEN_BORDER){
            return Color.rgb(124,179,66);
        }else if(value >= YELLOW_BORDER){
            return Color.YELLOW;
        }else if(value >= RED_BORDER){
            return Color.RED;
        }else{
            return Color.WHITE;
        }
    }
}
