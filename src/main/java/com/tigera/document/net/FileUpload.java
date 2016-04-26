package com.tigera.document.net;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;

public class FileUpload extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor.
	 */
	public FileUpload()
	{
	}
	
	// 缓存文件路径，
	static String	tempfiledir;
	
	// 这个好像暂时没用
	static String	uploadfiledir;
	
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		
		/*uploadfiledir = TigEraSetting.GetStoreDir();
		tempfiledir = TigEraSetting.GetCacheDir();
		
		ServletContext context = this.getServletContext();
		uploadfiledir = context.getRealPath(uploadfiledir);
		tempfiledir = context.getRealPath(tempfiledir);
		
		if (!uploadfiledir.endsWith(File.separator))
		{
			uploadfiledir = uploadfiledir + File.separator;
		}
		File dir = new File(uploadfiledir);
		dir.mkdirs();
		
		if (!tempfiledir.endsWith(File.separator))
		{
			tempfiledir = tempfiledir + File.separator;
		}
		
		dir = new File(tempfiledir);
		dir.mkdirs();*/
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String data = "Upload Batch,\nOnly support HTTP Post with Protobuffer data structure,\n"
				+ "   上传批次操作,\n仅支持基于Protobuff数据结构的 HTTP POST File";
		
		response.getOutputStream().write(data.getBytes("UTF-8"));
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		try
		{
			
			DiskFileItemFactory diskFactory = new DiskFileItemFactory();
			diskFactory.setRepository(new File(tempfiledir));
			
		/*	diskFactory.setSizeThreshold(1024 * 1024 * 128);
			ServletFileUpload upload = new ServletFileUpload(diskFactory);
			// 设置允许上传的最大文件，-1表示无上限
			upload.setFileSizeMax(-1);
			
			List<FileItem> fileitems = upload.parseRequest(request);
			Iterator iter = fileitems.iterator();
			while (iter.hasNext())
			{
				FileItem item = (FileItem) iter.next();
				if (item.isFormField())
				{
					// 表单内容，ignore
				}
				else
				{
					TigEraLogger.Log("上传.0....");
					ProcessDataFileUpload(item, response);
				}
			}*/
		}
		catch (Exception e)
		{
//			TigEraLogger.Log("上传.0.异常Exception occurs: " + e);
			e.printStackTrace();
		}
		
	}
	
	// one batch files is better save to disk first(or memory usage is very
	// high).
	
	/*private void ProcessDataFileUpload(FileItem item, HttpServletResponse hsr) throws IOException
	{
		
		NResultInfo nresult = new NResultInfo();
		
		String fname = item.getName();
		
		fname = fname.substring(fname.lastIndexOf("\\") + 1, fname.length());
		if (fname.endsWith(IBatchProcessor.PBOPEEXT))
		{
			nresult = ProcessBatchData(item);
		}
		else if (fname.endsWith(IBatchProcessor.PBDataExt))
		{
			nresult = ProcessBatchFileData(item);
		}
		else
		{
			// 上传不合规的数据，
			nresult.setStatus(EResultStatus.eFailed);
			nresult.setMsg("上传数据内容的扩展名非" + IBatchProcessor.PBDataExt + "或者" + IBatchProcessor.PBOPEEXT + "服务拒绝处理");
		}
		nresult.ToNetMsg().writeTo(hsr.getOutputStream());
		
	}
	
	private NResultInfo ProcessBatchData(FileItem item) throws IOException
	{
		NResultInfo nresult = new NResultInfo();
		
		TigEraLogger.Log("上传.0.获得元数据");
		
		MsgBatchInfo msgbatch = MsgBatchInfo.parseFrom(item.getInputStream());
		NBatchInfo nbatch = NBatchInfo.FromNetMsg(msgbatch);
		
		if (nbatch.IsFileDataComplete())
		{
			TigEraLogger.Log("上传.1.批次采用单文件");
			
			nresult = BatchOpeFactory.GetBatchOpe().ProcessBatch(nbatch);
			
		}
		else
		{
			TigEraLogger.Log("上传.2.1. 批次采分文件");
			
			///////
			// 创建目录信息等。
			BatchOpeFactory.GetBatchOpe().ProcessBatchHead(nbatch);
			_batchinfocache.put(nbatch.getBatchNO(), nbatch);// 暂时先缓存下来
		}
		return nresult;
	}
	
	private NResultInfo ProcessBatchFileData(FileItem item) throws IOException
	{
		
		NResultInfo nresult = new NResultInfo();
		TigEraLogger.Log("上传。2.2.获取文件数据");
		
		MsgFileInfo mfileinfo = MsgFileInfo.parseFrom(item.getInputStream());
		if (_batchinfocache.containsKey(mfileinfo.getBatchNO13()))
		{
			NFileInfo nfileinfo = NFileInfo.FromPBMsg(mfileinfo);
			
			NBatchInfo nbatch = _batchinfocache.get(mfileinfo.getBatchNO13());
			
			BatchOpeFactory.GetBatchOpe().ProcessBatchFile(nbatch, nfileinfo);
			
			// TODO: 文件数据完整后，进行ProcessBatch
			if (nbatch.IsFileDataComplete())
			{
				TigEraLogger.Log("2.3采用分文件传送批次，批次完成");
				// nresult = BatchOpeFactory.GetBatchOpe().ProcessBatch(nbatch);
				_batchinfocache.remove(nbatch.getBatchNO());;
			}
			
		}
		else
		{
			nresult = new NResultInfo();
			nresult.setStatus(EResultStatus.eFailed);
			nresult.setMsg("上传未知批次的内容，请重新上传整个批次");
		}
		return nresult;
	}
	
	static ConcurrentHashMap<String, NBatchInfo> _batchinfocache = new ConcurrentHashMap<String, NBatchInfo>();
	*/
}
