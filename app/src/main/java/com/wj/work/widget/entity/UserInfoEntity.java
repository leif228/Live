package com.wj.work.widget.entity;

import java.io.Serializable;
import java.util.Date;

//"用户表"
public class UserInfoEntity implements Serializable{

    /** user_id */
    private Long userId ;

    /** 用户头像 */
    private String userImg ;

    /** 我的邀请码 */
    private Integer myInvitationCode ;

    /** 用户名 */
    private String userName ;

    /** 密码 */
    private String passWord ;

    /** 密码盐 */
    private String passSalt ;

    /** 支付密码 */
    private String payPass ;

    /** 支付盐 */
    private String paySalt ;

    /** 真实姓名 */
    private String realName ;

    /** 身份证 */
    private String idCard ;

    /** 身份证正面 */
    private String idCardPositiveImg ;

    /** 身份证反面 */
    private String idCardReverseSideImg ;

    /** 性别 */
    private Integer sex ;

    /** 微信ID */
    private String wxOpenid ;

    /** 微信昵称 */
    private String wxNickname ;

    /** 微信性别 */
    private String wxSex ;

    /** 微信头像 */
    private String wxHeadPortrait ;

    /** 父级ID */
    private Long pid ;

    /** 所有父级 */
    private String pidsStr ;

    /** 下级数量 */
    private Integer numberOfSubordinates ;

    /** 余额 */
    private Double balance ;

    /** 已提金额 */
    private Double mentioned ;

    /** 会员等级 */
    private Integer memberGrade ;

    /** 用户类型：0.普通用户，1.小管家，2.大管家 */
    private Integer userType ;

    /** 角色类型：0.普通用户，1.平台用户 */
    private Integer roleType ;

    /** 类型 */
    private Integer typeId ;

    /** 备用类型 */
    private Integer typeSub ;

    /** 状态 */
    private Integer status ;

    /** 删除状态 */
    private Integer deleteFlag ;

    /** 失效时间 */
    private Date expirationTime ;

    /** 登陆时间 */
    private Date loginTime ;

    /** 创建人 */
    private Long addUserid ;

    /** 更新人 */
    private Long updataBy ;

    /** 更新时间 */
    private Date updateTime ;

    /** 备用 */
    private String memo1 ;

    /** 备用 */
    private String memo2 ;

    /** 备用 */
    private String memo3 ;
}