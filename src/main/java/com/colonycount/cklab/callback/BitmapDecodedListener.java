package com.colonycount.cklab.callback;

import android.graphics.Bitmap;

public interface BitmapDecodedListener {
	void onDecoded(Bitmap bitmap, String url);
}
