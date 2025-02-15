package com.zohocontacts.dataquerybuilder.querybuilder.datahelper;

import com.zohocontacts.dbpojo.tabledesign.Table;

public class changedStateContainer {

	private Table newData;
	private Table oldData;

	public void setOldData(Table oldData) {

		this.oldData = oldData;
	}
	

	public Table getOldData() {

		return this.oldData;
	}

	public void setNewData(Table newData) {

		this.newData = newData;
	}

	public Table getnewData() {

		return this.newData;
	}

}
