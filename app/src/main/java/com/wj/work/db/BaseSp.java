package com.wj.work.db;

import com.wj.work.db.SpManager;

/**
 *  base
 */
public abstract class BaseSp {

    private SpManager spManager;

    protected BaseSp(SpManager spManager) {
        this.spManager = spManager;
    }

    public SpManager getSpManager() {
        return spManager;
    }

    public abstract void forLogout();
}
