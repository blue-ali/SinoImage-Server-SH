package cn.net.sinodata.cm.hadoop.hbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Generate split file and create pre-split table of HBase
 * @author manan
 *
 */
public class HBaseTableHelper {
	
	public static void main(String[] args) {
		HBaseTableHelper t = new HBaseTableHelper();
		try {
//			t.generatekSplitFile("split-key", 2200);
			t.createTable("tb_image1", "F", 3, "split-key");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create table with pre-split rowkey file
	 * @param sTableName name of table
	 * @param columFamily name of columnFamily
	 * @param maxVersion the max version of table
	 * @param statisticsFile pre-split rowkey file
	 */
	public void createTable(String sTableName, String columFamily, int maxVersion, String statisticsFile)
			throws MasterNotRunningException, ZooKeeperConnectionException,
			IOException {
		String[] splitKeysString = genSplitKeysFromFile(statisticsFile);
		byte[][] splitKeys = Bytes.toByteArrays(splitKeysString);
		TableName tableName = TableName.valueOf(sTableName);
		HTableDescriptor htd = new HTableDescriptor(tableName);
		HColumnDescriptor hcd = new HColumnDescriptor(columFamily);
		hcd.setCompressionType(Compression.Algorithm.SNAPPY);
		hcd.setBloomFilterType(BloomType.ROW);
		hcd.setMaxVersions(maxVersion);
		htd.addFamily(hcd);

		HBaseAdmin adm = HBaseUtil.getHBaseAdmin();
		if (adm.tableExists(tableName)) {
			System.out.println("---table " + tableName + " exists");
			if (adm.isTableEnabled(tableName)) {
				adm.disableTable(tableName);
				System.out.println("---disabled table " + tableName );
			}
			adm.deleteTable(tableName);
			System.out.println("---deleted table " + tableName);
		}
		adm.createTable(htd, splitKeys);
		System.out.println("---created table " + tableName );
		if (adm != null) {
			adm.close();
		}
	}

	/**
	 * Generate split rowkey from file
	 * @param statisticsFile pre-split rowkey file
	 * @return
	 */
	private String[] genSplitKeysFromFile(String statisticsFile) {
		List<String> list = new ArrayList<String>();
		File f = new File(statisticsFile);
		System.out.println(f.getAbsolutePath());
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			while(true){
				String line = br.readLine();
				if(line != null){
					list.add(line);
				}else{
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fr!=null)
					fr.close();
				if(br!=null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	private static void generatekSplitFile(String path, int regionCount){
//		String[] zts = new String[]{"1","2"};
//		String p = HBaseUtil.class.getClassLoader().getResource(".").getPath();
		File file = new File(path);
		System.out.println(file.getAbsolutePath());
		FileWriter fw = null;
		int d =String.valueOf(regionCount).length();
		try{
			fw = new FileWriter(file);
			for (int i = 0; i < regionCount; i++) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%0" + d + "d", i+1));
					fw.append(sb.toString());
					if(i+1<regionCount)
						fw.append("\n");
			}
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
