package cn.net.sinodata.cm.hadoop.hbase;

public class HQuery {
	
	private String tableName;
	private String columFamily;
	private String columnName;
	private String rowkey;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumFamily() {
		return columFamily;
	}
	public void setColumFamily(String columFamily) {
		this.columFamily = columFamily;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getRowkey() {
		return rowkey;
	}
	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	
}
