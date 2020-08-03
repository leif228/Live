package com.wj.work;

import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.lib.kit.utils.StatusBarUtils;
import com.lib.kit.utils.ToastUtils;
import com.next.easynavigation.view.EasyNavigationBar;
import com.wj.work.app.ActivityManager;
import com.wj.work.base.BaseActivity;
import com.wj.work.ui.fragment.MarketFragment;
import com.wj.work.ui.fragment.StewardFragment;
import com.wj.work.ui.fragment.WorkbenchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.navigationBar)
    EasyNavigationBar mNavigationBar;

    private String[] tabText = {"工作台", "云仓市场", "管家中心"};

    //未选中icon
    private int[] normalIcon = {
            R.mipmap.main_bench_normal,
            R.mipmap.main_market_normal,
            R.mipmap.main_steward_normal,
    };
    //选中时icon
    private int[] selectIcon = {
            R.mipmap.main_bench_checked,
            R.mipmap.main_market_checked,
            R.mipmap.main_steward_checked,
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected boolean beforeSetContentView() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void initView() {
        setNavigationBar();

//        NettyEngine.connect();
    }

    private void setNavigationBar() {
        fragments.add(new WorkbenchFragment());
        fragments.add(new MarketFragment());
        fragments.add(new StewardFragment());

        mNavigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .addLayoutRule(EasyNavigationBar.RULE_CENTER)
                .addLayoutBottom(0)
                .addAlignBottom(true)
                .addAsFragment(true)
                .smoothScroll(false)
                .canScroll(false)
                .iconSize(27)
                .tabTextTop(0)
                .tabTextSize(14)
                .navigationHeight(50)
                .normalTextColor(ContextCompat.getColor(this, R.color.text_gray))
                .selectTextColor(ContextCompat.getColor(this, R.color.app_orange))
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener((view, position) -> {
                    if (position==1){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            StatusBarUtils.setLightMode(MainActivity.this);
                        }
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            StatusBarUtils.setDarkMode(MainActivity.this);
                        }
                    }
                    return false;
                })
                .mode(EasyNavigationBar.MODE_NORMAL)
                .addLayoutRule(EasyNavigationBar.RULE_CENTER)
                .build();
    }

    private long backClickTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long ct = System.currentTimeMillis();
            if (ct - backClickTime <= 2000) {
                ActivityManager.instance().finishAllActivity();
            } else {
                backClickTime=ct;
                ToastUtils.showShort(R.string.press_exit_again);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
