package com.gk.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@TableName("b_resume")
public class Resume {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField
    public String cover;
    @TableField(exist = false)
    public MultipartFile coverFile;
    @TableField
    public String name;
    @TableField
    public String sex;
    @TableField
    public String birthday;
    @TableField
    public String raw;
    @TableField(exist = false)
    public MultipartFile rawFile;
    @TableField
    public String education;
    @TableField
    public String school;
    @TableField
    public String email;
    @TableField
    public String mobile;
    @TableField
    public String userId;
}
