package com.tonmx.exmark.activity;

import java.util.ArrayList;
import java.util.List;

import com.tonmx.exmark.adapter.FragAdapter;
import com.tonmx.exmark.fragment.EXCertificateFragment;
import com.tonmx.exmark.fragment.EXMarkFragment;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

public class MainActivity extends FragmentActivity {
    private ViewPager vp;
    private EXCertificateFragment certificateFragment;
    private EXMarkFragment markFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		certificateFragment = new EXCertificateFragment();
		markFragment = new EXMarkFragment();
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(markFragment);
		fragments.add(certificateFragment);
		FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
		
		//设定适配器  
        vp = (ViewPager)findViewById(R.id.viewpager);  
        vp.setAdapter(adapter);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(vp.getCurrentItem() == 1){
		switch(keyCode){
		case KeyEvent.KEYCODE_DPAD_UP:
			Log.d("Chunna.zheng", "Activity===up按键");
			certificateFragment.zoomIn();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			Log.d("Chunna.zheng", "Activity===down按键");
			certificateFragment.zoomOut();
			break;
		}
		}
		return super.onKeyUp(keyCode, event);
	}

}
