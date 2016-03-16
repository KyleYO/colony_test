package com.colonycount.cklab.model;

import java.io.Serializable;
import java.util.List;

import edu.ntu.esoe.cklab.colonycountcore.Components.DisplayColony;

public class DataWrapper implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<DisplayColony> parliaments;

   public DataWrapper(List<DisplayColony> data) {
      this.parliaments = data;
   }

   public List<DisplayColony> getParliaments() {
      return this.parliaments;
   }
}
