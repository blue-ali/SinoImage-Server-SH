/**
 * 
 */
package cn.net.sinodata.cm.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import cn.net.sinodata.cm.init.InitAllConfigService;
import cn.net.sinodata.cm.util.bean.ColumStructure;
import cn.net.sinodata.cm.util.db.DbConnection;

/**
 * Hiernate工具类，根据表生成java实体对象和hbm文件
 * @author manan
 */
public class HibernateUtil {
	
	private static String javaPath = System.getProperty("user.dir") + "\\src\\main\\java\\cn\\net\\sinodata\\cm\\hibernate\\";	//生成类的路径
	private static String hbmPath = System.getProperty("user.dir") + "\\src\\main\\java\\resources\\hibernate\\";	//生成hbm文件的路径
	private static String templatePath = HibernateUtil.class.getClassLoader().getResource("resources/template/template.hbm.xml").getPath();
	private static String poPkg = "cn.net.sinodata.cm.hibernate";
	
	public static void genreateAll(String tableName){
		String busiCode = getBusiCodeFromTableName(tableName);
		String className = "PO_" + busiCode;
		String hbmName = "Hbm_" + busiCode;
		genreateJavaEntity(className, tableName);
		genreateHbmFile(hbmName, className, tableName);
	}
	
	/**
     * 由表结构生成对应的java实体类
     * @param tableStructure	表结构对象
     * @author manan
     */
    public static boolean genreateJavaEntity(String className, String tableName){
    	tableName = tableName.toLowerCase();
//    	String className = StringUtils.capitalize(tableName);
    	String source = genrateSource(tableName, className);
    	System.out.println(source);
    	String fileName = javaPath + className + ".java";
    	System.out.println("fileName = " + fileName);
    	FileOutputStream out = null;
    	BufferedOutputStream buffer = null;
    	try {
    		File file = new File(fileName);
    		
    		if(file.exists()){
    			file.delete();
    			System.out.println("deleted");
    		}
    		file.createNewFile();
			out = new FileOutputStream(file);
			buffer = new BufferedOutputStream(out);
			buffer.write(source.getBytes());
			buffer.flush();
			System.out.println("file created");
			return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
        	try {
        		if(out != null){
        			out.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        	try {
        		if(buffer != null){
        			buffer.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
	
    public static boolean genreateHbmFile(String hbmName, String className, String tableName){
    	XMLHandler xml = new XMLHandler();
    	xml.loadXML(templatePath);
    	tableName = tableName.toUpperCase();
    	File hbmDir = new File(hbmPath);
    	if(!hbmDir.exists()){
    		hbmDir.mkdir();
    	}
    	String filePath = hbmPath + hbmName + ".hbm.xml";
    	String classPath = poPkg + "." + className;
    	xml.addAttribute("/hibernate-mapping/class", "name", classPath);
    	xml.addAttribute("/hibernate-mapping/class", "table", tableName);
    	
    	List<ColumStructure> colums = getTableColums(tableName);
    	for (ColumStructure colum : colums) {
			String columName = colum.getColumName().toLowerCase();
			String fieldName = columName;
			String fieldType = dbType2HbmType(colum.getColumType());
			boolean isPk = colum.isPk();
			String length = getLengthFrSqlType(colum.getColumType());
			Element parentElement = null;
			if(isPk){
				parentElement = xml.getElement("/hibernate-mapping/class/id");
			} else {
				parentElement = xml.addNode("/hibernate-mapping/class", "property", null);
			}
			xml.addAttributeOnElement(parentElement, "name", fieldName);
			xml.addAttributeOnElement(parentElement, "type", fieldType);
			Element columnElemen = xml.addNodeOnElement(parentElement, "column", null);
			xml.addAttributeOnElement(columnElemen, "name", columName);
			if (length != null && !length.equals("")){
				xml.addAttributeOnElement(columnElemen, "length", length);
			}
		}
    	
    	System.out.println(xml.toString());
    	System.out.println(filePath);
    	return xml.writeToFile(filePath);
    }
    
	 /**
     * 生成java源码
     * @param tableStructure 表结构对象
     * @return	java源码
     * @author manan
     */
    private static String genrateSource(String tableName, String className){
    	StringBuilder sb = new StringBuilder();
    	sb.append("package " + poPkg + ";\r\n\n");
    	sb.append("public class " + StringUtils.capitalize(className) + "{\r\n\n");
    	
    	List<ColumStructure> colums = getTableColums(tableName);
    	String str = genrateFieldAndMethod(colums);
    	sb.append(str);
    	sb.append("}\r\n");
    	return sb.toString();
    }
    
    private static String genrateFieldAndMethod(List<ColumStructure> colums){
    	StringBuilder fieldSB = new StringBuilder();
    	StringBuilder methodSB = new StringBuilder();
    	for (ColumStructure colum : colums) {
			String fieldName = colum.getColumName().toLowerCase();
			String fieldType = colum.getColumType();
			String javaType = dbType2JavaType(fieldType);
			fieldSB.append("\tprivate " + javaType + " " + fieldName + ";\r\n");
//			typeMap.put(fieldName, javaType);
			methodSB = genrateMethod(methodSB, fieldName, javaType);
			
		}
    
    	return fieldSB.append("\r\n" + methodSB).toString();
    }
    
    @SuppressWarnings("unchecked")
	private static String getBusiCodeFromTableName(String tableName){
    	String cfgPath = InitAllConfigService.class
    			.getClassLoader().getResource("resources/config/BusinessConfig.xml").getPath();
		String busiCode = null;
    	try {
			// 读取配置文件BusinessConfig.xml
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(cfgPath);
			Element ServiceClasses = doc.getRootElement();
			List<Element> services = ServiceClasses.getChildren("service");

			for (Element service : services) {
				if(tableName.equalsIgnoreCase(service.getChild("tablename").getTextTrim())){
					busiCode = service.getChild("busicode").getTextTrim().toLowerCase();
					break;
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return busiCode;
    }
    
    /**
     * 根据表名获取表结构
     * @param tableName
     * @return
     * @author manan
     */
    private static List<ColumStructure> getTableColums(String tableName){
		Connection conn = null;
		DatabaseMetaData dbmd = null;
		List<ColumStructure> list = new ArrayList<ColumStructure>();
		try {
			conn = DbConnection.getInstance().getConnection();
			dbmd = conn.getMetaData();
			ResultSet pkRs = dbmd.getPrimaryKeys(null, null, tableName.toUpperCase());
			String pk_column = "";
			//暂时只支持唯一主键
			if(pkRs.next()){
				pk_column =pkRs.getString("COLUMN_NAME");
			}
			
			ResultSet columRs = dbmd.getColumns(null, "%", tableName.toUpperCase(), "%");
			while(columRs.next()){
				String columnName = columRs.getString("COLUMN_NAME");
				String typeName = columRs.getString("TYPE_NAME");
				ColumStructure columnStructure = new ColumStructure();
				columnStructure.setColumName(columnName);
				columnStructure.setColumType(typeName);
				if(pk_column.equals(columnName)){
					columnStructure.setPk(true);
				} else {
					columnStructure.setPk(false);
				}
				list.add(columnStructure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
    
    /**
     * 生成对应set get方法
     * @param sb
     * @param fieldName
     * @return
     * @author manan
     */
    private static StringBuilder genrateMethod(StringBuilder sb, String fieldName, String javaType){
    	sb.append("\tpublic " + javaType + " get" + StringUtils.capitalize(fieldName));
    	sb.append("(){\r\n");
    	sb.append("\t\treturn " + fieldName + ";\r\n");
    	sb.append("\t}\r\n\n");
    	
    	sb.append("\tpublic void " + "set" + StringUtils.capitalize(fieldName));
    	sb.append("(" + javaType + " " + fieldName + "){\r\n");
    	sb.append("\t\tthis." + fieldName + " = " + fieldName + ";\r\n");
    	sb.append("\t}\r\n\n");
    	return sb;
    }
    
    private static String dbType2HbmType(String sqlType) {
    	if (sqlType.toLowerCase().contains("varchar")
                || sqlType.toLowerCase().contains("char")
                || sqlType.toLowerCase().contains("int")) {
            return "java.lang.String";
    	} else if (sqlType.equalsIgnoreCase("number")) {
            return "java.math.BigDecimal";
    	} else if (sqlType.equalsIgnoreCase("clob")){
    		return "text";
    	} else if (sqlType.equalsIgnoreCase("blob")){
    		return "org.springframework.orm.hibernate3.support.BlobByteArrayType";
    	} else if (sqlType.toLowerCase().contains("timestamp")){
    		return "java.util.Date";
    	}
    	
        return null;
	}
    
    /**
     * 数据库类型转换为java类型
     * @param sqlType	数据库类型
     * @return		java类型
     * @author manan
     */
    private static String dbType2JavaType(String sqlType) {
    	System.out.println("sqlType = " + sqlType);
    	if (sqlType.toLowerCase().contains("varchar")
                || sqlType.toLowerCase().contains("char")
                || sqlType.toLowerCase().contains("int")) {
            return "String";
    	} else if (sqlType.equalsIgnoreCase("number")) {
            return "java.math.BigDecimal";
    	} else if (sqlType.equalsIgnoreCase("clob")){
    		return "String";
    	} else if (sqlType.equalsIgnoreCase("blob")){
    		return "byte[]";
    	} else if (sqlType.toLowerCase().contains("timestamp")){
    		return "Date";
    	}
        return null;
    }
    
    private static String getLengthFrSqlType(String sqlType){
    	if (sqlType.toLowerCase().contains("(") && sqlType.toLowerCase().contains(")")){
    		int startIndex = sqlType.lastIndexOf("(") + 1;
    		int endIndex = sqlType.lastIndexOf(")");
    		return sqlType.substring(startIndex, endIndex);
    	}
    	return null;
    }
    
    public static void main(String[] args) {
//		genreateJavaEntity("POC_BUSINESS");
//    	getTableColums("POC_BUSINESS");
//    	genreateHbmFile("POC_BUSINESS");
//    	genreateAll("test_tb");
//    	genreateJavaEntity("PO_BIZ_TRANSLIST", "BIZ_TRANSLIST");
//		genreateHbmFile("BIZ_TRANSLIST", "PO_BIZ_TRANSLIST", "BIZ_TRANSLIST");
		genreateJavaEntity("FileInfoImpl", "cm_file_info");
		genreateHbmFile("FileInfoImpl", "FileInfoImpl", "cm_file_info");
		
	}
}
