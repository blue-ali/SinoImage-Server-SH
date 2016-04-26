package cn.net.sinodata.cm.hadoop.hbase;

import java.util.HashMap;
import java.util.Map;

public class HData {

	private String rowkey;
	private String columnFamily;
	private Map<String, byte[]> datas = new HashMap<String, byte[]>();
	
	public String getRowkey() {
		return rowkey;
	}
	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	public String getColumnFamily() {
		return columnFamily;
	}
	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}
	public Map<String, byte[]> getDatas() {
		return datas;
	}
//	public void setDatas(Map<String, byte[]> datas) {
//		this.datas = datas;
//	}
	public void addKV(String key, byte[] value){
		datas.put(key, value);
	}
}
