package com.wellgood.camera;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.tencent.android.tpush.XGPushTextMessage;
import com.wellgood.contract.MyCameraInfo;

public final class TempData {
	private static TempData ins = new TempData();

	/**
	 * 登录返回的数�?
	 */
	private ServInfo loginData;

	/**
	 * 监控点信息，用作临时传�?�数据用
	 */
	private MyCameraInfo mycameraInfo;
	private CameraInfo cameraInfo;
	private String cameraID;
	private String regionID;
	private String regionName;
	private XGPushTextMessage message;
	public XGPushTextMessage getMessage() {
		return message;
	}

	public void setMessage(XGPushTextMessage message) {
		this.message = message;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCameraID() {
		return cameraID;
	}

	public String getRegionID() {
		return regionID;
	}

	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}

	public void setCameraID(String cameraID) {
		this.cameraID = cameraID;
	}

	public static TempData getIns() {
		return ins;
	}

	/**
	  * 设置登录成功返回的信�?
	  * @param loginData
	  * @since V1.0
	  */
	public void setLoginData(ServInfo loginData) {
		this.loginData = loginData;
	}

	/**
	  * 获取登录成功返回的信�?
	  * @return
	  * @since V1.0
	  */
	public ServInfo getLoginData() {
		return loginData;
	}

	/**
	  * 保存监控点信�?
	  * @param cameraInfo
	  * @since V1.0
	  */
	public void setCameraInfo(CameraInfo cameraInfo) {
		this.cameraInfo = cameraInfo;
	}

	/**
	  * 获取监控点信�?
	  * @return
	  * @since V1.0
	  */
	public CameraInfo getCameraInfo() {
		return cameraInfo;
	}
	/**
	  * 保存监控点信�?
	  * @param cameraInfo
	  * @since V1.0
	  */
	public void setMyCameraInfo(MyCameraInfo mycameraInfo) {
		this.mycameraInfo = mycameraInfo;
	}

	/**
	  * 获取监控点信�?
	  * @return
	  * @since V1.0
	  */
	public MyCameraInfo getMyCameraInfo() {
		return mycameraInfo;
	}
}
