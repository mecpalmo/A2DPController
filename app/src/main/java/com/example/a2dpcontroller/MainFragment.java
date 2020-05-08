package com.example.a2dpcontroller;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements BluetoothBroadcastReceiver.Callback, BluetoothA2DPRequester.Callback{

    private static final String TAG = "MyApp";
    private BluetoothAdapter mAdapter;
    private CodecController codecController;
    private boolean NONE = true;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;

    private ProgressBar progressBar;
    private Button button1;
    private Button button2;
    private Button button3;

    private int MAX_SOURCE_CODEC_TYPE = Codec.SOURCE_CODEC_TYPE_SBC;
    private int CURRENT_CODEC_TYPE = -1;
    private List<Integer> listOfSelectableCodecs = new ArrayList<>();
    private Context mainContext;
    FragmentChanger fragmentChanger;
    Toast currentToast;

    public MainFragment(Context context, FragmentChanger fc) {
        mainContext = context;
        fragmentChanger = fc;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        codecController = new CodecController();
        setViewComponents(view);
        initiateBluetoothAdapter();
        return view;
    }

    public void initiateBluetoothAdapter(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.i("MYAPP", "got the adapter");
        if (mAdapter.isEnabled()) {
            new BluetoothA2DPRequester(this).request(mainContext, mAdapter);
            Log.i("MYAPP", "Requester Requested");
        }else {
            setDefaultText();
            /*if(mAdapter.enable()) {
                BluetoothBroadcastReceiver.register(this, mainContext);
            }else{
                setDefaultText();
                makeToast("Cannot Enable Bluetooth");
            }*/
        }
    }

    private void setViewComponents(View view){

        textView1 = (TextView)view.findViewById(R.id.textView1);
        textView2 = (TextView)view.findViewById(R.id.textView2);
        textView3 = (TextView)view.findViewById(R.id.textView3);
        textView4 = (TextView)view.findViewById(R.id.textView4);
        textView5 = (TextView)view.findViewById(R.id.textView5);
        textView6 = (TextView)view.findViewById(R.id.textView6);
        textView7 = (TextView)view.findViewById(R.id.textView7);
        textView8 = (TextView)view.findViewById(R.id.textView8);
        textView9 = (TextView)view.findViewById(R.id.textView9);
        button1 = (Button)view.findViewById(R.id.button1);
        button2 = (Button)view.findViewById(R.id.button2);
        button2.setEnabled(false);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDesiredCodec(MAX_SOURCE_CODEC_TYPE);
            }
        });
        button3 = (Button)view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomCodecList();
            }
        });
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setMax(ProgressBarInfo.MAX_VALUE_QUALITY);

    }

    private void launchCustomCodecList(){
        if(!NONE && listOfSelectableCodecs.size() > 1 && CURRENT_CODEC_TYPE >= Codec.SOURCE_CODEC_TYPE_SBC && CURRENT_CODEC_TYPE <= Codec.SOURCE_CODEC_TYPE_LDAC ){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(mainContext);
            List<String> stringList = new ArrayList<>();
            for(int item : listOfSelectableCodecs){
                stringList.add(Codec.getCodecName(item));
            }
            String[] stringArray = stringList.toArray(new String[0]);
            mBuilder.setTitle("Choose specific codec");
            mBuilder.setSingleChoiceItems(stringArray, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setDesiredCodec(listOfSelectableCodecs.get(i));
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
        if(one){
            makeToast("Codec changed successfully");
            getAllInfo();
        }else{
            makeToast("Failed to change codec");
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
        listOfSelectableCodecs = new ArrayList<>();
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_SBC) && local.contains(Codec.SOURCE_CODEC_TYPE_SBC)) {
            listOfSelectableCodecs.add(Codec.SOURCE_CODEC_TYPE_SBC);
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_AAC) && local.contains(Codec.SOURCE_CODEC_TYPE_AAC)){
            listOfSelectableCodecs.add(Codec.SOURCE_CODEC_TYPE_AAC);
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_APTX) && local.contains(Codec.SOURCE_CODEC_TYPE_APTX)){
            listOfSelectableCodecs.add(Codec.SOURCE_CODEC_TYPE_APTX);
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_APTX_HD) && local.contains(Codec.SOURCE_CODEC_TYPE_APTX_HD)){
            listOfSelectableCodecs.add(Codec.SOURCE_CODEC_TYPE_APTX_HD);
        }
        if(selectable.contains(Codec.SOURCE_CODEC_TYPE_LDAC) && local.contains(Codec.SOURCE_CODEC_TYPE_LDAC)){
            listOfSelectableCodecs.add(Codec.SOURCE_CODEC_TYPE_LDAC);
        }
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

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentChanger.changeFragment(codecController.getCodec());
                }
            });
            button1.setEnabled(true);

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
            setLocalCodecs(localCodecs);

            List<Integer> selectableCodecs = codecController.getSelectableCodecs();
            setMAX_SOURCE_CODEC_TYPE(selectableCodecs,localCodecs);
            setListOfSelectableCodecs(selectableCodecs,localCodecs);
            setSelectableCodecs(selectableCodecs);

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
            }else if(devicesName.size()==1){
                textView5.setText("Bluetooth Device: "+devicesName.get(0));
            }else{
                textView5.setText("Bluetooth Device: Unknown");
            }

        }else {
            setDefaultText();
        }
        return success;
    }

    public void setLocalCodecs(List<Integer> list) {
        String str = "";
        if (list.size() > 0) {
            for (int item : list) {
                if(item>= Codec.SOURCE_CODEC_TYPE_SBC && item<=Codec.SOURCE_CODEC_TYPE_LDAC) {
                    str = str + Codec.getCodecName(item) + ", ";
                }
            }
            str = str.substring(0, str.length() - 2);
        } else{
            str = "not found";
        }
        textView7.setText(str);
    }


    public void setSelectableCodecs(List<Integer> list){
        String str = "";
        if (list.size() > 0) {
            for (int item : list) {
                if(item>= Codec.SOURCE_CODEC_TYPE_SBC && item<=Codec.SOURCE_CODEC_TYPE_LDAC) {
                    str = str + Codec.getCodecName(item) + ", ";
                }
            }
            str = str.substring(0, str.length() - 2);
        } else{
            str = "not found";
        }
        textView9.setText(str);
    }

    private void setDefaultText(){
        NONE = true;
        CURRENT_CODEC_TYPE = -1;
        textView1.setText("None");
        textView2.setText("Sample Rate: None");
        textView3.setText("Bits Per Sample: None");
        textView4.setText("Quality: None");
        progressBar.setProgress(0);
        textView7.setText("None");
        textView9.setText("None");
        button1.setEnabled(false);
        button2.setEnabled(false);
        button2.setText("No device detected");
        textView5.setText("Bluetooth Device: None");
    }

    private void makeDefualtToast(){
        makeToast("Something went wrong");
    }

    private void makeToast(String str){
        //currentToast = new Toast(mainContext);
        //currentToast.makeText(mainContext, str, Toast.LENGTH_SHORT);
        //currentToast.show();
        Toast.makeText(mainContext, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothConnected() {
        initiateBluetoothAdapter();
    }

    @Override
    public void onBluetoothError() {
        setDefaultText();
        makeToast("Error has occured");
    }

    @Override
    public void onBluetoothDisconnected() {
        setDefaultText();
        makeToast("Device disconnected");
        BluetoothBroadcastReceiver.register(this, mainContext);
    }

    @Override
    public void onA2DPProxyReceived(BluetoothA2dp proxy){
        codecController.setA2dp(proxy);
        getAllInfo();
    }

    public static interface FragmentChanger{
        public void changeFragment(int codec);
    }
}
