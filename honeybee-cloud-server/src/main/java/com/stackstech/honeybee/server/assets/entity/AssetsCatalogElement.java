package com.stackstech.honeybee.server.assets.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetsCatalogElement {

    private Long id;
    private String name;
    List<Object> element;

    public AssetsCatalogElement(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
