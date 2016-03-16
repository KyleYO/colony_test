package com.colonycount.cklab.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImgSearchFilter {
	private List<Calendar> dateRange;
	private Set<String> colonyType;
	private List<Integer> dilutionNumber;
	private Set<String> expParam;
	private String defaultOptionAll;
	
	public ImgSearchFilter(String defaultOptionAll){
		dateRange = new ArrayList<Calendar>();
		colonyType = new HashSet<String>();
		dilutionNumber = new ArrayList<Integer>();
		expParam = new HashSet<String>();
		
		// set default date range
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		dateRange.add(c1);
		dateRange.add(c2);
		
		// set default dilution number
		int i1 = -1;
		int i2 = -10;
		dilutionNumber.add(i1);
		dilutionNumber.add(i2);

		this.defaultOptionAll = defaultOptionAll;
		// set default type
		colonyType.add(defaultOptionAll);
		// set default param
		expParam.add(defaultOptionAll);
	}
	
	public void setDateRange(Calendar c1, Calendar c2){
		dateRange.set(0, c1);
		dateRange.set(1, c2);
	}
	
	public void setDilutionNumberRange(int i1, int i2){
		dilutionNumber.set(0, i1);
		dilutionNumber.set(1, i2);
	}
	
	public List<Date> getDateRange(){
		List<Date> temp = new ArrayList<Date>();
		for(Calendar c : dateRange){
			temp.add(c.getTime());
		}
		
		return temp;
	}
	
	public List<Integer> getDilutionNumberRange(){
		return dilutionNumber;
	}
	
	public String getDateRangeString(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String date1 = format.format(dateRange.get(0).getTime());
		String date2 = format.format(dateRange.get(1).getTime());
		
		return String.format("%s - %s", date1, date2); 
	}
	
	public Set<String> getColonyType(){
		return colonyType;
	}
	
	public Set<String> getExpParam(){
		return expParam;
	}
	
	public void addColonyType(String value){
		if(value.equals(defaultOptionAll))
			colonyType.clear();
		else 
			colonyType.remove(defaultOptionAll);
			
		colonyType.add(value);
	}
	
	public void removeColonyType(String value){
		colonyType.remove(value);
	}
	
	public void addExpParam(String value){
		if(value.equals(defaultOptionAll))
			expParam.clear();
		else
			expParam.remove(defaultOptionAll);
		
		expParam.add(value);
	}
	
	public void removeExpParam(String value){
		expParam.remove(value);
	}
}