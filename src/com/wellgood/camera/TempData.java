package com.wellgood.camera;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ServInfo;

public final class TempData {
	private static TempData ins = new TempData();

	/**
	 * 登录返回的数�?
	 */
	private ServInfo loginData;

	/**
	 * 监控点信息，用作临时传�?�数据用
	 */
	private CameraInfo cameraInfo;

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
}
