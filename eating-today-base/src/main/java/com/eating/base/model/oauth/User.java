package com.eating.base.model.oauth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author han bin
 **/
@Data
@NoArgsConstructor
@TableName(value = "user", schema = "auth")
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    Long id;

    @TableField(value = "user_name")
    String userName;

    @TableField(value = "display_name")
    String displayName;

    @TableField(value = "pass_word")
    String passWord;

    @TableField(value = "ip_address")
    String ipAddress;

    @TableField(value = "logged_at")
    Date loggedAt;

    @TableField(value = "is_active")
    Boolean isActive;

    @TableField(value = "created_at")
    Date createdAt;
}
