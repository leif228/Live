package com.wj.work.db;

import com.wj.work.db.BaseSp;
import com.wj.work.db.SpManager;
import com.wj.work.widget.entity.LoginEntity;

/**
 * 登录用户信息
 */
public final class LoginSp extends BaseSp {

    private final String KEY_USER_ID = "userId";
    private final String KEY_AVATAR = "avatar";
    private final String KEY_TEL = "tel";
    private final String KEY_REAL_NAME = "realName";
    private final String KEY_NICKNAME = "nickname";
    private final String KEY_GENDER = "gender";
    private final String KEY_TOKEN = "token";
    private final String KEY_ROLE_TYPE = "RoleType";
    private final String KEY_TYPE_ID = "typeId";
    private final String KEY_INVITATION_CODE = "myInvitationCode";
    private final String KEY_USER_TYPE = "userType";
    private final String IP = "ip";
    private final String PORT = "port";
    private final String FZWNO = "fzwno";


    LoginSp(SpManager spManager) {
        super(spManager);
    }

    @Override
    public void forLogout() {
        getSpManager().putLong(KEY_USER_ID, 0);
        getSpManager().putString(KEY_AVATAR, "");
        getSpManager().putString(KEY_TEL, "");
        getSpManager().putString(KEY_REAL_NAME, "");
        getSpManager().putString(KEY_NICKNAME, "");
        getSpManager().putInt(KEY_GENDER, 0);
        getSpManager().putString(KEY_TOKEN, "");

        getSpManager().putInt(KEY_ROLE_TYPE, 0);
        getSpManager().putInt(KEY_TYPE_ID, 0);
        getSpManager().putInt(KEY_INVITATION_CODE, 0);
        getSpManager().putInt(KEY_USER_TYPE, 0);


        getSpManager().putString(IP, "");
        getSpManager().putString(PORT, "");
        getSpManager().putString(FZWNO, "");
    }

    // ---------------------------------------------------------
    public void putLoginInfoEntity(LoginEntity loginInfo) {
        getSpManager().putLong(KEY_USER_ID, loginInfo.getUserId());
        getSpManager().putString(KEY_AVATAR, loginInfo.getUserImg());
        getSpManager().putString(KEY_TEL, loginInfo.getUserName());
        getSpManager().putInt(KEY_GENDER, loginInfo.getSex());
        getSpManager().putString(KEY_TOKEN, loginInfo.getToken());

        getSpManager().putInt(KEY_ROLE_TYPE, loginInfo.getRoleType());
        getSpManager().putInt(KEY_TYPE_ID, loginInfo.getTypeId());
        getSpManager().putInt(KEY_INVITATION_CODE, loginInfo.getMyInvitationCode());
        getSpManager().putInt(KEY_USER_TYPE, loginInfo.getUserType());

        getSpManager().putString(IP, loginInfo.getIp());
        getSpManager().putString(PORT, loginInfo.getPort());
        getSpManager().putString(FZWNO, loginInfo.getFzwno());
    }

    public LoginEntity getLoginInfoEntity() {
        LoginEntity result = new LoginEntity();
        result.setUserId(getSpManager().getLong(KEY_USER_ID, 0));
        result.setUserImg(getSpManager().getString(KEY_AVATAR, ""));
        result.setUserName(getSpManager().getString(KEY_TEL, ""));
        result.setSex(getSpManager().getInt(KEY_GENDER, 0));
        result.setToken(getSpManager().getString(KEY_TOKEN, ""));


        result.setRoleType(getSpManager().getInt(KEY_ROLE_TYPE, 0));
        result.setTypeId(getSpManager().getInt(KEY_TYPE_ID, 0));
        result.setMyInvitationCode(getSpManager().getInt(KEY_INVITATION_CODE, 0));
        result.setUserType(getSpManager().getInt(KEY_USER_TYPE, 0));


        result.setIp(getSpManager().getString(IP, ""));
        result.setPort(getSpManager().getString(PORT, ""));
        result.setFzwno(getSpManager().getString(FZWNO, ""));

        return result;
    }

    // 快捷方法 ------------------------------------------------
    public void putUserId(String id) {
        getSpManager().putString(KEY_USER_ID, id);
    }

    public String getUserId() {
        return getSpManager().getString(KEY_USER_ID, "");
    }

    public String getToken() {
        return getSpManager().getString(KEY_TOKEN, "");
    }

    public void updateRealNameType(int typeId){
        getSpManager().putInt(KEY_TYPE_ID, typeId);
    }

    public int getTypeId() {
        return getSpManager().getInt(KEY_TYPE_ID, -1);
    }
}
