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

    Codec(){ }

    public static String getCodecName(int mCodecType) {
        switch (mCodecType) {
            case SOURCE_CODEC_TYPE_SBC:
                return "SBC";
            case SOURCE_CODEC_TYPE_AAC:
                return "AAC";
            case SOURCE_CODEC_TYPE_APTX:
                return "aptX";
            case SOURCE_CODEC_TYPE_APTX_HD:
                return "aptX HD";
            case SOURCE_CODEC_TYPE_LDAC:
                return "LDAC";
            default:
                return "";
        }
    }

}
