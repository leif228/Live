package com.wj.work.base.mvp;

import com.wj.work.base.BaseFragment;
import com.wj.work.base.mvp.BasePresenter;

/**
 * @author tanghu
 * @title BaseMvpFragment
 * @package com.duoqio.gonet.base.mvp
 * @date 2020/4/2 20:14
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mPresenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
    }
}
