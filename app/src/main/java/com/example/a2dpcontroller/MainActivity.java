package com.example.a2dpcontroller;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements BluetoothBroadcastReceiver.Callback, BluetoothA2DPRequester.Callback {

    private static final String TAG = "MyApp";
    private BluetoothAdapter mAdapter;
    private CodecController codecController;
    private boolean NONE = true;

    private fragment1 fragment;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private ProgressBar progressBar;
    private Button button1;
    private Button button2;
    private Button button3;
    private SwipeRefreshLayout mSwipeRefresh;

    private int MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_SBC;
    private int CURRENT_CODEC_TYPE = -1;
    private String[] listOfSelectableCodecs = {Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_SBC)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        codecController = new CodecController(this);
        setViewComponents();
        setDefaultText();

        initiateBluetoothAdapter();
    }

    public void initiateBluetoothAdapter(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter.isEnabled()) {
            new BluetoothA2DPRequester(this).request(this, mAdapter);
        }else{
            if(mAdapter.enable()) {
                BluetoothBroadcastReceiver.register(this, this);
            }else{
                setDefaultText();
                Toast.makeText(this, "Cannot Enable Bluetooth", Toast.LENGTH_SHORT);
            }
        }
    }

    private void setViewComponents(){
        fragment = (fragment1)getSupportFragmentManager().findFragmentById(R.id.fragment);
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button2.setEnabled(false);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDesiredCodec(MAX_SOURCE_CODEC_TYPE);
            }
        });
        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomCodecList();
            }
        });
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(ProgressBarInfo.MAX_VALUE_QUALITY);
        mSwipeRefresh = findViewById(R.id.swiperefresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateBluetoothAdapter();
            }
        });
    }

    private void launchCustomCodecList(){
        if(!NONE && listOfSelectableCodecs.length > 1 && CURRENT_CODEC_TYPE >= 0){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Choose specific codec");
            mBuilder.setSingleChoiceItems(listOfSelectableCodecs, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(listOfSelectableCodecs[i]==Codec.getCodecName(CURRENT_CODEC_TYPE)){

                    }else if(listOfSelectableCodecs[i]==Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_SBC)){
                        setDesiredCodec(Codec.SOURCE_CODEC_TYPE_SBC);
                    }else if(listOfSelectableCodecs[i]==Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_AAC)){
                        setDesiredCodec(Codec.SOURCE_CODEC_TYPE_AAC);
                    }else if(listOfSelectableCodecs[i]==Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_APTX)){
                        setDesiredCodec(Codec.SOURCE_CODEC_TYPE_APTX);
                    }else if(listOfSelectableCodecs[i]==Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_APTX_HD)){
                        setDesiredCodec(Codec.SOURCE_CODEC_TYPE_APTX_HD);
                    }else if(listOfSelectableCodecs[i]==Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_LDAC)){
                        setDesiredCodec(Codec.SOURCE_CODEC_TYPE_LDAC);
                    }else{
                        makeDefualtToast();
                    }
                    dialogInterface.dismiss();
                }
            });
            mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }
    }

    private void setDesiredCodec(int codec_type){
        boolean one = codecController.setCodec(codec_type);
        boolean two = getAllInfo();
        if(one && two){
            Toast.makeText(this, "Codec changed success",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Failed to change codec",Toast.LENGTH_SHORT).show();
        }
    }

    private void setMAX_SOURCE_CODEC_TYPE(List<Integer> selectable, List<Integer> local){
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_LDAC) && local.contains(Codec.SOURCE_CODEC_TYPE_LDAC)){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_LDAC;
        }else if(selectable.contains(Codec.SOURCE_CODEC_TYPE_APTX_HD) && local.contains(Codec.SOURCE_CODEC_TYPE_APTX_HD)){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_APTX_HD;
        }else if(selectable.contains(Codec.SOURCE_CODEC_TYPE_APTX) && local.contains(Codec.SOURCE_CODEC_TYPE_APTX)){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_APTX;
        }else if(selectable.contains(Codec.SOURCE_CODEC_TYPE_AAC) && local.contains(Codec.SOURCE_CODEC_TYPE_AAC)){
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_AAC;
        }else{
            MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_SBC;
        }
    }

    private void setListOfSelectableCodecs(List<Integer> selectable, List<Integer> local){
        List<String> stringList = new ArrayList<String>();
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_LDAC) && local.contains(Codec.SOURCE_CODEC_TYPE_LDAC)) {
            stringList.add(Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_LDAC));
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_APTX_HD) && local.contains(Codec.SOURCE_CODEC_TYPE_APTX_HD)){
            stringList.add(Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_APTX_HD));
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_APTX) && local.contains(Codec.SOURCE_CODEC_TYPE_APTX)){
            stringList.add(Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_APTX));
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_AAC) && local.contains(Codec.SOURCE_CODEC_TYPE_AAC)){
            stringList.add(Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_AAC));
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_SBC) && local.contains(Codec.SOURCE_CODEC_TYPE_SBC)){
            stringList.add(Codec.getCodecName(Codec.SOURCE_CODEC_TYPE_SBC));
        }
        listOfSelectableCodecs = (String[]) stringList.toArray();
    }

    private boolean getAllInfo(){
        boolean success = codecController.getCurrentCodec();

        if(success) {
            NONE = false;
            textView1.setText(Codec.getCodecName(codecController.getCodec()));
            CURRENT_CODEC_TYPE = codecController.getCodec();
            progressBar.setProgress(ProgressBarInfo.getProgress(codecController.getCodec()));
            textView4.setText(ProgressBarInfo.getText(progressBar.getProgress()));
            progressBar.setProgressTintList(ColorStateList.valueOf(ProgressBarInfo.getColor(progressBar.getProgress())));

            switch (codecController.getSAMPLE_RATE()) {
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
            switch (codecController.getBITS_PER_SAMPLE()) {
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

            List<Integer> localCodecs = codecController.getLocalCodecs();
            fragment.setLocalCodecs(localCodecs);

            List<Integer> selectableCodecs = codecController.getSelectableCodecs();
            setMAX_SOURCE_CODEC_TYPE(selectableCodecs,localCodecs);
            setListOfSelectableCodecs(selectableCodecs,localCodecs);
            fragment.setSelectableCodecs(selectableCodecs);

            if (codecController.getCodec() == MAX_SOURCE_CODEC_TYPE) {
                //textView1.setForegroundTintList(ColorStateList.valueOf(Color.GREEN));
                button2.setEnabled(false);
                button2.setText("Using Maximum Quality");
            } else {
                //textView1.setForegroundTintList(ColorStateList.valueOf(Color.BLACK));
                button2.setEnabled(true);
                button2.setText("Increase Codec Quality");
            }

            List<String> devicesName = codecController.getDevicesNames();
            if(devicesName.size()>1){
                setDefaultText();
                textView5.setText("More then 1 device connected");
                button2.setText("Unable to use A2DP");
            }else{
                textView5.setText("Bluetooth Device: "+devicesName.get(0));
            }

        }else {
            setDefaultText();
        }
        return success;
    }

    private void setDefaultText(){
        NONE = true;
        CURRENT_CODEC_TYPE = -1;
        textView1.setText("None");
        textView2.setText("Sample Rate: None");
        textView3.setText("Bits Per Sample: None");
        textView4.setText("Quality: None");
        progressBar.setProgress(0);
        fragment.setDefaultText();
        button2.setEnabled(false);
        button2.setText("No device detected");
        textView5.setText("Bluetooth Device: None");
    }

    private void makeDefualtToast(){
        Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothConnected() {
        initiateBluetoothAdapter();
    }

    @Override
    public void onBluetoothError() {
        setDefaultText();
        Toast.makeText(this,"Error has occured",Toast.LENGTH_SHORT);
    }

    @Override
    public void onBluetoothDisconnected() {
        setDefaultText();
        Toast.makeText(this, "Device disconnected", Toast.LENGTH_SHORT);
        BluetoothBroadcastReceiver.register(this,this);
    }

    @Override
    public void onA2DPProxyReceived(BluetoothA2dp proxy){
        codecController.setA2dp(proxy);
        getAllInfo();
    }

}
