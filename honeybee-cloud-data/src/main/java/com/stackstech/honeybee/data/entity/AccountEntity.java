package com.stackstech.honeybee.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@NotNull(message = "request parameter cannot be null")
public class AccountEntity {

    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_NAME = "account";
    public static final String ACCOUNT_PWD = "password";

    private Long id;

    private String accountName;

    @JsonIgnore
    private String accountPassword;

    private Long accountRole;

    private String accountRealname;

    private Integer accountGender;

    private String accountEmail;

    private String accountPhone;

    private Integer status;

    @ApiModelProperty(hidden = true)
    private Long owner;

    @ApiModelProperty(hidden = true)
    private Date updatetime;

    @ApiModelProperty(hidden = true)
    private Date createtime;

    private String desc;

}