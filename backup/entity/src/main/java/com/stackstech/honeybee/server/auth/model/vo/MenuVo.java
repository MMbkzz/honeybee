package com.stackstech.honeybee.server.auth.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 */
public class MenuVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 生成long类型ID
     */
    private Long id;

    /**
     * 资源编码
     */
    private String code;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String descr;

    /**
     * 父级资源id
     */
    private Long parent_id;

    /**
     * 是否启用
     */
    private String status;

    /**
     * 分类id
     */
    private Long category_id;

    private String operationIds;
    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;
    /**
     * 创建人
     */
    private Date create_by;

    /**
     * 更新人
     */
    private Date update_by;

    /**
     * 预留属性1
     */
    private String attr1;

    /**
     * 预留属性2
     */
    private String attr2;

    /**
     * 预留属性3
     */
    private String attr3;

    /**
     * 预留属性4
     */
    private String attr4;

    /**
     * 预留属性5
     */
    private String attr5;

    private List<MenuVo> children = new ArrayList<MenuVo>(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Date create_by) {
        this.create_by = create_by;
    }

    public Date getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(Date update_by) {
        this.update_by = update_by;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public String getOperationIds() {
        return operationIds;
    }

    public void setOperationIds(String operationIds) {
        this.operationIds = operationIds;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

}
