package com.wj.work.widget.params;

import com.wj.work.widget.entity.DefinitionEntity;
import com.wj.work.widget.entity.LiveTypeEntity;

import java.util.List;

/**
 * LiveParams
 * 2020/4/22 10:57
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class LiveParams {

    private LiveTypeEntity type;
    private long id;
    private List<String> goods;
    private boolean isPreview;
    private boolean isNotifyAllSubscriber ;
    private DefinitionEntity definition;
    private String  pushFlowAddress;
    private boolean isCameraFront=true;

    public boolean isCameraFront() {
        return isCameraFront;
    }

    public LiveParams setCameraFront(boolean cameraFront) {
        isCameraFront = cameraFront;
        return this;
    }

    public String getPushFlowAddress() {
        return pushFlowAddress;
    }

    public LiveParams setPushFlowAddress(String pushFlowAddress) {
        this.pushFlowAddress = pushFlowAddress;
        return this;
    }

    public long getId() {
        return id;
    }

    public LiveParams setId(long id) {
        this.id = id;
        return this;
    }

    public LiveTypeEntity getType() {
        return type;
    }

    public LiveParams setType(LiveTypeEntity type) {
        this.type = type;
        return this;
    }

    public List<String> getGoods() {
        return goods;
    }

    public LiveParams setGoods(List<String> goods) {
        this.goods = goods;
        return this;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public LiveParams setPreview(boolean preview) {
        isPreview = preview;
        return this;
    }

    public boolean isNotifyAllSubscriber() {
        return isNotifyAllSubscriber;
    }

    public LiveParams setNotifyAllSubscriber(boolean notifyAllSubscriber) {
        isNotifyAllSubscriber = notifyAllSubscriber;
        return this;
    }


    public DefinitionEntity getDefinition() {
        return definition;
    }

    public LiveParams setDefinition(DefinitionEntity definition) {
        this.definition = definition;
        return this;
    }
}
