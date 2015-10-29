package com.wellgood.frame.indicator.selectView;

import com.wellgood.fragment.*;


public class SecuritySelect  {


    public static BaseFragment select(int position) {
        People people=new People();
        Room room=new Room();
        switch (position) {
		case 0:
			 return people;
		case 1:
			 return room;
		
        }
		return people;
    }




}
