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

	List<String> columnNames;

	public static List<Table> PojoResultSetter(Table selectTable, List<String> columnNames, ResultSet result)
			throws SQLException {
		List<Table> data = new ArrayList<>();
		Map<Long, Table> uniqueList = new HashMap<Long, Table>();
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
			while (result.next()) {

				table = mapTableData(selectTable, result, columnNames);
				if (uniqueList.get(table.getID()) != null) {
					table = uniqueList.get(table.getID());

				} else {
					uniqueList.put(table.getID(), table);
					data.add(table);

				}
				if (table instanceof TableWithChild) {
					TableWithChild parentTable = (TableWithChild) table;
					for (Table childTable : childTables) {
						parentTable.setChildTable(mapTableData(childTable, result, columnNames));
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

	private static Table mapTableData(Table table, ResultSet result, List<String> columnNames) {
		Map<String, Object> row = new HashMap<String, Object>();

		for (String columnName : table.getTableColumnNames()) {

			if (getObject(table.getTableName() + "." + columnName, result, columnNames) != null) {

				if (getObject(table.getTableName() + "." + columnName, result, columnNames) instanceof Integer) {

					Integer intValue = (Integer) getObject(table.getTableName() + "." + columnName, result,
							columnNames);
					long longValue = intValue.longValue();
					row.put(columnName, longValue);

				} else {
					row.put(columnName, getObject(table.getTableName() + "." + columnName, result, columnNames));

				}
			}

		}

		return table.getNewTable(row);
	}

	private static Object getObject(String column, ResultSet result, List<String> columnNames) {
		try {
			if (columnNames.contains(column)) {

				return result.getObject(column);

			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return null;
	}
}
