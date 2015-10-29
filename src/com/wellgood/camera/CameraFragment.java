package com.wellgood.camera;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.util.Handler_Inject;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.live.LiveActivity;
import com.playback.PlayBackActivity;
import com.wellgood.activity.R;
import com.wellgood.frame.grid.StaggeredGridView;
import com.wellgood.frame.grid.Adapter.SampleAdapter;

public class CameraFragment extends Fragment  implements AbsListView.OnItemClickListener{
	public static String CLASS_NAME="CameraFragment";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    private static final int FETCH_DATA_TASK_DURATION = 500;

    private StaggeredGridView mGridView;
    private SampleAdapter mAdapter;

    /** 发送消息的对象 */
    private MsgHandler          handler       = new MsgHandler();
    /** 用户名输入框 */
   // private EditText            username;
    /** 密码输入框 */
    //private EditText            passwd;
    /** 登录按钮 */
   // private Button              loginBtn;
    /** 自动登录复选框 */
    //private CheckBox            autologinChk;

    /** 用户选中的线路 */
    private LineInfo            lineInfo;
    /** 登录返回的数据 */
    private ServInfo            servInfo=new ServInfo();
    /** 是否是第一次执行onResume方法 */
    private boolean             isFirstResume = true;
    /** 线路选择下拉框 */
    //private Spinner             lineSpinner;
    /** 获取线路按钮 */
    private Button              fetchLineBtn;
    /** 线路列表适配器 */
    //private ResourceListAdapter lineAdapter;
    /** 服务器地址输入框 */
   // private EditText            serverAddrEt;
    /**  线路列表**/
    List<LineInfo> lineInfoList = new ArrayList<LineInfo>();
    /**  控制中心列表**/
    List<ControlUnitInfo> ctrlUnitList = new ArrayList<ControlUnitInfo>();
    /**摄像头列表**/
    List<CameraInfo> cameraList = new ArrayList<CameraInfo>();
    /**区域列表**/
    List<RegionInfo> regionList=new ArrayList<RegionInfo>();
    
    String  servAddr = "http://112.12.17.3";
    String userName="test" ;
    String password="12345" ;
    ProgressDialog pd;
	View view;
	private Dialog mDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//this.inflater = inflater;
		Log.d(CLASS_NAME, "onCreateView()");
		view = inflater.inflate(R.layout.activity_sgv_empy_view, container, false);
		Handler_Inject.injectOrther(this, view);
        getActivity().setTitle("公共");
        
        
        mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);


    /*    View header = inflater.inflate(R.layout.list_item_header_footer, null);
        View footer = inflater.inflate(R.layout.list_item_header_footer, null);*/
/*        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("THE HEADER!");
        txtFooterTitle.setText("THE FOOTER!");*/

/*        mGridView.addHeaderView(header);
        mGridView.addFooterView(footer);*/
        mGridView.setEmptyView(view.findViewById(android.R.id.empty));
        mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);



        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(this);
		return view;
	}

	@InjectInit
	private void init(){
		Log.d(CLASS_NAME, "init()");
		 pd= ProgressDialog.show(getActivity(),"正在获取摄像头列表...","Please Wait...");
		fetchLine();
		//login();
		
	}
	
    private void fillAdapter() {
        for (CameraInfo info : cameraList) {
            mAdapter.add(info.name);
        }
    }
	
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
        final Object itemData = cameraList.get(position);
        //gotoLiveOrPlayBack((CameraInfo) itemData);
        gotoLive((CameraInfo) itemData);
    }

	/** handler  所以我们只要
	 * 在run方法中------发送Message,
	 * 在Handler里handleMessage----接收Message，
	 * 通过不同的Message执行不同的任务。  **/
	
	
    @SuppressLint("HandlerLeak")				//屏蔽lint错误
    private final class MsgHandler extends Handler {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.Login.GET_LINE_IN_PROCESS:
                	Log.d(CLASS_NAME, "获取线路中。。。");
                    //showGetLineProgress();
                break;
                case Constants.Login.GET_LINE_SUCCESS:
                	Log.d(CLASS_NAME, "获取线路中成功！");
                	List<LineInfo> lineList=(List<LineInfo>) msg.obj;
                	lineInfo=lineList.get(1);
                	login();
                    //onGetLineSuccess((List<Object>) msg.obj);
                break;
                case Constants.Login.GET_LINE_FAILED:
                	Log.d(CLASS_NAME, "获取线路中失败！");
                    //onGetLineFailed();
                break;
                case Constants.Login.SHOW_LOGIN_PROGRESS:
                    //showLoginProgress();
                	Log.d(CLASS_NAME, "正在登陆。。。");
                break;
                case Constants.Login.CANCEL_LOGIN_PROGRESS:
                    //cancelProgress();
                break;
                case Constants.Login.LOGIN_SUCCESS:
                    // 登录成功
                	Log.d(CLASS_NAME, "登陆成功");
                	getControlUnit();
                   // onLoginSuccess();
                break;
                case Constants.Login.LOGIN_FAILED:
                    // 登录失败
                   // onLoginFailed();
                break;
                case MsgIds.GET_C_F_NONE_FAIL:
                	Log.d(CLASS_NAME, "获取控制空心列表失败");
                	break;
                case MsgIds.GET_C_F_NONE_SUC:
                	Log.d(CLASS_NAME, "获取控制空心列表成功！");
                	getRegionList();
                	break;
                	
                case MsgIds.GET_CAMERA_FAIL:
                	Log.d(CLASS_NAME, "获取摄像头列表失败");
                	break;
                case MsgIds.GET_CAMERA_SUC:
                	Log.d(CLASS_NAME, "获取摄像头列表成功！");
                	pd.dismiss();
                	fillAdapter();
                	break;
                case MsgIds.GET_REGION_FAIL:
                	Log.d(CLASS_NAME, "获取区域列表失败");
                	break;
                case MsgIds.GET_REGION_SUC:
                	Log.d(CLASS_NAME, "获取区域列表成功！");
                	getCameraList();
                	break;
                	
                default:
                break;
            }

        }
    }
	
        /**
         * 登录
         */
        protected void login() {
        	Log.d(CLASS_NAME, "login()");
            
            /** 开启线程，新建一个线程   **/
            new Thread(new Runnable() {
                @Override
                public void run() {
                	
                    handler.sendEmptyMessage(Constants.Login.SHOW_LOGIN_PROGRESS);
                    

                    String macAddress = getMac();
                    Log.d(CLASS_NAME, "登陆参数"+"servAddr"+servAddr+"userName"+userName+"password"+password+"lineInfo.lineID"+lineInfo.lineID+"macAddress"+macAddress);
                    // 登录请求
                    boolean ret = VMSNetSDK.getInstance().login(servAddr, userName, password, lineInfo.lineID, macAddress,
                            servInfo);
                    if (servInfo == null) {
                    	Log.d(CLASS_NAME, "servInfo==null");
                    }
                    if (servInfo != null) {
                        // 打印出登录时返回的信息
                        Log.i(Constants.LOG_TAG, "login ret : " + ret);
                        Log.i(Constants.LOG_TAG, "login response info[" + "sessionID:" + servInfo.sessionID + ",userID:"
                                + servInfo.userID + ",magInfo:" + servInfo.magInfo + ",picServerInfo:"
                                + servInfo.picServerInfo + ",ptzProxyInfo:" + servInfo.ptzProxyInfo + ",userCapability:"
                                + servInfo.userCapability + ",vmsList:" + servInfo.vmsList + ",vtduInfo:"
                                + servInfo.vtduInfo + ",webAppList:" + servInfo.webAppList + "]");
                    }
                    Log.d(CLASS_NAME, "ret"+ret);
                     if (ret) {
                        TempData.getIns().setLoginData(servInfo);
                        handler.sendEmptyMessage(Constants.Login.LOGIN_SUCCESS);
                    } else {
                        handler.sendEmptyMessage(Constants.Login.LOGIN_FAILED);
                    }

                }
            }).start();
            Log.d(CLASS_NAME, "线程开启");
        }
        
        
        /**
         * 获取线路
         */
        protected void fetchLine() {
        	Log.d(CLASS_NAME, "fetchLine()");
            new Thread() {
                public void run() {
                    handler.sendEmptyMessage(Constants.Login.GET_LINE_IN_PROCESS);
                   // List<LineInfo> lineInfoList = new ArrayList<LineInfo>();
                    Log.i(Constants.LOG_TAG, "servAddr:" + servAddr);
                    boolean ret = VMSNetSDK.getInstance().getLineList(servAddr, lineInfoList);
                    if (ret) {
                        Message msg = new Message();
                        msg.what = Constants.Login.GET_LINE_SUCCESS;
                        msg.obj = lineInfoList;
                        //lineInfo=lineInfoList.get(1);
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(Constants.Login.GET_LINE_FAILED);
                    }
                };
            }.start();
        }
        /***
         * 获取控制中心列表
         * */
        protected void  getControlUnit() {
			Log.d(CLASS_NAME, "getControlUnit()");
			  new Thread() {
	                public void run() {
					  ServInfo loginData = TempData.getIns().getLoginData();
				        if (loginData == null) {
				            Log.d(Constants.LOG_TAG, "requestFirstList loginData:" + loginData);
				            return;
				        }
				        String sessionID = loginData.sessionID;
				        Log.d(CLASS_NAME, "sessionID"+sessionID);
				        int controlUnitID = 0;// 首次获取数据，表示根目录
				        int numPerPage = 10000;// 此处传10000，由于实际不可能有那么多，表示获取所有数据
				        int curPage = 1;
				       
				        // 获取控制中心列表
				        boolean ret = VMSNetSDK.getInstance().getControlUnitList(servAddr, sessionID, controlUnitID, numPerPage,
				                curPage, ctrlUnitList);
				        Log.d(Constants.LOG_TAG, "getControlUnitList ret:" + ret);
				        if (ctrlUnitList != null && !ctrlUnitList.isEmpty()) {
				            for (ControlUnitInfo info : ctrlUnitList) {
				                Log.d(Constants.LOG_TAG, "name:" + info.name + ",controlUnitID:" + info.controlUnitID + ",parentID:"
				                        + info.parentID);
				            }
				        }
				        Log.d(Constants.LOG_TAG, "allData size is " + ctrlUnitList.size());
				        if (!ret) {
				            Log.d(Constants.LOG_TAG, "Invoke VMSNetSDK.getControlUnitList failed:");
				            Message msg = new Message();
							msg.what = MsgIds.GET_C_F_NONE_FAIL;
							msg.obj = ctrlUnitList;
							handler.sendMessage(msg);
				            
				        }if (ret) {
				        	Message msg = new Message();
							msg.what = MsgIds.GET_C_F_NONE_SUC;
							msg.obj = ctrlUnitList;
							handler.sendMessage(msg);
						}
			         };
	         }.start();
		}
        /**获取区域列表**/
        protected void getRegionList() {
			Log.d(CLASS_NAME, "getRegionList");
			  new Thread() {
	                public void run() {
					  ServInfo loginData = TempData.getIns().getLoginData();
				        if (loginData == null) {
				            Log.d(Constants.LOG_TAG, "getRegion loginData:" + loginData);
				            return;
				        }
				        String sessionID = loginData.sessionID;
				        Log.d(CLASS_NAME, "region sessionID"+sessionID);
				        int controlUnitID = ctrlUnitList.get(0).controlUnitID;// 首次获取数据，表示根目录
				        int numPerPage = 10000;// 此处传10000，由于实际不可能有那么多，表示获取所有数据
				        int curPage = 1;
				       
				        Log.d(CLASS_NAME, "controlUnitID"+controlUnitID);
				        // 2.从控制中心获取区域列表
				        boolean ret = VMSNetSDK.getInstance().getRegionListFromCtrlUnit(servAddr, sessionID, controlUnitID, numPerPage,
				                curPage, regionList);
				        
				        if (regionList != null && !regionList.isEmpty()) {
				            for (RegionInfo info : regionList) {
				                Log.d(CLASS_NAME, "region :" + info.name + ",regionID:" + info.regionID );
				            }
				        }
				        Log.d(Constants.LOG_TAG, "allregion size is " + cameraList.size());
				        if (!ret) {
				            Log.d(Constants.LOG_TAG, "Invoke VMSNetSDK.getControlUnitList failed:");
				            Message msg = new Message();
							msg.what = MsgIds.GET_REGION_FAIL;
							msg.obj = regionList;
							handler.sendMessage(msg);
				            
				        }if (ret) {
				        	Message msg = new Message();
							msg.what = MsgIds.GET_REGION_SUC;
							msg.obj = regionList;
							handler.sendMessage(msg);
						}
			         };
	         }.start();
		}
        
        /**获取摄像头列表**/
        protected void getCameraList() {
			Log.d(CLASS_NAME, "getCameraList");
			  new Thread() {
	                public void run() {
					  ServInfo loginData = TempData.getIns().getLoginData();
				        if (loginData == null) {
				            Log.d(Constants.LOG_TAG, "getcamera loginData:" + loginData);
				            return;
				        }
				        String sessionID = loginData.sessionID;
				        Log.d(CLASS_NAME, "r sessionID"+sessionID);
				        int controlUnitID = regionList.get(0).regionID;// 首次获取数据，表示根目录
				        int numPerPage = 10000;// 此处传10000，由于实际不可能有那么多，表示获取所有数据
				        int curPage = 1;
				       
				        Log.d(CLASS_NAME, "controlUnitID"+controlUnitID);
				        // 3.从区域获取摄像头列表
				       boolean ret = VMSNetSDK.getInstance().getCameraListFromRegion(servAddr, loginData.sessionID, controlUnitID, numPerPage, curPage,
				                cameraList);
				        Log.d(CLASS_NAME, "getCameraListFromRegion ret:" + ret);
				        
				        if (cameraList != null && !cameraList.isEmpty()) {
				            for (CameraInfo info : cameraList) {
				                Log.d(CLASS_NAME, "careme :" + info.name + ",cameraType:" + info.cameraType + ",cameraType:"
				                        + info.cameraType);
				            }
				        }
				        Log.d(Constants.LOG_TAG, "allcameraData size is " + cameraList.size());
				        if (!ret) {
				            Log.d(Constants.LOG_TAG, "Invoke VMSNetSDK.getControlUnitList failed:");
				            Message msg = new Message();
							msg.what = MsgIds.GET_CAMERA_FAIL;
							msg.obj = cameraList;
							handler.sendMessage(msg);
				            
				        }if (ret) {
				        	Message msg = new Message();
							msg.what = MsgIds.GET_CAMERA_SUC;
							msg.obj = cameraList;
							handler.sendMessage(msg);
						}
			         };
	         }.start();
		}
        
        /**
         * 获取登录设备mac地址
         * 
         * @return
         */
        protected String getMac() {
            WifiManager wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            String mac = wm.getConnectionInfo().getMacAddress();
            return mac == null ? "" : mac;
        }
        public void initUI(){
 
        }

    	private void gotoLiveOrPlayBack(final CameraInfo info) {
    		String[] datas = new String[]{"预览","回放"};
    		mDialog = new AlertDialog.Builder(getActivity()).setSingleChoiceItems(datas, 0, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				mDialog.dismiss();
    				switch (which) {

    				case 0:
    					gotoLive(info);
    					break;
    				case 1:
    					gotoPlayback(info);
    					break;
    				default:
    					break;
    				}
    			}
    		}).create();
    		mDialog.show();
    		
    	}

    	/**
    	  * 进入远程回放
    	  * @param info
    	  * @since V1.0
    	  */
    	protected void gotoPlayback(CameraInfo info) {
    	    if(info == null){
    	        Log.e(Constants.LOG_TAG,"gotoPlayback():: fail");
    	        return;
    	    }
    		Intent it = new Intent(getActivity(), PlayBackActivity.class);
    		it.putExtra(Constants.IntentKey.CAMERA_ID, info.cameraID);
    		it.putExtra(Constants.IntentKey.DEVICE_ID, info.deviceID);
    		getActivity().startActivity(it);
    		
    	}

    	/**
    	  * 进入实时预览
    	  * @param info
    	  * @since V1.0
    	  */
    	protected void gotoLive(CameraInfo info) {
    	    if(info == null){
                Log.e(Constants.LOG_TAG,"gotoLive():: fail");
                return;
            }
    		Intent it = new Intent(getActivity(), LiveActivity.class);
    		it.putExtra(Constants.IntentKey.CAMERA_ID, info.cameraID);
    		TempData.getIns().setCameraInfo(info);
    		getActivity().startActivity(it);
    	}

}
