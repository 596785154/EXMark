package com.tonmx.exmark.fragment;

import com.tonmx.exmark.activity.R;
import com.tonmx.exmark.view.ZoomImageView;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;


public class EXCertificateFragment extends Fragment{
	ZoomImageView zoomImage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_excertificate, container, false);
		//zoomImage = (ZoomImageView) view.findViewById(R.id.zoom_image);
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	}
	public void zoomIn(){
		//zoomImage.zoomIn();
	}
	public void zoomOut(){
		//zoomImage.zoomOut();
	}

}
