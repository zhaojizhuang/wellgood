package com.wellgood.frame.indicator.selectView;

import com.wellgood.fragment.BaseFragment;
import com.wellgood.fragment.InterviewFragment;
import com.wellgood.fragment.Settings;

/**
 * 用于顶部tab选择对应fragment
 **消息一栏的tab选择
 **/

public class MSelect  {


    public static BaseFragment select(int position) {
        InterviewFragment settings=new InterviewFragment();
        
        switch (position) {
		case 0:
			 return settings;
		case 1:
			 return settings;
		case 2:
			 return settings;
        }
		return settings;
    }




}
