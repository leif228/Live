package com.wj.work.ui.fragment.product;

import android.os.Bundle;
import android.view.View;

import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.BaseFragment;
import com.wj.work.widget.adapter.BannerImageAdapter;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.util.BannerUtils;

import java.util.List;

import butterknife.BindView;

/**
 * ProductSurveyFragment
 * 2020/4/7 16:48
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ProductSurveyFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner banner;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_product_survey;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initBanner();
    }

    private void initBanner() {
        List<String> imgs= DataTemp.getProductImgs();
        banner.setAdapter(new BannerImageAdapter(imgs, getActivity()));
        banner.setIndicator(new CircleIndicator(getActivity()));
        banner.setOrientation(Banner.HORIZONTAL);
        banner.setUserInputEnabled(true);
        banner.setIndicatorSelectedColorRes(R.color.white);
        banner.setIndicatorNormalColorRes(R.color.gray_33);
        banner.setIndicatorGravity(IndicatorConfig.Direction.CENTER);
        banner.setIndicatorSpace(BannerUtils.dp2px(10));
        banner.setIndicatorMargins(new IndicatorConfig.Margins((int) BannerUtils.dp2px(30), 0, 0, (int) BannerUtils.dp2px(6)));
        banner.setIndicatorWidth(10, 20);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
            }

            @Override
            public void onBannerChanged(int position) {
            }
        });
        banner.start();
    }
}
