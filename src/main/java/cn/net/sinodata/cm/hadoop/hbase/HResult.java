package cn.net.sinodata.cm.hadoop.hbase;

/**
 * HBase result object
 * @author manan
 *
 */
public class HResult {

	private String key;
	private byte[] value;
	private long timestamp;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
