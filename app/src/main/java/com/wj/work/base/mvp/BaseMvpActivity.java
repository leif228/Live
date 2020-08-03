package com.wj.work.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import com.wj.work.base.BaseActivity;
import com.wj.work.base.mvp.BasePresenter;

/**
 * @author tanghu
 * @title BaseMvpActivity
 * @package com.duoqio.gonet.base.mvp
 * @date 2020/4/2 20:01
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        super.onDestroy();
    }
}
