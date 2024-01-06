package com.johnlyon.hoolipiper.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Manager")
public class Manager {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String aw;
    private String manager;
    private String name;
}
