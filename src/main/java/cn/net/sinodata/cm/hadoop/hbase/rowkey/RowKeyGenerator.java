package cn.net.sinodata.cm.hadoop.hbase.rowkey;


public interface RowKeyGenerator {
    byte [] nextId();
}
