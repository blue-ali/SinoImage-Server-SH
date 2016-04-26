package cn.net.sinodata.cm.hadoop.hbase.rowkey;

import org.apache.hadoop.hbase.util.Bytes;

public class PartitionRowKeyManager implements RowKeyGenerator,
		SplitKeysCalculator {

	public static final int DEFAULT_PARTITION_AMOUNT = 220;
	private long currentId = 1;
	private int partition = DEFAULT_PARTITION_AMOUNT;
	public void setPartition(int partition) {
		this.partition = partition;
	}
	
	public byte[] generateKey(String rowkey){
		long partitionId = Long.valueOf(rowkey) % partition;
		return Bytes.add(Bytes.toBytes(partitionId),
				Bytes.toBytes(rowkey));
	}

	public byte[] nextId() {
		try {
			long partitionId = currentId % partition;
			return Bytes.add(Bytes.toBytes(partitionId),
					Bytes.toBytes(currentId));
		} finally {
			currentId++;
		}
	}

	public byte[][] calcSplitKeys() {
		byte[][] splitKeys = new byte[partition - 1][];
		for(int i = 1; i < partition ; i ++) {
			splitKeys[i-1] = Bytes.toBytes((long)i);
		}
		return splitKeys;
	}
}
