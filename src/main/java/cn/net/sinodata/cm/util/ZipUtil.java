package cn.net.sinodata.cm.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import cn.net.sinodata.framework.util.FileUtil;

public class ZipUtil {

	/**
	 * Zip压缩文件的数据流
	 * 
	 * @param filename
	 *            将数据流保存到zip包中使用的文件名
	 * @param filecontent
	 *            数据内容
	 * @return 压缩后的zip数据
	 */
	public static byte[] fileByte2Zip(String filename, byte[] filecontent) {
		byte[] b = new byte[] {};
		if (filecontent == null || filecontent.length == 0)
			return b;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zipOS = new ZipOutputStream(bos);
		zipOS.setLevel(6);
		ZipEntry entry = new ZipEntry(filename);
		entry.setTime(System.currentTimeMillis());
		entry.setSize(filecontent.length);
		try {
			zipOS.putNextEntry(entry);
			zipOS.write(filecontent, 0, filecontent.length);
			if (zipOS != null) {
				zipOS.finish();
				b = bos.toByteArray();
				zipOS.close();
				bos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * Zip压缩多个文件的数据流
	 * 
	 * @param filename
	 *            将数据流保存到zip包中使用的文件名
	 * @param filecontent
	 *            数据内容
	 * @return 压缩后的zip数据
	 * @throws Exception
	 */
	public static byte[] filesByte2Zip(Map<String, byte[]> fileBytes) throws Exception {
		Set<Entry<String, byte[]>> set = fileBytes.entrySet();
		byte[] b = new byte[] {};
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zipOS = new ZipOutputStream(bos);
		zipOS.setLevel(6);
		for (Entry<String, byte[]> entry : set) {
			String fileName = entry.getKey();
			byte[] fileContent = entry.getValue();
			if (fileContent == null || fileContent.length == 0)
				return b;
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipEntry.setTime(System.currentTimeMillis());
			zipEntry.setSize(fileContent.length);
			try {
				zipOS.putNextEntry(zipEntry);
				zipOS.write(fileContent, 0, fileContent.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			zipOS.finish();
			b = bos.toByteArray();
			zipOS.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public static byte[] files2ZipBytes(List<File> files) throws Exception {
		byte[] b = new byte[] {};
		byte[] buf = new byte[1024];

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zipOS = new ZipOutputStream(bos);
		zipOS.setLevel(6);
		
		int readLen = 0;
		for (File file : files) {
			String fileName = file.getName();
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipEntry.setTime(file.lastModified());
			zipEntry.setSize(file.length());
			try {
				zipOS.putNextEntry(zipEntry);
//				zipOS.write(fileContent, 0, fileContent.length);
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					zipOS.write(buf, 0, readLen);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			zipOS.finish();
			b = bos.toByteArray();
			zipOS.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	public static void file2Zip(Collection<File> files, String targetPath) throws Exception {

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(targetPath)));
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		int readLen = 0;
		for (File file : files) {
//			File f = (File) fileList.get(i);
//			("Adding: " + file.getPath() + file.getName());
			// 创建一个ZipEntry，并设置Name和其它的一些属性
			ze = new ZipEntry(file.getName());
			ze.setSize(file.length());
			ze.setTime(file.lastModified());
			// 将ZipEntry加到zos中，再写入实际的文件内容
			zos.putNextEntry(ze);
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				zos.write(buf, 0, readLen);
			}
			is.close();
		}
		zos.close();
	}
	
	/**
	 * 解压文件到指定路径
	 * @param file
	 * @param unzipPath
	 */
	public static void upZipFile(File file, String unzipPath) {
		File outFile = null;
		ZipFile zipFile;
		InputStream input = null;
		OutputStream output = null;
		try {
			zipFile = new ZipFile(file);
			@SuppressWarnings("resource")
			ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
			ZipEntry entry = null;
			while ((entry = zipInput.getNextEntry()) != null) {
				outFile = new File(unzipPath + File.separator + entry.getName());
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdir();
				}
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				input = zipFile.getInputStream(entry);
				output = new FileOutputStream(outFile);
				int temp = 0;
				while ((temp = input.read()) != -1) {
					output.write(temp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public static void main(String[] args) throws Exception {
		File dir = new File("f://tmp//download");
		List<File> files = Arrays.asList(dir.listFiles());
		byte[] b = ZipUtil.files2ZipBytes(files);
		FileUtil.byte2file(b, "d://tmp", "aaa.zip");
	}

}
