package com.example.a2dpcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BluetoothBroadcastReceiver.Callback, BluetoothA2DPRequester.Callback {

    private static final String TAG = "MyApp";
    private BluetoothAdapter mAdapter;
    public CodecController codecController;

    private fragment1 fragment;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private ProgressBar progressBar;
    private Button button1;
    private Button button2;

    private int MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_SBC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codecController = new CodecController();
        setViewComponents();
        setDefaultText();

        initiateBluetoothAdapter();
    }

    public void initiateBluetoothAdapter(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter.isEnabled()) {
            new BluetoothA2DPRequester(this).request(this, mAdapter);
            getAllInfo();
        }
    }

    private void setViewComponents(){
        fragment = (fragment1)getSupportFragmentManager().findFragmentById(R.id.fragment);
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button2.setEnabled(false);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDesiredCodec();
            }
        });

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(ProgressBarInfo.MAX_VALUE_QUALITY);
    }

    private void setDesiredCodec(){
        codecController.setCodec(MAX_SOURCE_CODEC_TYPE);
        getCurrentCodec();
    }

    private void getCurrentCodec(){
        String codec = codecController.getCurrentCodec();
        if(codec == Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_LDAC)){
            progressBar.setProgress(ProgressBarInfo.LDAC_QUALITY);
            textView1.setText("LDAC");
        }else if(codec == Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_APTX_HD)){
            progressBar.setProgress(ProgressBarInfo.APTXHD_QUALITY);
            textView1.setText("aptX HD");
        }else if(codec == Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_APTX)){
            progressBar.setProgress(ProgressBarInfo.APTX_QUALITY);
            textView1.setText("aptX");
        }else if(codec == Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_AAC)){
            progressBar.setProgress(ProgressBarInfo.AAC_QUALITY);
            textView1.setText("AAC");
        }else if(codec == Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_SBC)){
            progressBar.setProgress(ProgressBarInfo.SBC_QUALITY);
            textView1.setText("SBC");
        }else{
            progressBar.setProgress(0);
            textView1.setText("None");
        }
        if(progressBar.getProgress() >= ProgressBarInfo.GREEN_BORDER){
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            textView4.setText("Quality: Very Good");
        }else if(progressBar.getProgress() >= ProgressBarInfo.YELLOW_BORDER){
            textView4.setText("Quality: Good");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        }else if(progressBar.getProgress() >= ProgressBarInfo.RED_BORDER){
            textView4.setText("Quality: Weak");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
        switch (codecController.getSAMPLE_RATE()){
            case Codec.SAMPLE_RATE_44100:
                textView2.setText("Sample Rate: 44100 Hz");
                break;
            case Codec.SAMPLE_RATE_48000:
                textView2.setText("Sample Rate: 48000 Hz");
                break;
            case Codec.SAMPLE_RATE_88200:
                textView2.setText("Sample Rate: 88200 Hz");
                break;
            case Codec.SAMPLE_RATE_96000:
                textView2.setText("Sample Rate: 96000 Hz");
                break;
            case Codec.SAMPLE_RATE_176400:
                textView2.setText("Sample Rate: 176400 Hz");
                break;
            case Codec.SAMPLE_RATE_192000:
                textView2.setText("Sample Rate: 192000 Hz");
                break;
            default:
                textView2.setText("Sample Rate: None");
        }
        switch (codecController.getBITS_PER_SAMPLE()){
            case Codec.BITS_PER_SAMPLE_16:
                textView3.setText("Bits Per Sample: 16");
                break;
            case Codec.BITS_PER_SAMPLE_24:
                textView3.setText("Bits Per Sample: 24");
                break;
            case Codec.BITS_PER_SAMPLE_32:
                textView3.setText("Bits Per Sample: 32");
                break;
            default:
                textView3.setText("Bits Per Sample: None");
        }
        if(codec == Codec.getCodecType(MAX_SOURCE_CODEC_TYPE)){
            textView1.setForegroundTintList(ColorStateList.valueOf(Color.GREEN));
            button2.setEnabled(false);
        }else{
            textView1.setForegroundTintList(ColorStateList.valueOf(Color.BLACK));
            button2.setEnabled(true);
        }
    }

    private void getPossibleCodecs(){
        List<String> localCodecs = codecController.getLocalCodecs();
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(localCodecs);
        List<String> LocalCodecs = new ArrayList<>(hashSet);
        fragment.setLocalCodecs(LocalCodecs);

        List<String> selectableCodecs = codecController.getSelectableCodecs();
        LinkedHashSet<String> hashSet2 = new LinkedHashSet<>(selectableCodecs);
        List<String> SelectableCodecs = new ArrayList<>(hashSet2);
        setMAX_SOURCE_CODEC_TYPE(SelectableCodecs);
        fragment.setLocalCodecs(SelectableCodecs);
    }

    private void setMAX_SOURCE_CODEC_TYPE(List<String> list){
        if(list.contains(Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_LDAC))){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_LDAC;
        }else if(list.contains(Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_APTX_HD))){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_APTX_HD;
        }else if(list.contains(Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_APTX))){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_APTX;
        }else if(list.contains(Codec.getCodecType(Codec.SOURCE_CODEC_TYPE_AAC))){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_AAC;
        }else{
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_SBC;
        }
    }

    private void getAllInfo(){
        getPossibleCodecs();
        getCurrentCodec();
    }

    private void setDefaultText(){
        textView1.setText("None");
        textView2.setText("Sample Rate: None");
        textView3.setText("Bits Per Sample: None");
        textView4.setText("Quality: None");
        fragment.setDefaultText();
        button2.setEnabled(false);
    }


    @Override
    public void onBluetoothConnected() {
        initiateBluetoothAdapter();
    }

    @Override
    public void onBluetoothDisconnected() {
        setDefaultText();
    }

}
