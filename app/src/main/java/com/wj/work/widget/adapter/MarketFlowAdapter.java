package com.wj.work.widget.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wj.work.R;
import com.wj.work.widget.entity.SifterTag;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

public class MarketFlowAdapter extends TagAdapter<SifterTag> {

    private Context context;
    public MarketFlowAdapter(Context context,List<SifterTag> datas) {
        super(datas);
        this.context=context;
    }

    @Override
    public View getView(FlowLayout parent, int position, SifterTag sifterTag) {
        TextView tv = (TextView) View.inflate(context,R.layout.item_flow_tag,null);
        tv.setText(sifterTag.getText());
        return tv;
    }
}
