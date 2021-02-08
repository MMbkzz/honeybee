package com.stackstech.honeybee.common.entity;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonParameterMap extends ParameterMap<String, Object> {

}