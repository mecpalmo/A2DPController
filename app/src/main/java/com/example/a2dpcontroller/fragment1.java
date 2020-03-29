package com.example.a2dpcontroller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class fragment1 extends Fragment {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment1, container, false);
        textView1 = (TextView)view.findViewById(R.id.textView1);
        textView2 = (TextView)view.findViewById(R.id.textView2);
        textView3 = (TextView)view.findViewById(R.id.textView3);
        textView4 = (TextView)view.findViewById(R.id.textView4);
        return view;
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
        textView2.setText(str);
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
        textView4.setText(str);
    }

    public void setDefaultText(){
        textView2.setText("None");
        textView4.setText("None");
    }
}
