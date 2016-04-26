package cn.net.sinodata.cm.hadoop.hbase.rowkey;

import org.apache.hadoop.hbase.util.Bytes;

public class PartitionRowKeyGenerator {
	
//	private static long partition = 22000;

	/**
	 * Generate rowkey with partition
	 * @param rowkey Original rowkey
	 * @param length Length of max partition, to format the head of rowkey by  "%0Xd"
	 * @param partition The number of regions
	 * @return
	 */
	public static byte[] generateRowKey(String rowkey, int length, int partition){
		long partitionId = Long.valueOf(rowkey) % partition;
		String format = "%0" + length + "d";
		String formatId = String.format(format, partitionId);
		return Bytes.add(Bytes.toBytes(formatId),
				Bytes.toBytes(rowkey));
	}
}
