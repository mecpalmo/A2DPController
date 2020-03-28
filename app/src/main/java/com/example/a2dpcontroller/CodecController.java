package com.example.a2dpcontroller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class CodecController {

    private BluetoothA2dp a2dp;

    private int SAMPLE_RATE = 0;
    private int BITS_PER_SAMPLE = 0;

    CodecController(){
        //initialA2DP();
    }

    public void setA2dp(BluetoothA2dp setter){
        a2dp = setter;
    }

    public int getCurrentCodec(){
        Object bcodecstatus;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                bcodecstatus = getCodecStatus().invoke(a2dp,(Object)getBluetoothDevice());
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
        int codecName;
        if(bcodecconfig != null) {
            try {
                codecName = (int) getCodecType().invoke(bcodecconfig);
                SAMPLE_RATE = (int)getSampleRate().invoke(bcodecconfig);
                BITS_PER_SAMPLE = (int)getBitsPerSample().invoke(bcodecconfig);
                return codecName;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return 10;
            }
        }else {
            return 10;
        }
    }

    public List<Integer> getLocalCodecs(){
        Object bcodecstatus;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                bcodecstatus = getCodecStatus().invoke(a2dp,getBluetoothDevice());
                Log.i("MYAPP","b codec status got successfully");
            }else{
                bcodecstatus = getCodecStatus().invoke(a2dp);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            bcodecstatus = null;
            Log.i("MYAPP", "b codec status is null");
        }
        Object[] bcodecconfig;
        try {
            bcodecconfig = (Object[]) getCodecsLocalCapabilities().invoke(bcodecstatus);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            bcodecconfig= null;
        }
        List<Integer> codecs = null;
        for(int i=0; i<bcodecconfig.length; i++){
            try {
                Object config = bcodecconfig[i];
                int num = (int)getCodecType().invoke(config);
                codecs.add(num);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return codecs;
    }

    public List<Integer> getSelectableCodecs(){
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
        Object[] bcodecconfig = null;
        try {
            bcodecconfig = (Object[])getCodecsSelectableCapabilities().invoke(bcodecstatus);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        List<Integer> codecs = null;
        for(int i=0; i<bcodecconfig.length; i++){
            try {
                Object config = bcodecconfig[i];
                int num = (int)getCodecType().invoke(config);
                codecs.add(num);
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
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Method getCodecType(){
        try {
            return Class.forName("android.bluetooth.BluetoothCodecConfig").getDeclaredMethod("getCodecType");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BluetoothDevice getBluetoothDevice(){
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                Method getActiveDevice = BluetoothA2dp.class.getDeclaredMethod("getActiveDevice");
                BluetoothDevice device = (BluetoothDevice)getActiveDevice.invoke(a2dp);
                Log.i("MYAPP", device.getName());
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
