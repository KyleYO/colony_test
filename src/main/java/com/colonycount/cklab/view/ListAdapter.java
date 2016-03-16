package com.colonycount.cklab.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.colonycount.cklab.activity.R;

public class ListAdapter extends BaseAdapter {
	
	private LayoutInflater myInflater;
	private List<Boolean> checkList = new ArrayList<Boolean>();
	private List<String> colonyTypeList = new ArrayList<String>();
	
	public ListAdapter(Context context){
		myInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return checkList.size();
	}

	@Override
	public Object getItem(int position) {
		return checkList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ListItem listItem;
		
		if(convertView == null){
            //取得listItem容器 view
            convertView = myInflater.inflate(R.layout.dialog_add_tag_listview_item, null);
            
            //建構listItem內容view
            CheckBox cbx = (CheckBox) convertView.findViewById(R.id.checkBox1);
            TextView text = (TextView) convertView.findViewById(R.id.textView1);
            Button delete = (Button) convertView.findViewById(R.id.button1);
            listItem = new ListItem(cbx, text, delete);
            
            //設置容器內容
            convertView.setTag(listItem);
        } else {
        	listItem = (ListItem) convertView.getTag();
        }
		
		listItem.cbx.setChecked(checkList.get(position));
		listItem.colonyType.setText(colonyTypeList.get(position));
		
		return convertView;
	}
	
	
	public void addItem(String colonyType){
		checkList.add(false);
		colonyTypeList.add(colonyType);
		notifyDataSetChanged();
	}
	
	class ListItem {
		CheckBox cbx;
		TextView colonyType;
        Button delete;
    
        public ListItem(CheckBox cbx, TextView colonyType, Button delete){
            this.cbx = cbx;
            this.colonyType = colonyType;
            this.delete = delete;
        }
    }
}