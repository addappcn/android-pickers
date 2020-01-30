package cn.addapp.pickers.entity;

import java.util.List;

public class BaseData {
    public List<String> getShowContents() {
        return showContents;
    }

    public void setShowContents(List<String> showContents) {
        this.showContents = showContents;
    }

    public List<TimeWrapperEntity> getContentEntity() {
        return contentEntity;
    }

    public void setContentEntity(List<TimeWrapperEntity> contentEntity) {
        this.contentEntity = contentEntity;
    }

    private List<String> showContents;
    private List<TimeWrapperEntity> contentEntity;

    public BaseData() {
    }


}
