package com.cameralive;

/**
 * 预览模块用到的常�?
 * 
 * @author huangweifeng
 * @Data 2013-10-21
 */
public class ConstantLive {

	private static final int ERR_BASE = 10000;
	/**
	 * 取流成功
	 * */
	public static final int RTSP_SUCCESS = ERR_BASE;
	/**
	 * 启动播放失败
	 **/
	public static final int START_OPEN_FAILED = ERR_BASE + 1;
	/**
	 * 播放成功
	 * */
	public static final int PLAY_DISPLAY_SUCCESS = ERR_BASE + 2;
	/**
	 * 停止成功
	 * */
	public static final int STOP_SUCCESS = ERR_BASE + 3;
	/**
	 * 播放库句柄不可用
	 * */
	public static final int PLAYER_HANDLE_NULL = ERR_BASE + 4;
	/**
	 * 播放库端口不可用
	 * */
	public static final int PLAYER_PORT_UNAVAILABLE = ERR_BASE + 5;
	/**
	 * RTSP链接失败
	 * */
	public static final int RTSP_FAIL = ERR_BASE + 6;
	/**
	 * 获取OSD时间失败
	 * */
	public static final int GET_OSD_TIME_FAIL = ERR_BASE + 7;
	/**
	 * SD卡不可用
	 * */
	public static final int SD_CARD_UN_USEABLE = ERR_BASE + 8;
	/**
	 * SD卡空间不�?
	 * */
	public static final int SD_CARD_SIZE_NOT_ENOUGH = ERR_BASE + 9;
	/**
	 * 非播放状态不能抓�?
	 */
	public static final int CAPTURE_FAILED_NPLAY_STATE = ERR_BASE + 10;
	/**
	 * 从MAG取流标签
	 * */
	public static final int MAG = 2;
	/**
	 * 主码流标�?
	 */
	public static final int MAIN_STREAM = 0;
	/**
	 * 子码流标�?
	 * */
	public static final int SUB_STREAM = 1;

}
