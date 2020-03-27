package com.example.a2dpcontroller;

public class Codec {

    public static final int SOURCE_CODEC_TYPE_SBC     = 0;
    public static final int SOURCE_CODEC_TYPE_AAC     = 1;
    public static final int SOURCE_CODEC_TYPE_APTX    = 2;
    public static final int SOURCE_CODEC_TYPE_APTX_HD = 3;
    public static final int SOURCE_CODEC_TYPE_LDAC    = 4;
    public static final int SOURCE_CODEC_TYPE_MAX     = 5;

    public static final int SAMPLE_RATE_NONE   = 0;
    public static final int SAMPLE_RATE_44100  = 0x1 << 0;
    public static final int SAMPLE_RATE_48000  = 0x1 << 1;
    public static final int SAMPLE_RATE_88200  = 0x1 << 2;
    public static final int SAMPLE_RATE_96000  = 0x1 << 3;
    public static final int SAMPLE_RATE_176400 = 0x1 << 4;
    public static final int SAMPLE_RATE_192000 = 0x1 << 5;

    public static final int BITS_PER_SAMPLE_NONE = 0;
    public static final int BITS_PER_SAMPLE_16   = 0x1 << 0;
    public static final int BITS_PER_SAMPLE_24   = 0x1 << 1;
    public static final int BITS_PER_SAMPLE_32   = 0x1 << 2;

    private static final String[] CODEC_TYPES = {"SBC","AAC","aptX","aptX HD","LDAC","MAX"};

    Codec(){

    }

    public static String getCodecType(int i){
        switch(i){
            case SOURCE_CODEC_TYPE_SBC:
                return CODEC_TYPES[0];
            case SOURCE_CODEC_TYPE_AAC:
                return CODEC_TYPES[1];
            case SOURCE_CODEC_TYPE_APTX:
                return CODEC_TYPES[2];
            case SOURCE_CODEC_TYPE_APTX_HD:
                return CODEC_TYPES[3];
            case SOURCE_CODEC_TYPE_LDAC:
                return CODEC_TYPES[4];
            case SOURCE_CODEC_TYPE_MAX:
                return CODEC_TYPES[5];
            default:
                return "ERROR";
        }
    }

}
