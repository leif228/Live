package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.ui.contract.MineClientView;
import com.wj.work.ui.presenter.MineClientPresenter;
import com.wj.work.widget.adapter.CustomerAdapter;
import com.wj.work.widget.helper.ActivityAnimatorHelper;

import butterknife.BindView;

/**
 * MineCustomerListActivity
 * 2020/4/30 17:44
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class MineCustomerListActivity extends BaseMvpActivity<MineClientPresenter> implements MineClientView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    CustomerAdapter mAdapter;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, MineCustomerListActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_customer;
    }

    @Override
    protected void initView() {
        mAdapter=new CustomerAdapter(null);
        recyclerView.setAdapter(mAdapter);
        mAdapter.getData().addAll(DataTemp.getCustomerList());
        mAdapter.notifyDataSetChanged();
    }
}
