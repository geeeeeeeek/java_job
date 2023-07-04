package com.gk.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@TableName("b_post")
public class Post {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField
    public String resumeId;
    @TableField
    public String userId;
    @TableField
    public String thingId;
    @TableField
    public String companyId;
    @TableField
    public String createTime;

    @TableField(exist = false)
    public String name;

    @TableField(exist = false)
    public String raw;
}
