package com.tonmx.exmark.fragment;

import com.tonmx.exmark.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EXMarkFragment extends Fragment{
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	 View view = inflater.inflate(R.layout.fragment_exmark,container,false);
    	 
    	return view;
    }
}
