package cn.net.sinodata.cm.hadoop.hbase;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;

public class HBaseUtil {

	private static String HADOOP_HOME;
	private static String HMASTER_LOCATION;
	private static String ZK_QUORUM;
	private static String ZK_CLIENTPORT;
	private static Configuration conf = null;
	private static HConnection conn = null;
	
	public static void initConfiguration(){
		Properties prop = new Properties();
		try {
			
			prop.load(HBaseUtil.class.getClassLoader().getResourceAsStream("/resources/hadoop/hbase.conf.property"));
			HADOOP_HOME = prop.getProperty("hadoop.home.dir");
			HMASTER_LOCATION = prop.getProperty("hbase.zookeeper.master");
			ZK_QUORUM = prop.getProperty("hbase.zookeeper.quorum");
			ZK_CLIENTPORT = prop.getProperty("hbase.zookeeper.clientPort");
			System.setProperty("hadoop.home.dir", HADOOP_HOME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取全局唯一的Configuration实例
	 * @return
	 */
	public static synchronized Configuration getConfiguration()
	{		
		if(conf == null)
		{
			initConfiguration();
			conf =  HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", ZK_QUORUM); 
			conf.set("hbase.zookeeper.property.clientPort", ZK_CLIENTPORT);			
		}		
		return conf;				
	}
	
	public static HBaseAdmin getHBaseAdmin(){
//		System.setProperty("hadoop.home.dir", "/hadoop");
		conf = HBaseUtil.getConfiguration();
		HBaseAdmin admin = null;
		try {
			admin = new HBaseAdmin(conf);
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return admin;
	}
	
	/**
	 * 获取全局唯一的HConnection实例
	 * @return
	 * @throws IOException 
	 */
	public static synchronized HConnection getHConnection() throws IOException
	{
		if(conn == null)
		{
			conn = HConnectionManager.createConnection(getConfiguration());
		}
		return conn;
	}
	
	public static void close(HTableInterface table, HConnection conn){
		if(table != null){
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		HBaseUtil.getConfiguration();
	}
}