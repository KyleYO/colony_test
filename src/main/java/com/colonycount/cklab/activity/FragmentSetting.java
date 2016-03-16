package com.colonycount.cklab.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentSetting extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_fragment_setting, container, false);


		//Set cklab logo
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int ScreenWidth = dm.widthPixels;   //螢幕的寬
		//int ScreenHeight = dm.heightPixels;  //螢幕的高
		ScreenWidth = 2 * ScreenWidth / 5 ; //調整成2/5倍螢幕寬
		ImageView logo_cklab = (ImageView)view.findViewById(R.id.logo_cklab);
		setResizwLogo(logo_cklab, ScreenWidth , ScreenWidth);
		ScreenWidth = ScreenWidth / 3;

		ImageView logo_opencv = (ImageView)view.findViewById(R.id.logo_opencv);
		setResizwLogo(logo_opencv, ScreenWidth, ScreenWidth);
		ImageView logo_github = (ImageView)view.findViewById(R.id.logo_github);
		setResizwLogo(logo_github, ScreenWidth , ScreenWidth);
		ImageView logo_google = (ImageView)view.findViewById(R.id.logo_google);
		setResizwLogo(logo_google, ScreenWidth , ScreenWidth);


		//Click title to open/close the content message
		TextView opencv_title = (TextView)view.findViewById(R.id.opencv_title);
		final TextView opencv_msg = (TextView)view.findViewById(R.id.opencv_msg);
		opencv_title.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (opencv_msg.getVisibility() == View.VISIBLE)
					opencv_msg.setVisibility(View.GONE);
				else
					opencv_msg.setVisibility(View.VISIBLE);
			}
		});
		logo_opencv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (opencv_msg.getVisibility() == View.VISIBLE)
					opencv_msg.setVisibility(View.GONE);
				else
					opencv_msg.setVisibility(View.VISIBLE);
			}
		});
		TextView timesquare_title = (TextView)view.findViewById(R.id.timesquare_title);
		final TextView timesquare_msg = (TextView)view.findViewById(R.id.timesquare_msg);
		timesquare_title.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (timesquare_msg.getVisibility() == View.VISIBLE)
					timesquare_msg.setVisibility(View.GONE);
				else
					timesquare_msg.setVisibility(View.VISIBLE);
			}
		});
		logo_github.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (timesquare_msg.getVisibility() == View.VISIBLE)
					timesquare_msg.setVisibility(View.GONE);
				else
					timesquare_msg.setVisibility(View.VISIBLE);
			}
		});
		TextView google_title = (TextView)view.findViewById(R.id.google_title);
		final TextView google_msg = (TextView)view.findViewById(R.id.google_msg);
		google_title.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (google_msg.getVisibility() == View.VISIBLE)
					google_msg.setVisibility(View.GONE);
				else
					google_msg.setVisibility(View.VISIBLE);
			}
		});
		logo_google.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (google_msg.getVisibility() == View.VISIBLE)
					google_msg.setVisibility(View.GONE);
				else
					google_msg.setVisibility(View.VISIBLE);
			}
		});

		//Get app version
		PackageInfo pinfo = null;
		try {
			pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		String versionName = pinfo.versionName;
		TextView version_name = (TextView)view.findViewById(R.id.version_name);
		version_name.setText("[NTU CKLAB] 版權所有 \nVersion : " + versionName + "\n\n-----Special thanks-----");

		version_name.setTextColor(this.getResources().getColor(R.color.license_dark_gray));
		opencv_title.setTextColor(this.getResources().getColor(R.color.license_gray));
		opencv_msg.setTextColor(this.getResources().getColor(R.color.license_gray));
		timesquare_title.setTextColor(this.getResources().getColor(R.color.license_gray));
		timesquare_msg.setTextColor(this.getResources().getColor(R.color.license_gray));
		google_title.setTextColor(this.getResources().getColor(R.color.license_gray));
		google_msg.setTextColor(this.getResources().getColor(R.color.license_gray));
/*
		opencv_title.setBackgroundColor(Color.BLUE);
		timesquare_title.setBackgroundColor(Color.YELLOW);
		google_title.setBackgroundColor(Color.GREEN);
		logo_google.setBackgroundColor(Color.GRAY);
		logo_opencv.setBackgroundColor(Color.RED);
		logo_github.setBackgroundColor(Color.BLUE);
		version_name.setBackgroundColor(Color.WHITE);*/
		return view;
	}

	private void setResizwLogo(ImageView logo_image, int evenWidth, int evenHight) {
		// TODO 自動產生的方法 Stub
		ViewGroup.LayoutParams params = logo_image.getLayoutParams();  //需import android.view.ViewGroup.LayoutParams;
		params.width = evenWidth;
		params.height = evenHight;
		logo_image.setLayoutParams(params);
	}


}