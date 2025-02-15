package com.zohocontacts.dataquerybuilder.querybuilder.datahelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zohocontacts.dbpojo.tabledesign.Table;
import com.zohocontacts.dbpojo.tabledesign.TableWithChild;

public class GenericPojoMapper {

	ResultSet result;

	ArrayList<String> columnNames;

	public ArrayList<Table> PojoResultSetter(Table selectTable, ArrayList<String> columnNames, ResultSet result)
			throws SQLException {
		ArrayList<Table> data = new ArrayList<>();
		HashMap<Long, Table> uniqueList = new HashMap<Long, Table>();

		this.result = result;
		this.columnNames = columnNames;
		List<Table> childTables = new ArrayList<>();

		if (selectTable instanceof TableWithChild) {

			TableWithChild parentTable = (TableWithChild) selectTable;
			for (Table child : parentTable.getChildTables()) {
				Boolean isExist = false;
				for (Table tables : childTables) {

					if (child.getTableName().equals(tables.getTableName())) {
						isExist = true;
						break;
					}
				}

				if (!isExist) {

					childTables.add(child);
				}

			}
		}
		Table table;
		try {
			while (this.result.next()) {

				table = mapTableData(selectTable);
				if (uniqueList.get(table.getID()) != null) {
					table = uniqueList.get(table.getID());

				} else {
					uniqueList.put(table.getID(), table);
					data.add(table);

				}
				if (table instanceof TableWithChild) {
					TableWithChild parentTable = (TableWithChild) table;
					for (Table childTable : childTables) {
						parentTable.setChildTable(mapTableData(childTable));
					}

				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			uniqueList.clear();

		}

		return data;
	}

	private Table mapTableData(Table table) {
		Map<String, Object> row = new HashMap<String, Object>();

		for (String columnName : table.getTableColumnNames()) {

			if (getObject(table.getTableName() + "." + columnName) != null) {

				if (getObject(table.getTableName() + "." + columnName) instanceof Integer) {

					Integer intValue = (Integer) getObject(table.getTableName() + "." + columnName);
					long longValue = intValue.longValue();
					row.put(columnName, longValue);

				} else {
					row.put(columnName, getObject(table.getTableName() + "." + columnName));

				}
			}

		}

		return table.getNewTable(row);
	}

	private Object getObject(String column) {
		try {
			if (this.columnNames.contains(column)) {

				return result.getObject(column);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}
}
