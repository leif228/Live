package com.wj.work.widget.entity;

/**
 * RealNameInfo
 * 2020/5/7 16:06
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class RealNameInfo {

    private String idCard;
    private String idCardPositiveImg;
    private String idCardReverseSideImg;
    private String memo3; // 拒绝提示
    private String realName;
    private int typeId;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardPositiveImg() {
        return idCardPositiveImg;
    }

    public void setIdCardPositiveImg(String idCardPositiveImg) {
        this.idCardPositiveImg = idCardPositiveImg;
    }

    public String getIdCardReverseSideImg() {
        return idCardReverseSideImg;
    }

    public void setIdCardReverseSideImg(String idCardReverseSideImg) {
        this.idCardReverseSideImg = idCardReverseSideImg;
    }

    public String getMemo3() {
        return memo3;
    }

    public void setMemo3(String memo3) {
        this.memo3 = memo3;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
