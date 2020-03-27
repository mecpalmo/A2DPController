package com.example.a2dpcontroller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CodecController {

    private static BluetoothA2dp a2dp;

    private static int SAMPLE_RATE = 0;
    private static int BITS_PER_SAMPLE = 0;

    CodecController(){
        //initialA2DP();
    }

    public static void setA2dp(BluetoothA2dp setter){
        a2dp = setter;
    }

    public String getCurrentCodec(){
        Object bcodecstatus;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                bcodecstatus = getCodecStatus().invoke(a2dp,getBluetoothDevice());
            }else{
                bcodecstatus = getCodecStatus().invoke(a2dp);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            bcodecstatus = null;
        }
        Object bcodecconfig;
        if(bcodecstatus != null){
            try {
                bcodecconfig = getCodecConfig().invoke(bcodecstatus);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                bcodecconfig = null;
            }
        }else{
            bcodecconfig = null;
        }
        String codecName;
        if(bcodecconfig != null) {
            try {
                codecName = (String) getCodecName().invoke(bcodecconfig);
                SAMPLE_RATE = (int)getSampleRate().invoke(bcodecconfig);
                BITS_PER_SAMPLE = (int)getBitsPerSample().invoke(bcodecconfig);
                return codecName;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }else {
            return null;
        }
    }

    public List<String> getLocalCodecs(){
        Object bcodecstatus;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                bcodecstatus = getCodecStatus().invoke(a2dp,getBluetoothDevice());
            }else{
                bcodecstatus = getCodecStatus().invoke(a2dp);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            bcodecstatus = null;
        }
        List<Object> bcodecconfig = null;
        try {
            bcodecconfig = (List<Object>) getCodecsLocalCapabilities().invoke(bcodecstatus);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        List<String> codecs = null;
        for(int i=0; i<bcodecconfig.size(); i++){
            try {
                codecs.add((String)getCodecName().invoke(bcodecconfig.get(i)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return codecs;
    }

    public List<String> getSelectableCodecs(){
        Object bcodecstatus;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                bcodecstatus = getCodecStatus().invoke(a2dp,getBluetoothDevice());
            }else{
                bcodecstatus = getCodecStatus().invoke(a2dp);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            bcodecstatus = null;
        }
        List<Object> bcodecconfig = null;
        try {
            bcodecconfig = (List<Object>) getCodecsSelectableCapabilities().invoke(bcodecstatus);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        List<String> codecs = null;
        for(int i=0; i<bcodecconfig.size(); i++){
            try {
                codecs.add((String)getCodecName().invoke(bcodecconfig.get(i)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return codecs;
    }

    public void setCodec(int CODEC_TYPE){
        Object bcodecconfig = null;
        try {
            bcodecconfig = Class.forName("android.bluetooth.BluetoothCodecConfig");
            setCodecPriority().invoke(bcodecconfig, new int[]{CODEC_TYPE});
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if(bcodecconfig != null) {
            Object params[] = {(Object)getBluetoothDevice(),(Object)bcodecconfig};
            try {
                setCodecConfigPreference().invoke(a2dp, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSAMPLE_RATE(){
        return SAMPLE_RATE;
    }

    public int getBITS_PER_SAMPLE(){
        return BITS_PER_SAMPLE;
    }

    private void initialA2DP(){
        try {
            a2dp = BluetoothA2dp.class.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

    }

    private Method getCodecStatus(){
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            {
                return BluetoothA2dp.class.getDeclaredMethod("getCodecStatus", BluetoothDevice.class);
            }else {
                return BluetoothA2dp.class.getDeclaredMethod("getCodecStatus");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getCodecConfig(){
        try {
            return Class.forName("android.bluetooth.BluetoothCodecStatus").getDeclaredMethod("getCodecConfig");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private Method getCodecName(){
        try {
            return Class.forName("android.bluetooth.BluetoothCodecConfig").getDeclaredMethod("getCodecName");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BluetoothDevice getBluetoothDevice(){
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                Method getActiveDevice = BluetoothA2dp.class.getDeclaredMethod("getActiveDevice");
                BluetoothDevice device = (BluetoothDevice)getActiveDevice.invoke(a2dp);
                return device;
            }else {
                return null;
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        }

    private Method setCodecConfigPreference(){
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Class<?> params[] = {BluetoothDevice.class, Class.forName("android.bluetooth.BluetoothCodecConfig")};
                return BluetoothA2dp.class.getDeclaredMethod("setCodecConfigPreference", params);
            }else{
                return BluetoothA2dp.class.getDeclaredMethod("setCodecConfigPreference", Class.forName("android.bluetooth.BluetoothCodecConfig"));
            }
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method setCodecPriority(){
        try {
            return Class.forName("android.bluetooth.BluetoothCodecConfig").getDeclaredMethod("setCodecPriority", int.class);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getCodecsLocalCapabilities(){
        try {
            return Class.forName("android.bluetooth.BluetoothCodecStatus").getDeclaredMethod("getCodecsLocalCapabilities");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getCodecsSelectableCapabilities(){
        try {
            return Class.forName("android.bluetooth.BluetoothCodecStatus").getDeclaredMethod("getCodecsSelectableCapabilities");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getSampleRate(){
        try{
            return Class.forName("android.bluetooth.BluetoothCodecConfig").getDeclaredMethod("getSampleRate");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getBitsPerSample(){
        try{
            return Class.forName("android.bluetooth.BluetoothCodecConfig").getDeclaredMethod("getBitsPerSample");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}
