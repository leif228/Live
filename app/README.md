# 20190409_jingxiangjia_admin
```

type: 
data:


type==0心跳 data={}
type==1 登陆netty data={userInfo: {}}
type==2 进入房间 data={roomInfo: {}}
type==3 进入房间 data={}
type==4 互动聊天 data={content: 发送内容}
type==5 商品点击 data={}
type==11 商品上下架/推荐 data={recommendedProducts: {待定}}




userInfo: {
/** user_id */
@ApiModelProperty(value = "user_id")
private Long userId ;

/** 用户名 */
@ApiModelProperty(value = "用户名")
private String userName ;

/** 用户头像 */
@ApiModelProperty(value = "用户头像")
private String userImg ;

/** 我的邀请码 */
@ApiModelProperty(value = "我的邀请码")
private Integer myInvitationCode ;

/** 性别 */
@ApiModelProperty(value = "性别")
private Integer sex ;

/** 父级ID */
@ApiModelProperty(value = "父级ID")
private Long pid ;

/** 所有父级 */
@ApiModelProperty(value = "所有父级")
private String pidsStr ;

/** 下级数量 */
@ApiModelProperty(value = "下级数量")
private Integer numberOfSubordinates ;

/** 余额 */
@ApiModelProperty(value = "余额")
private Double balance ;

/** 已提金额 */
@ApiModelProperty(value = "已提金额")
private Double mentioned ;

/** 会员等级 */
@ApiModelProperty(value = "会员等级")
private Integer memberGrade ;

/** 用户类型：0.普通用户，1.小管家，2.大管家 */
@ApiModelProperty(value = "用户类型：0.普通用户，1.小管家，2.大管家")
private Integer userType ;

/** 角色类型：0.普通用户，1.平台用户 */
@ApiModelProperty(value = "角色类型：0.普通用户，1.平台用户")
private Integer roleType ;

/** 类型：0.未实名，1.审核中，2.驳回，3.成功 */
@ApiModelProperty(value = "类型：0.未实名，1.审核中，2.驳回，3.成功")
private Integer typeId ;

/** 备用类型 */
@ApiModelProperty(value = "备用类型")
private Integer typeSub ;

/** 状态 */
@ApiModelProperty(value = "状态")
private Integer status ;

/** 删除状态 */
@ApiModelProperty(value = "删除状态")
private Integer deleteFlag ;

/** 失效时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "失效时间")
private Date expirationTime ;

/** 登陆时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "登陆时间")
private Date loginTime ;

/** 创建人 */
@ApiModelProperty(value = "创建人")
private Long addUserid ;

/** 更新人 */
@ApiModelProperty(value = "更新人")
private Long updataBy ;

/** 创建时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "创建时间")
private Date addTime ;

/** 更新时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "更新时间")
private Date updateTime ;

/** 备用 */
@ApiModelProperty(value = "备用")
private String memo1 ;

/** 备用 */
@ApiModelProperty(value = "备用")
private String memo2 ;

/** 备用 */
@ApiModelProperty(value = "实名认证驳回原因")
private String memo3 ;

@ApiModelProperty(value = "token")
private String token;

}






roomInfo: {
@ApiModelProperty(value = "id")
private Long id ;

/** 直播标题 */
@ApiModelProperty(value = "直播标题")
private String liveTitle ;

/** 直播封面 */
@ApiModelProperty(value = "直播封面")
private String liveCover ;

/** 店铺ID */
@ApiModelProperty(value = "店铺ID")
private Long shopId ;

/** 经度 */
@ApiModelProperty(value = "经度")
private String longitude ;

/** 纬度 */
@ApiModelProperty(value = "纬度")
private String latitude ;

/** 直播地址 */
@ApiModelProperty(value = "直播地址")
private String liveAddress ;

/** 推荐商品列表 */
@ApiModelProperty(value = "推荐商品列表")
private String recommendGoods ;

@ApiModelProperty(value = "在线人数")
private Integer onlineUsers = 0;

/** 观看人数 */
@ApiModelProperty(value = "观看人数")
private Integer viewers = 0;

/** 点赞次数 */
@ApiModelProperty(value = "点赞次数")
private Integer likes = 0;

/** 最大在线人数 */
@ApiModelProperty(value = "最大在线人数")
private Integer maxOnlineUsers = 0;

/** 累计互动 */
@ApiModelProperty(value = "累计互动")
private Integer interactions = 0;

/** 分享次数 */
@ApiModelProperty(value = "分享次数")
private Integer shares = 0;

/** 商品点击 */
@ApiModelProperty(value = "商品点击")
private Integer goodsClick = 0;

/** 订单数量 */
@ApiModelProperty(value = "订单数量")
private Integer orderQuantity = 0;

/** 订单金额 */
@ApiModelProperty(value = "订单金额")
private BigDecimal orderAmount;

/** 预估佣金 */
@ApiModelProperty(value = "预估佣金")
private BigDecimal commission;

/** 直播分类：0.玉石，1.彩石，2.宝石，3.有机宝石，4.珍珠，5.其他材质 */
@ApiModelProperty(value = "直播分类：0.玉石，1.彩石，2.宝石，3.有机宝石，4.珍珠，5.其他材质")
private Integer typeId ;

/** 直播类型：0.直播，1.直播预告 */
@ApiModelProperty(value = "直播类型：0.直播，1.直播预告")
private Integer typeSub ;

/** 清晰度：0.标清，1.高清，2.超清，3.1080p */
@ApiModelProperty(value = "清晰度：0.标清，1.高清，2.超清，3.1080p")
private Integer sharpnessType ;

/** 状态 */
@ApiModelProperty(value = "状态")
private Integer status ;

/** 删除状态 */
@ApiModelProperty(value = "删除状态")
private Integer deleteFlag ;

/** 创建人 */
@ApiModelProperty(value = "创建人")
private Long addUserid ;

/** 更新人 */
@ApiModelProperty(value = "更新人")
private Long updataBy ;

/** 创建时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "创建时间")
private Date addTime = new Date();

/** 预播时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "预播时间")
private Date preBroadcastTime ;

/** 直播开始时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "直播开始时间")
private Date updateTime ;

/** 直播结束时间 */
@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@ApiModelProperty(value = "直播结束时间")
private Date endTime ;

/** 备用 */
@ApiModelProperty(value = "备用")
private String memo1 ;

/** 备用 */
@ApiModelProperty(value = "备用")
private String memo2 ;

/** 备用 */
@ApiModelProperty(value = "备用")
private String memo3 ;

}






```