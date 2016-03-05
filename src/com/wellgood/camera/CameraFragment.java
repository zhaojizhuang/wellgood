package com.wellgood.camera;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.util.Handler_Inject;
import com.cameralive.LiveActivity;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.playback.PlayBackActivity;
import com.wellgood.activity.R;
import com.wellgood.adapter.GridItem;
import com.wellgood.adapter.StickyGridAdapter;
import com.wellgood.application.APP;
import com.wellgood.fenleicamera.ImagesResource;
import com.zjz.pulltorefresh.PullToRefreshView;
import com.zjz.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.zjz.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;

public class CameraFragment extends Fragment  implements AbsListView.OnItemClickListener,OnHeaderRefreshListener,
OnFooterRefreshListener{
	
	private int[] images=new int[]{R.drawable.c1,R.drawable.c2,R.drawable.c3,R.drawable.c4,R.drawable.c5};
	
	
	
	
	public static String CLASS_NAME="CameraFragment";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    private static final int FETCH_DATA_TASK_DURATION = 500;

    private GridView mGridView;
    private StickyGridAdapter mAdapter;
    private List<GridItem>    listdata;
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
    List<CameraInfo> cameraList1 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList2 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList3 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList4 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList5 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList6 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList7 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList8 = new ArrayList<CameraInfo>();
    List<CameraInfo> cameraList9 = new ArrayList<CameraInfo>();
    /**区域列表**/
    List<RegionInfo> regionList=new ArrayList<RegionInfo>();
    /**二级区域列表**/
    List<RegionInfo> subregionList=new ArrayList<RegionInfo>();
    public static String  servAddr = "http://112.12.17.3";
    public static String userName="dbwl" ;
    public static String password="12345" ;
/*    String userName="test" ;
    String password="12345" ;*/
    ProgressDialog pd;
	View view;
	private Dialog mDialog;
	
	
	PullToRefreshView mPullToRefreshView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//this.inflater = inflater;
		Log.d(CLASS_NAME, "onCreateView()");
		view = inflater.inflate(R.layout.fragment_camera, container, false);
		Handler_Inject.injectOrther(this, view);
        getActivity().setTitle("公共");
    	mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
        
    	mGridView = (GridView)view.findViewById(R.id.asset_grid);


        listdata=new ArrayList<GridItem>() ;
        
        mAdapter = new StickyGridAdapter(getActivity(), APP.zhsqListdata);



        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(this);
        
        
		
		 if (APP.zhsqListdata.size()==0) {
			 Log.d(CLASS_NAME, "APP.zhsqListdata"+APP.zhsqListdata);
			 if (APP.NetAvalible) {
				 pd= ProgressDialog.show(getActivity(),"正在获取摄像头列表...","请等待...");
				 getCameraListThread();
			}
		
		}
        
        
		return view;
	}

	@InjectInit
	private void init(){
		Log.d(CLASS_NAME, "init()");

		
		//login();
		
	}
     
     
     
     
     
     
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        CameraInfo itemData = APP.zhsqListdata.get(position).getCameraInfo();
        Log.d(CLASS_NAME, "CameraInfo"+itemData.cameraID);
       //gotoLiveOrPlayBack((CameraInfo) itemData);
       gotoLive( itemData);
    }

    
	
	public List<GridItem>  fillImage(List<GridItem> list){
		List<GridItem> mlist=list;
		for (int i = 0; i < mlist.size(); i++) {
			mlist.get(i).setImageID(ImagesResource.yhxcImages[i%ImagesResource.yhxcImages.length]);
		}
		return mlist;
		
	}
    private void fillListData() {
    	try {
	
			        for (CameraInfo info : cameraList) {
			        	
			        	GridItem mGridItem = new GridItem(info,"迎晖社区");
			        	
			        	mGridItem.setSection(1);
			          APP.zhsqListdata.add(mGridItem);
			        }
			    for (CameraInfo info : cameraList1) {
			        	
			        	GridItem mGridItem = new GridItem(info,"桑梓头村");
			        	
			        	mGridItem.setSection(2);
			        	 APP.zhsqListdata.add(mGridItem);
			        }
			     for (CameraInfo info : cameraList2) {
			     	
			     	GridItem mGridItem = new GridItem(info,"溪口村");
			     	
			     	mGridItem.setSection(3);
			     	 APP.zhsqListdata.add(mGridItem);
			     }
			     for (CameraInfo info : cameraList3) {
			     	
			     	GridItem mGridItem = new GridItem(info,"茜畴村");
			     	
			     	mGridItem.setSection(4);
			     	 APP.zhsqListdata.add(mGridItem);
			     }
			     for (CameraInfo info : cameraList4) {
			     	
			     	GridItem mGridItem = new GridItem(info,"三甲院村");
			     	
			     	mGridItem.setSection(5);
			     	 APP.zhsqListdata.add(mGridItem);
			     }
			     for (CameraInfo info : cameraList5) {
			     	
			     	GridItem mGridItem = new GridItem(info,"召塘里村");
			     	
			     	mGridItem.setSection(6);
			     	 APP.zhsqListdata.add(mGridItem);
			     }
			     for (CameraInfo info : cameraList6) {
			     	
			     	GridItem mGridItem = new GridItem(info,"下街头村");
			     	
			     	mGridItem.setSection(7);
			     	 APP.zhsqListdata.add(mGridItem);
			     }
			    /* for (CameraInfo info : cameraList7) {
			      	
			      	GridItem mGridItem = new GridItem(info,"下街头村");
			      	
			      	mGridItem.setSection(8);
			      	 APP.zhsqListdata.add(mGridItem);
			      }*/
			     for (CameraInfo info : cameraList8) {
				      	
				      	GridItem mGridItem = new GridItem(info,"定午常村");
				      	
				      	mGridItem.setSection(9);
				      	 APP.zhsqListdata.add(mGridItem);
				      }
			     
			     
			     APP.zhsqListdata=fillImage( APP.zhsqListdata);
			   for (GridItem itm :  APP.zhsqListdata) {
				Log.e(CLASS_NAME, "cameraname"+(String) itm.getCameraInfo().name+"cameraid:"+itm.getCameraInfo().cameraID);
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
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
                	
                case MsgIds.GET_CAMERA_FAIL:
                	Log.d(CLASS_NAME, "获取摄像头列表失败");
                	mPullToRefreshView.onFooterRefreshComplete();
                	mPullToRefreshView.onHeaderRefreshComplete();
                	Toast.makeText(getActivity(), "请稍后再试", Toast.LENGTH_LONG).show();
                	fillListData();
                	pd.dismiss();
                	break;
             /*   case prom:
                	pb.setProgress((Integer)msg.obj);
                	//textView.setText((Integer)msg.obj+"%");
                	break;*/
                case MsgIds.GET_CAMERA_SUC:
                	Log.d(CLASS_NAME, "获取摄像头列表成功！");
                	pd.dismiss();
                	fillListData();
                	mAdapter.notifyDataSetChanged();
                	mPullToRefreshView.onFooterRefreshComplete();
                	mPullToRefreshView.onHeaderRefreshComplete();
                	//Intent intent = new Intent (APPStart.this,LoginActivity.class);
                	//pb.setProgress(100);
        			//Intent intent = new Intent (APPStart.this,ListActivity.class);
        			//Intent intent = new Intent (APPStart.this,WallView.class);		
                	//APPStart.this.finish();
        			//startActivity(intent);			
        			
                	break;
                	
                default:
                break;
            }

        }
    }
	
        
        
        /**获取摄像头列表**/
        public void getCameraListThread() {
			Log.d(CLASS_NAME, "getCameraList");
			  new Thread() {
	                public void run() {
	                	
	                	try {
							
						
			                	if (APP.NetAvalible) {
			                		cameraList=CameraUtils.getCameraInfoListfrom4Level("0/0/0/0/0");
								}else {
								}
			                
			                	
			                	if (APP.NetAvalible) {
			                		cameraList1=CameraUtils.getCameraInfoListfrom4Level("0/0/1/0/0");
			                	Log.d(CLASS_NAME, "cameraList1"+cameraList1);
			                	}else {
								}
			                	if (APP.NetAvalible) {
		
			                		cameraList2=CameraUtils.getCameraInfoListfrom4Level("0/0/2/0/0");
			                	}else {
								}
			                	if (APP.NetAvalible) {
			                		cameraList3=CameraUtils.getCameraInfoListfrom4Level("0/0/3/0/0");
			                	}else {
								}
			                	if (APP.NetAvalible) {
			                		cameraList4=CameraUtils.getCameraInfoListfrom4Level("0/0/3/1/0");
			                	}else {
								}
			                	if (APP.NetAvalible) {
			                		cameraList5=CameraUtils.getCameraInfoListfrom4Level("0/0/3/2/0");
			                	}else {
								}
			                	if (APP.NetAvalible) {
			                		cameraList6=CameraUtils.getCameraInfoListfrom4Level("0/0/3/3/0");
			                	}else {
								}
			                /*	if (APP.NetAvalible) {
			                		cameraList7=CameraUtils.getCameraInfoListfrom4Level("0/0/3/4/0");
			                	}else {
								}*/if (APP.NetAvalible) {
			                		cameraList8=CameraUtils.getCameraInfoListfrom4Level("0/0/4/0/0");
			                	}else {
								}
		
			                	
						        Log.d(Constants.LOG_TAG, "allcameraData size is " + cameraList.size());
						        if (cameraList==null) {
						            Log.d(Constants.LOG_TAG, "Invoke VMSNetSDK.getControlUnitList failed:");
						            Message msg = new Message();
									msg.what = MsgIds.GET_CAMERA_FAIL;
									msg.obj = cameraList;
									handler.sendMessage(msg);
						            
						        }else {
						        	Message msg = new Message();
									msg.what = MsgIds.GET_CAMERA_SUC;
									msg.obj = cameraList;
									
									
									
									handler.sendMessage(msg);
								}
				        
	                	} catch (Exception e) {
							// TODO: handle exception
	                		e.printStackTrace();
								handler.sendEmptyMessage(MsgIds.GET_CAMERA_FAIL);
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
    	
    	@Override
    	public void onHeaderRefresh(PullToRefreshView view) {
    		pd= ProgressDialog.show(getActivity(),"正在获取摄像头列表...","请等待...");
   		 this.getCameraListThread();
   		 Log.d(CLASS_NAME, "onFooterRefresh");
    		 //getCameraListThread();
    		//Toast.makeText(APP.app.getApplicationContext(), "onFooterRefresh", 0).show();
    		//mPullToRefreshView.onHeaderRefreshComplete();
    	}
    	@Override
    	public void onFooterRefresh(PullToRefreshView view) {
    		 pd= ProgressDialog.show(getActivity(),"正在获取摄像头列表...","请等待...");
    		 this.getCameraListThread();
    		 Log.d(CLASS_NAME, "onFooterRefresh");
    		//Toast.makeText(APP.app.getApplicationContext(), "onFooterRefresh", 0).show();
    		//mPullToRefreshView.onFooterRefreshComplete();
    	}



}
