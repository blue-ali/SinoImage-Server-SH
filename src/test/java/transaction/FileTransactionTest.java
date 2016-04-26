package transaction;

public class FileTransactionTest {

/*	public void test() throws Exception {
		Log log = LogFactory.getLog(FileTransactionTest.class);
		// 这个是commons-transaction包中的类
		LoggerFacade logger = new CommonsLoggingLogger(log);
		// 存储的路径
		String storeDir = "d:/tmp";
		// 临时路径
		String workDir = "e:/tmp";
		// 构造函数的第三个参数：false，标识是否encoding文档的url，这个一般不需要设置为true
		FileResourceManager frm = new FileResourceManager(storeDir, workDir, false, logger);
		// FileResourceManager frm1 = new FileResourceManager(storeDir, workDir,
		// urlEncodePath, logger); 
		// 这标识frm的状态为start
		frm.start();
		// 下面需要frm的状态为start
		String txId = frm.generatedUniqueTxId();
		try {
			// 开启Transaction
			frm.startTransaction(txId);
			// frm.deleteResource(txId, "RFID.rtf");j
//			 frm.createResource(txId, "RFID.rtf");
			frm.copyResource(txId, "0005.jpg", "0006.jpg", false);
              			// throw new Exception("sdf");
		} catch (Exception e) {
			try {
				// 回滚事务
				e.printStackTrace();
				System.out.println("roll back ...");
				frm.rollbackTransaction(txId);
			} catch (ResourceManagerException e1) {
				e1.printStackTrace();
			}
		}
		// 提交事务
		frm.commitTransaction(txId);
	}

	public static void main(String[] args) {
		try {
			new FileTransactionTest().test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
