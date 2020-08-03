package com.wj.work.ui.activity;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.wj.work.R;
import com.wj.work.base.BaseActivity;
import com.wj.work.ui.fragment.product.ProductDetailFragment;
import com.wj.work.ui.fragment.product.ProductParamsFragment;
import com.wj.work.ui.fragment.product.ProductSurveyFragment;
import com.lib.kit.utils.StatusBarUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;

/**
 * ProductDetailActivity
 * 商品详情
 * 2020/4/7 16:08
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.tabs)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    private String[] mTitleDataList = new String[]{"商品", "详情", "商品参数"};

    @Override
    protected void initView() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(mActivity)
                .add("商品", ProductSurveyFragment.class)
                .add("详情", ProductDetailFragment.class)
                .add("商品参数", ProductParamsFragment.class)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
