package com.wj.work;

import com.wj.work.widget.entity.ConditionTab;
import com.wj.work.widget.entity.Customer;
import com.wj.work.widget.entity.DefinitionEntity;
import com.wj.work.widget.entity.LiveComment;
import com.wj.work.widget.entity.LiveTypeEntity;
import com.wj.work.widget.entity.PrimaryTab;
import com.wj.work.widget.entity.Product;
import com.wj.work.widget.entity.SifterTag;
import com.wj.work.widget.entity.StoreEntity;
import com.wj.work.widget.entity.SubTab;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 没网络请求前的临时数据
 * DataTemp
 * 2020/03/30 16:13
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class DataTemp {

    // 清晰度
    //  //  1:      2:超清720P  3 蓝光 1080P
    public static List<DefinitionEntity> getLiveDefinitionList() {
        List<DefinitionEntity> list = Lists.newArrayList();
        list.add(new DefinitionEntity(1, "超清 480P"));
        list.add(new DefinitionEntity(2, "超清 720P"));
        list.add(new DefinitionEntity(3, "蓝光 1080P"));
        return list;
    }

    // 直播分类
    public static List<LiveTypeEntity> getLiveTypeList() {
        List<LiveTypeEntity> list = Lists.newArrayList();
        list.add(new LiveTypeEntity(1, "玉石"));
        list.add(new LiveTypeEntity(2, "彩石"));
        list.add(new LiveTypeEntity(3, "宝石"));
        list.add(new LiveTypeEntity(4, "有机宝石"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(5, "珍珠"));
        list.add(new LiveTypeEntity(6, "其他材质"));
        return list;
    }

    public static List<SifterTag> getSifterTagList() {
        List<SifterTag> list = Lists.newArrayList();
        list.add(new SifterTag(0, "佛像"));
        list.add(new SifterTag(1, "平安扣"));
        list.add(new SifterTag(2, "生肖"));
        list.add(new SifterTag(3, "玉牌"));
        list.add(new SifterTag(4, "如意"));
        return list;
    }

    public static List<ConditionTab> getMarketConditionList() {
        List<ConditionTab> result = Lists.newArrayList();
        result.add(new ConditionTab(0, "热销", 0, false));
        result.add(new ConditionTab(1, "最新", 0, false));
        result.add(new ConditionTab(2, "价格", R.mipmap.ic_price_none, false));
        result.add(new ConditionTab(3, "筛选", R.mipmap.ic_sifter, false));
        return result;
    }

    public static List<PrimaryTab> getTabList() {

        List<PrimaryTab> result = Lists.newArrayList();

        PrimaryTab pTab = new PrimaryTab();
        pTab.setText("翡翠");

        List<SubTab> subTabList = Lists.newArrayList();
        SubTab subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("挂饰");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("戒指");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("手镯");
        subTabList.add(subTab);

        pTab.setList(subTabList);
        result.add(pTab);

        // ------------------------------------------------------
        pTab = new PrimaryTab();
        pTab.setText("砖石");

        subTabList = Lists.newArrayList();
        subTab = new SubTab();
        subTab.setText("挂饰");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("手镯");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("戒指");
        subTabList.add(subTab);

        pTab.setList(subTabList);
        result.add(pTab);

// ------------------------------------------------------
        pTab = new PrimaryTab();
        pTab.setText("K金");

        subTabList = Lists.newArrayList();
        subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("18K金条");
        subTabList.add(subTab);

        pTab.setList(subTabList);
        result.add(pTab);


        // ------------------------------------------------------
        pTab = new PrimaryTab();
        pTab.setText("翡翠砖石");

        subTabList = Lists.newArrayList();
        subTab = new SubTab();
        subTab.setText("翡翠");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("砖石");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("金手指");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("缅甸矿");
        subTabList.add(subTab);

        subTab = new SubTab();
        subTab.setText("西班牙和田玉");
        subTabList.add(subTab);

        pTab.setList(subTabList);
        result.add(pTab);

        return result;
    }


    public static List<Product> getProducts() {

        List<Product> result = Lists.newArrayList();
        Product item;

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠hahaha哈哈哈");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/750855741/O1CN01FLOgeU1sHQOhGP3gW_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠TTT");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/1692486039/TB1etiadlLN8KJjSZFPXXXoLXXa_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠TBCA");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/2236730845/O1CN01u7emK81I73FOTOk69_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);


        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠SJKDH");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i3/1692486039/TB2.yIHc5rpK1RjSZFhXXXSdXXa_!!1692486039-0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠ALKJSD");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i4/3447638660/O1CN01xlNXD42DqKSg2xeYH_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);


        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/750855741/O1CN01FLOgeU1sHQOhGP3gW_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/1692486039/TB1etiadlLN8KJjSZFPXXXoLXXa_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/2236730845/O1CN01u7emK81I73FOTOk69_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);


        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i3/1692486039/TB2.yIHc5rpK1RjSZFhXXXSdXXa_!!1692486039-0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i4/3447638660/O1CN01xlNXD42DqKSg2xeYH_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        return result;

    }


    public static List<StoreEntity> getAttentionStoreList() {
        List<StoreEntity> result = Lists.newArrayList();
        StoreEntity item;

        item = new StoreEntity();
        item.setName("鲸享家一店");
        item.setFenceNum(155);
        item.setAvatar("https://img.alicdn.com/imgextra/i1/750855741/O1CN01FLOgeU1sHQOhGP3gW_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new StoreEntity();
        item.setName("鲸享家二店");
        item.setFenceNum(188);
        item.setAvatar("https://img.alicdn.com/imgextra/i1/750855741/O1CN01FLOgeU1sHQOhGP3gW_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        return result;
    }


    public static List<Product> getAttentionProductList() {

        List<Product> result = Lists.newArrayList();
        Product item;

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/750855741/O1CN01FLOgeU1sHQOhGP3gW_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/1692486039/TB1etiadlLN8KJjSZFPXXXoLXXa_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠纯天然白兔毛释迦牟尼佛摆件吊坠纯天然白兔毛释迦牟尼佛摆件吊坠纯天然白兔毛释迦牟尼佛摆件吊坠纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/2236730845/O1CN01u7emK81I73FOTOk69_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);


        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i3/1692486039/TB2.yIHc5rpK1RjSZFhXXXSdXXa_!!1692486039-0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i4/3447638660/O1CN01xlNXD42DqKSg2xeYH_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);


        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/750855741/O1CN01FLOgeU1sHQOhGP3gW_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        item = new Product();
        item.setDescribe("纯天然白兔毛释迦牟尼佛摆件吊坠");
        item.setName("Name");
        item.setPrice("1290.00");
        item.setImg("https://img.alicdn.com/imgextra/i1/1692486039/TB1etiadlLN8KJjSZFPXXXoLXXa_!!0-item_pic.jpg_430x430q90.jpg");
        result.add(item);

        return result;
    }

    public static List<String> getProductImgs() {
        List<String> result = Lists.newArrayList();
        result.add("https://img.alicdn.com/imgextra/i1/1692486039/TB1etiadlLN8KJjSZFPXXXoLXXa_!!0-item_pic.jpg_430x430q90.jpg");
        result.add("https://img.alicdn.com/imgextra/i3/1692486039/TB2.yIHc5rpK1RjSZFhXXXSdXXa_!!1692486039-0-item_pic.jpg_430x430q90.jpg");
        result.add("https://img.alicdn.com/imgextra/i1/750855741/O1CN01FLOgeU1sHQOhGP3gW_!!0-item_pic.jpg_430x430q90.jpg");
        return result;
    }

    public static  List<LiveComment> getCommentData() {

        List<LiveComment> result = Lists.newArrayList();

        LiveComment comment=new LiveComment();
        comment.setContent("温馨提示：绿色直播，涉及色情、低俗、暴力等内容将被封停账号，文明直播，从我做起");
        comment.setUserName("竹林小溪");
        comment.setType(1);
        result.add(comment);

//        comment=new LiveComment();
//        comment.setContent("OIJSdJOJSDOSADADASDSADASDASDASDSADSAADDs");
//        comment.setUserName("竹林小溪");
//        comment.setType(2);
//        result.add(comment);
//
//
//        comment=new LiveComment();
//        comment.setContent("另类的虚幻来捧场了！");
//        comment.setType(3);
//        result.add(comment);
//
//
//        comment=new LiveComment();
//        comment.setContent("OIJSdJasdasdwqasdadaOJSDO");
//        comment.setUserName("竹林小溪");
//        comment.setType(2);
//        result.add(comment);
//
//        comment=new LiveComment();
//        comment.setContent("OIJSdJaOJSDO");
//        comment.setUserName("竹林小溪");
//        comment.setType(2);
//        result.add(comment);
//
//        comment=new LiveComment();
//        comment.setContent("OIJSdJaOJSDO");
//        comment.setUserName("竹林小溪");
//        comment.setType(2);
//        result.add(comment);
//
//        comment=new LiveComment();
//        comment.setContent("OIJSdJaOJSDO");
//        comment.setUserName("竹林小溪");
//        comment.setType(2);
//        result.add(comment);
//
//        comment=new LiveComment();
//        comment.setContent("OIJSdJaOJSDO");
//        comment.setUserName("竹林小溪");
//        comment.setType(2);
//        result.add(comment);
//
//        comment=new LiveComment();
//        comment.setContent("OIJSdJaOJSDO");
//        comment.setUserName("竹林小溪");
//        comment.setType(2);
//        result.add(comment);

        return result;
    }

    public static List<Customer> getCustomerList() {

        List<Customer> list=Lists.newArrayList();

        Customer temp=new Customer();
        temp.setAvatar("http://pics2.baidu.com/feed/0824ab18972bd40794fdb40f0133ea570eb3094e.jpeg?token=dc9221c706806c92c94bca5cfd0edcd7");
        temp.setName("潇湘子");
        temp.setId(1);
        list.add(temp);

        temp=new Customer();
        temp.setAvatar("http://pics2.baidu.com/feed/0824ab18972bd40794fdb40f0133ea570eb3094e.jpeg?token=dc9221c706806c92c94bca5cfd0edcd7");
        temp.setName("潇湘子1");
        temp.setId(2);
        list.add(temp);

        temp=new Customer();
        temp.setAvatar("http://pics2.baidu.com/feed/0824ab18972bd40794fdb40f0133ea570eb3094e.jpeg?token=dc9221c706806c92c94bca5cfd0edcd7");
        temp.setName("潇湘子2");
        temp.setId(3);
        list.add(temp);

        return list;
    }
}
