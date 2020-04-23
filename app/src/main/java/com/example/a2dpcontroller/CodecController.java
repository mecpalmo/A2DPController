package com.example.a2dpcontroller;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CodecController {

    private static BluetoothA2dp a2dp;
    private static Object bcodecconfig;

    private int codec = -1;
    private int SAMPLE_RATE = 0;
    private int BITS_PER_SAMPLE = 0;
    ArrayList<Integer> localCodecs = new ArrayList<>();
    ArrayList<Integer> selectableCodecs = new ArrayList<>();
    ArrayList<String> devicesNames = new ArrayList<>();

    CodecController(){
    }

    public void setA2dp(BluetoothA2dp setter){
        a2dp = setter;
    }

    public boolean getCurrentCodec(){
        boolean success = true;
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
            success = false;
        }

        if(bcodecstatus != null){
            try {
                bcodecconfig = getCodecConfig().invoke(bcodecstatus);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                bcodecconfig = null;
                success = false;
            }
        }else{
            bcodecconfig = null;
            success = false;
        }

        if(bcodecconfig != null) {
            try {
                codec = (int) getCodecType().invoke(bcodecconfig);
                SAMPLE_RATE = (int)getSampleRate().invoke(bcodecconfig);
                BITS_PER_SAMPLE = (int)getBitsPerSample().invoke(bcodecconfig);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                codec = -1;
                success = false;
            }
        }else {
            codec = -1;
            success = false;
        }

        Object[] localconfig;
        try {
            localconfig = (Object[]) getCodecsLocalCapabilities().invoke(bcodecstatus);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            localconfig= null;
            success = false;
        }
        while(localCodecs.size()>0){
            localCodecs.remove(localCodecs.size()-1);
        }
        for(int i=0; i<localconfig.length; i++){
            try {
                Object config = localconfig[i];
                localCodecs.add((int)getCodecType().invoke(config));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                success = false;
            }
        }

        Object[] selectableconfig = null;
        try {
            selectableconfig = (Object[])getCodecsSelectableCapabilities().invoke(bcodecstatus);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            success = false;
        }
        while(selectableCodecs.size()>0){
            selectableCodecs.remove(selectableCodecs.size()-1);
        }
        for(int i=0; i<selectableconfig.length; i++){
            try {
                Object config = selectableconfig[i];
                selectableCodecs.add((int)getCodecType().invoke(config));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                success = false;
            }
        }
        getBluetoothDevices();
        return success;
    }

    public boolean setCodec(int CODEC_TYPE){
        boolean success = true;
        try {
            setCodecPriority().invoke(bcodecconfig, CODEC_TYPE);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            success = false;
        }
        if(bcodecconfig != null) {
            Object params[] = {(Object)getBluetoothDevice(),(Object)bcodecconfig};
            try {
                setCodecConfigPreference().invoke(a2dp, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                success = false;
            }
        }else {
            success = false;
        }
        return success;
    }

    public int getSAMPLE_RATE(){
        return SAMPLE_RATE;
    }

    public int getBITS_PER_SAMPLE(){
        return BITS_PER_SAMPLE;
    }

    public int getCodec(){ return codec;}

    public List<Integer> getLocalCodecs(){return localCodecs;}

    public List<Integer> getSelectableCodecs(){return selectableCodecs;}

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
                return device;
            }else {
                return null;
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getDevicesNames(){
        return devicesNames;
    }

    private void getBluetoothDevices(){
        try {
            Method getConnectedDevices = BluetoothA2dp.class.getDeclaredMethod("getConnectedDevices");
            List<BluetoothDevice> list = (List<BluetoothDevice>)getConnectedDevices.invoke(a2dp);
            devicesNames = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                devicesNames.add(list.get(i).getName());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            devicesNames = new ArrayList<>();
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
