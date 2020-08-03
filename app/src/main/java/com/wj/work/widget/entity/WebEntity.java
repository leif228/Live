package com.wj.work.widget.entity;

// 用户登录信息
public class WebEntity {

    //"addTime": "2020-05-06T08:36:17.758Z",
    //    "addUserid": 0,
    //    "balance": 0,
    //    "deleteFlag": 0,
    //    "expirationTime": "2020-05-06T08:36:17.758Z",
    //    "loginTime": "2020-05-06T08:36:17.758Z",
    //    "memberGrade": 0,
    //    "memo1": "string",
    //    "memo2": "string",
    //    "memo3": "string",
    //    "mentioned": 0,
    //    "myInvitationCode": 0,
    //    "numberOfSubordinates": 0,
    //    "pid": 0,
    //    "pidsStr": "string",
    //    "roleType": 0,
    //    "sex": 0,
    //    "status": 0,
    //    "token": "string",
    //    "typeId": 0,
    //    "typeSub": 0,
    //    "updataBy": 0,
    //    "updateTime": "2020-05-06T08:36:17.758Z",
    //    "userId": 0,
    //    "userImg": "string",
    //    "userName": "string",
    //    "userType": 0

    private long userId; // 用户ID
    private String userName; // tel
    private String userImg;
    private int sex;
    private float balance;
    //用户类型：0.普通用户，1.小管家，2.大管家
    private int userType;
    private String token;
    //类型：0.未实名，1.审核中，2.驳回，3.成功
    private int typeId;
    //角色类型：0.普通用户，1.平台用户
    private int roleType;
    // 我的邀请码
    private int myInvitationCode;


    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getMyInvitationCode() {
        return myInvitationCode;
    }

    public void setMyInvitationCode(int myInvitationCode) {
        this.myInvitationCode = myInvitationCode;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }



    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
