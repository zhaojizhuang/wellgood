/**
 * 
 */
package com.wellgood.camera;

/**
 * 消息id常量�??
 * 
 * @author zhoudaihui
 * 
 */
public interface MsgIds {
	
	/**
	 * 获取摄像头列表成�?
	 */
	int GET_CAMERA_SUC = 0x20;
	/**
	 * 获取摄像头列表失�?
	 */
	int GET_CAMERA_FAIL = 0x21;
	/**获取区域列表成功**/
	int GET_REGION_SUC = 0x30;
	/**
	 * 获取区域列表失败
	 */
	int GET_REGION_FAIL = 0x31;
	/**
	 * 获取控制中心列表成功
	 */
	int GET_C_F_NONE_SUC = 0x10;
	/**
	 * 获取控制中心列表失败
	 */
	int GET_C_F_NONE_FAIL = 0x11;
	/**
	 * 调用getControlUnitList失败
	 */
	int GET_CU_F_CU_FAIL = 0x12;
	/**
	 * 调用getRegionListFromCtrlUnit失败
	 */
	int GET_R_F_C_FAIL = 0x13;
	/**
	 * 调用getCameraListFromCtrlUnit失败
	 */
	int GET_C_F_C_FAIL = 0x14;
	/**
	 * 从控制中心获取下级资源列表成�??
	 */
	int GET_SUB_F_C_SUC = 0x15;
	/**
	 * 从控制中心获取下级资源列表成失败
	 */
	int GET_SUB_F_C_FAIL = 0x16;
	/**
	 * 调用getRegionListFromRegion失败
	 */
	int GET_R_F_R_FAIL = 0x017;
	/**
	 * 调用getCameraListFromRegion失败
	 */
	int GET_C_F_R_FAIL = 0x18;
	/**
	 * 从区域获取二级区域成功
	 */
	int GET_SUB_F_R_SUC = 0x19;
	/**
	 * 从区域获取二级区域失败
	 */
	int GET_SUB_F_R_FAILED = 0x1A;
	/**
	 * 从区域获取三级区域成功
	 */
	int GET_THRD_F_R_SUC = 0x40;
	/**
	 * 从区域获取三级区域失败
	 */
	int GET_THRD_F_R_FAILED = 0x41;

}
