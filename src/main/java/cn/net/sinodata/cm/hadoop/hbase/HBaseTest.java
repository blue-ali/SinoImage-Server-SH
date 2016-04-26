package cn.net.sinodata.cm.hadoop.hbase;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.ColumnFamilySchema;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

//import cn.net.sinodata.sinoimage.base.FileUtil;

public class HBaseTest {

	private HBaseAdmin admin = null;
	private Configuration conf = null;

	@Before
	public void init() {
		conf = HBaseUtil.getConfiguration();
		try {
			admin = new HBaseAdmin(conf);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void creat() throws IOException {
		String tablename = "TEST2";
		String columnFamily = "F";
		if (admin.tableExists(tablename)) {
			System.out.println("table Exists!");
			admin.disableTable(tablename);
			admin.deleteTable(tablename);
			// System.exit(0);
			System.out.println("deleted");
		}
		TableName tabName = TableName.valueOf(tablename);
		HTableDescriptor tableDesc = new HTableDescriptor(tabName);
		HColumnDescriptor hcd = new HColumnDescriptor(columnFamily);
		hcd.setCompressionType(Compression.Algorithm.SNAPPY);
		hcd.setBloomFilterType(BloomType.ROW);
		hcd.setMaxVersions(10);
		tableDesc.addFamily(hcd);
		// tableDesc.addFamily(new HColumnDescriptor(columnFamily));
		admin.createTable(tableDesc);
		System.out.println("create table success!");

	}

	@Test
	public void scanAll() throws IOException {
		String tableName = "TEST2";
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan();
		ResultScanner resultScanner = table.getScanner(scan);
		for (Result result : resultScanner) {
			List<Cell> cells = result.listCells();
			for (Cell cell : cells) {
				byte[] rb = cell.getValueArray();
				String row = new String(result.getRow(), "UTF-8");
				String family = new String(CellUtil.cloneFamily(cell), "UTF-8");
				String qualifier = new String(CellUtil.cloneQualifier(cell), "UTF-8");
				String value = new String(CellUtil.cloneValue(cell), "UTF-8");
				System.out.println("[row:" + row + "],[family:" + family + "],[qualifier:" + qualifier + "],[value:"
						+ value + "]");
			}
		}
	}

	@Test
	public void desc() throws IOException {
		TableName tableName = TableName.valueOf("HBASE.GCZB");
		HTableDescriptor descriptor = admin.getTableDescriptor(tableName);
		Collection<HColumnDescriptor> collections = descriptor.getFamilies();

		for (HColumnDescriptor hColumnDescriptor : collections) {
			System.out.println(hColumnDescriptor.getNameAsString());
			ColumnFamilySchema schema = hColumnDescriptor.convert();
			List list = schema.getAttributesList();
			System.out.println(hColumnDescriptor.getEncryptionKey());
			// hColumnDescriptor.get
			// System.out.println(schema.getName());
		}

		HColumnDescriptor[] column = descriptor.getColumnFamilies();
		Set<byte[]> set = descriptor.getFamiliesKeys();
		for (byte[] bs : set) {
			// System.out.println(new String(bs));
		}

		// for (HColumnDescriptor hColumnDescriptor : column) {
		// System.out.println(hColumnDescriptor.getName());
		// }
	}

	@Test
	public void count() throws IOException {
		String tableName = "ZBXX";
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan();
		long start = System.currentTimeMillis();
		ResultScanner resultScanner = table.getScanner(scan);
		Iterator<Result> itor = resultScanner.iterator();
		int count = 0;
		for (Result result : resultScanner) {
			count++;
		}
		System.out.println(count);
		System.out.println((System.currentTimeMillis() - start));
		table.close();
	}

	@Test
	public void countByCoprocessor() {
		TableName tableName = TableName.valueOf("TB_FCT_OPERDAILY");
		Scan s = new Scan();
		long start = System.currentTimeMillis();
		// s.addColumn(Bytes.toBytes("F1"), Bytes.toBytes("D_FFRQ"));
		s.addFamily(Bytes.toBytes("F"));
		Configuration conf = HBaseUtil.getConfiguration();
		conf.set("hbase.client.scanner.caching", "4000");
		conf.set("hbase.rpc.timeout", "36000000");
		AggregationClient ac = new AggregationClient(conf);
		try {
			long count = ac.rowCount(tableName, new LongColumnInterpreter(), s);
			System.out.println("count: " + count);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("cost: " + (System.currentTimeMillis() - start));
	}

	@Test
	public void putData(String tableName, String row,String columnFamily ,String column,byte[] data) throws IOException {
		//String tableName = "TEST2";
		//String row = "1";
		//String columnFamily = "F";
		//String column = "D";
		//String data = "6666";
		// double data = 123.4;
		HTable table = new HTable(conf, tableName);
		Byte[] b = new Byte[8];
		Put put = new Put(Bytes.toBytes(row));

		//File file = new File("e:\\111.jpg");
		// byte[] fileBytes = FileUtil.file2byte(file);

		//put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(String.valueOf(data)));
		 put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
				 data);
		// table.put(put);

		// put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column),
		// null);
		table.put(put);
		System.out.println("put '" + row + "','" + columnFamily + ":" + column + "','" + data + "'");
		table.close();
	}

	@Test
	public void getData() throws IOException {
		String tableName = "TEST2";
		String row = "1";
		String columnFamily = "F";
		String column = "D";
		HTable table = new HTable(conf, tableName);
		// Scan scan = new Scan();
		// ResultScanner result = table.getScanner(scan);
		Get get = new Get(Bytes.toBytes(row));
		get.setMaxVersions();
		get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
		Result result = table.get(get);
		List<KeyValue> results = result.list();
		for (KeyValue kv : results) {
			System.out.println(new String(kv.getValueArray()));
		}
		// byte[] rb = result.getValue(Bytes.toBytes(columnFamily),
		// Bytes.toBytes(column));
		// FileUtil.byte2file(rb, "e:\\temp", "111.jpg");
		// String value = new String(rb, "UTF-8");
		// Double value = Double.valueOf(new String(rb));
		// System.out.println("value-----------" + value);
	}

	@Test
	public void deleteData() throws IOException {
		String tableName = "TEST2";
		String row = "1";
		String columnFamily = "F";
		String column = "D";
		HTable table = new HTable(conf, tableName);

		Delete del = new Delete(Bytes.toBytes(row));
		table.delete(del);
	}

	@Test
	public void delete() throws IOException {
		String tablename = "TEST2";
		if (admin.tableExists(tablename)) {
			try {
				admin.disableTable(tablename);
				admin.deleteTable(tablename);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
