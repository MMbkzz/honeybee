package com.stackstech.honeybee.core.http;

import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 返回实体基类
 */
@XmlRootElement
@Component
public class ResponseBaseBean implements Serializable {
}
