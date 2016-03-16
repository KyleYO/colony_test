package com.colonycount.cklab.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class FragmentDialog extends DialogFragment {
	private String 			title;
	private String 			msg;
	private int    			iconId;
	private String			positiveBtnText;
	private String 			negativeBtnText;
	private OnClickListener positiveClickListener;
	private OnClickListener negativeClickListener;
	
	public FragmentDialog(String title, String msg, int iconId, String positiveBtnText, String negativeBtnText, OnClickListener positiveClickListener, OnClickListener negativeClickListener){
		this.title = title;
		this.msg = msg;
		this.iconId = iconId;
		this.positiveBtnText = positiveBtnText;
		this.negativeBtnText = negativeBtnText;
		this.positiveClickListener = positiveClickListener;
		this.negativeClickListener = negativeClickListener;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        
        if(iconId != 0)
        	builder.setIcon(iconId);
        
        if(positiveBtnText != null)
        	builder.setPositiveButton(positiveBtnText, positiveClickListener);
        
        if(negativeBtnText != null)
        	builder.setNegativeButton(negativeBtnText, negativeClickListener);
        
        return builder.create();
    }
}
