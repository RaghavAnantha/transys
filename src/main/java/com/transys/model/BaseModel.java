package com.transys.model;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public interface BaseModel extends Serializable {
    Long getId();
    void setId(Long id);

    Long getCreatedBy();
    void setCreatedBy(Long createdBy);

    Long getModifiedBy();
    void setModifiedBy(Long modifiedBy);

    Date getCreatedAt();
    void setCreatedAt(Date createdAt);

    Date getModifiedAt();
    void setModifiedAt(Date modifiedAt);

    Integer getDeleteFlag();
    void setDeleteFlag(Integer deleteFlag);
    
    public String getHasDocs();
    public void setHasDocs(String hasDocs);
    
    public String[] getFileList();
}
