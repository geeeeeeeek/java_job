package com.gk.study.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@TableName("b_company")
public class Company {
    @TableId(value = "id",type = IdType.AUTO)
    public Long id;
    @TableField
    public String cover;
    @TableField(exist = false)
    public MultipartFile coverFile;
    @TableField
    public String title;
    @TableField
    public String guimo;
    @TableField
    public String hangye;
    @TableField
    public String description;
    @TableField
    public String location;
    @TableField
    public String userId;

}
