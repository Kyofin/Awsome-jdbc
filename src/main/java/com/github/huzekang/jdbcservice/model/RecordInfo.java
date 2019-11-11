

package com.github.huzekang.jdbcservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class RecordInfo<T> {

    @JSONField(serialize = false)
    Long createBy;

    @JSONField(serialize = false)
    Date createTime;

    @JSONField(serialize = false)
    Long updateBy;

    @JSONField(serialize = false)
    Date updateTime;

    public T createdBy(Long userId) {
        this.createBy = userId;
        this.createTime = new Date();
        return (T) this;
    }

    public void updatedBy(Long userId) {
        this.updateBy = userId;
        this.updateTime = new Date();
    }
}
