package cn.net.sinodata.cm.pb.bean;

import java.text.ParseException;

import com.tigera.document.definition.TigEraFileTransfer.EResultStatus;
import com.tigera.document.definition.TigEraFileTransfer.MsgResultInfo;


public class ResultInfo {
	
	private String msg = "";
	private EResultStatus status = EResultStatus.eSuccess;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public EResultStatus getStatus() {
		return status;
	}

	public void setStatus(EResultStatus status) {
		this.status = status;
	}

	public static ResultInfo fromPBBytes(byte[] bytes) throws Exception {
		MsgResultInfo mResultInfo = MsgResultInfo.parseFrom(bytes);
		ResultInfo resultInfo = ResultInfo.fromNetMsg(mResultInfo);
		return resultInfo;
	}
	
	public static ResultInfo fromNetMsg(MsgResultInfo input) throws ParseException {
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setMsg(input.getMsg());
		resultInfo.setStatus(input.getStatus());
		return resultInfo;
	}
	//////////////////////////////////////////////////
	public MsgResultInfo toNetMsg() {
		MsgResultInfo.Builder builder = MsgResultInfo.newBuilder();
		builder.setMsg(this.getMsg());
		builder.setStatus(this.getStatus());
		return builder.build();
	}

}