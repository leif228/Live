package com.wj.work.ui.helper;

import com.wj.work.R;
import com.wj.work.widget.entity.ConditionTab;

public class DataHelper {

    public static void resetTabAfterTap(int index,ConditionTab tab){
        switch (index){
            case 0:
            case 1:
                tab.setSelect(!tab.isSelect());
                break;
            case 2:
                tab.setSelect(true);
                if (tab.getMode()==0){
                    tab.setMode(1);
                }else{
                    tab.setMode(-tab.getMode());
                }
                tab.setImg(tab.getMode()==0?R.mipmap.ic_price_none
                        :tab.getMode()==1?R.mipmap.ic_price_up
                        :R.mipmap.ic_price_down
                );
                break;
        }
    }

}
