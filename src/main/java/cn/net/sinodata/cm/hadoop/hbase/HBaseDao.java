package cn.net.sinodata.cm.hadoop.hbase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import cn.net.sinodata.cm.hadoop.hbase.rowkey.PartitionRowKeyGenerator;
//import cn.net.sinodata.sinoimage.base.FileUtil;

/**
 * HBase data access operation
 * 
 * @author manan
 *
 */
public class HBaseDao {

	private static Configuration conf = HBaseUtil.getConfiguration();
	private static final Logger log = Logger.getLogger(HBaseDao.class);
	private static HConnection conn = null;

	static {
		conf = HBaseUtil.getConfiguration();
		try {
			conn = HConnectionManager.createConnection(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteTable(String tabName) {
		if (tabName != null && tabName.length() > 0) {
			try {
				HBaseAdmin admin = new HBaseAdmin(conf);
				admin.disableTable(tabName);
				admin.deleteTable(tabName);
				System.out.println("表【" + tabName + "】删除成功");
			} catch (IOException e) {
				System.out.println("表删除失败：【" + tabName + "】 删除失败");
			}
		} else {
			System.out.println("表删除失败：表名【" + tabName + "】不存在");
			return;
		}
	}

	/**
	 * query record by rowkey
	 * 
	 * @param hquery
	 *            query object for hbase
	 * @return
	 * @throws IOException
	 */
	public HResult queryByRowkey(HQuery hquery) throws IOException {
		return queryByRowkey(hquery, false).get(0);
	}

	/**
	 * query all version of record by rowkey
	 * 
	 * @param hquery
	 *            query object for hbase
	 * @param allVersion
	 * @return
	 * @throws IOException
	 */
	public List<HResult> queryByRowkey(HQuery hquery, boolean allVersion) throws IOException {
		List<HResult> results = new ArrayList<HResult>();
		HTableInterface table = null;

		table = conn.getTable(hquery.getTableName());
		//byte[] rowkey = PartitionRowKeyGenerator.generateRowKey(hquery.getRowkey(), 4, 2200);
		byte[] rowkey = Bytes.toBytes(hquery.getRowkey());
		log.debug("rowkey: " + new String(rowkey));
		Get get = new Get(rowkey);
		if (allVersion) {
			get.setMaxVersions();
		}
		get.addColumn(Bytes.toBytes(hquery.getColumFamily()), Bytes.toBytes(hquery.getColumnName()));
		Result result = table.get(get);
		List<Cell> cells = result.listCells();
		for (Cell cell : cells) {
			HResult hResult = new HResult();
			// hdata.setKey(cell.get);
			hResult.setValue(CellHelper.getCellByteValue(cell));
			hResult.setTimestamp(cell.getTimestamp());
			results.add(hResult);
		}
		return results;
	}

	/**
	 * insert or update record by a record list
	 * 
	 * @param tableName
	 * @param hdatas
	 * @throws IOException
	 */
	public void upsert(String tableName, List<HData> hdatas) throws IOException {
		long start = System.currentTimeMillis();
		HTableInterface table = null;
		table = conn.getTable(tableName);
		table.setAutoFlushTo(false);
		table.setWriteBufferSize(10 * 1024 * 1024);
		// log.debug("table cost: " + (System.currentTimeMillis() - start));
		List<Put> puts = new ArrayList<Put>();

		for (HData hdata : hdatas) {
			Map<String, byte[]> datas = hdata.getDatas();
			Set<Entry<String, byte[]>> entrySet = datas.entrySet();
			//TODO 閰嶇疆鏂囦欢 => 鍙橀噺
//			byte[] rowkey = PartitionRowKeyGenerator.generateRowKey(hdata.getRowkey(), 4, 2200);
			byte[] rowkey = Bytes.toBytes(hdata.getRowkey());
			for (Entry<String, byte[]> entry : entrySet) {
				Put put = new Put(rowkey);
				put.add(Bytes.toBytes(hdata.getColumnFamily()), Bytes.toBytes(entry.getKey()), entry.getValue());
				puts.add(put);
			}
		}
		table.put(puts);
		table.flushCommits();
		log.debug("cost: " + (System.currentTimeMillis() - start));

	}

	public void upsert(String tableName, HData hdata) throws IOException {
		long start = System.currentTimeMillis();
		HTableInterface table = null;
		table = conn.getTable(tableName);
		// table.setAutoFlushTo(false);
		// table.setWriteBufferSize(10 * 1024 * 1024);
		List<Put> puts = new ArrayList<Put>();
		Map<String, byte[]> datas = hdata.getDatas();
		Set<Entry<String, byte[]>> entrySet = datas.entrySet();
		//TODO 閰嶇疆鏂囦欢 => 鍙橀噺
//		byte[] rowkey = PartitionRowKeyGenerator.generateRowKey(hdata.getRowkey(), 4, 2200);
		byte[] rowkey = Bytes.toBytes(hdata.getRowkey());
		log.debug("rowkey: " + new String(rowkey));
		for (Entry<String, byte[]> entry : entrySet) {
			Put put = new Put(rowkey);
			put.add(Bytes.toBytes(hdata.getColumnFamily()), Bytes.toBytes(entry.getKey()), entry.getValue());
			puts.add(put);
		}
		table.put(puts);
		log.debug("cost: " + (System.currentTimeMillis() - start));
	}

	public static void main(String[] args) {
		List<HData> hdatas = new ArrayList<HData>();
		int batch = 500;
		int count = 5000;
		long start = System.currentTimeMillis();
		HBaseDao dao = new HBaseDao();
		File file = new File("e:\\111.jpg");
		try {
			//byte[] b = FileUtil.file2byte(file);
			//for (int i = 0; i < count; i++) {
				HData hdata = new HData();
				hdata.setColumnFamily("F");
				hdata.setRowkey(String.valueOf("188888"));
				// hdata.addKV("content", Bytes.toBytes("111"));
				hdata.addKV("content", Bytes.toBytes("111"));
				hdata.addKV("xml", Bytes.toBytes("<111></111>"));
				hdatas.add(hdata);
				//if (i != 0 && i % batch == 0) {
					//System.out.println("i: " + i);
					long upstart = System.currentTimeMillis();
					dao.upsert("tb_image", hdatas);
					hdatas.clear();
					System.out.println("upsert cost: " + (System.currentTimeMillis() - upstart));
				//}
			//}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("all cost: " + (System.currentTimeMillis() - start) / 1000);
	}
}
