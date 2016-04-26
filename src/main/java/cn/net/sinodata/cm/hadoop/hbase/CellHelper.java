package cn.net.sinodata.cm.hadoop.hbase;

import java.util.Arrays;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.util.Bytes;

public class CellHelper {
	
	public static String getCellValue(Cell cell){
		return Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
	}
	
	public static byte[] getCellByteValue(Cell cell){
        return Arrays.copyOfRange(cell.getValueArray(), cell.getValueOffset(), cell.getValueOffset() + cell.getValueLength());
//		return Bytes.toBytes(Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
	}
}

