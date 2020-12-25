package com.stackstech.honeybee.server.platform.service;

import com.stackstech.honeybee.server.platform.model.ServiceDriver;
import com.stackstech.honeybee.server.platform.vo.ServiceDriverConfigVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 驱动Service接口
 */
public interface ServiceDriverService {

    List<ServiceDriver> queryAll(ServiceDriver serviceDriver) throws Exception;

    ServiceDriverConfigVO query(String id) throws Exception;

    ServiceDriver queryByName(String driverName) throws Exception;

    ResponseEntity<?> insert(ServiceDriverConfigVO serviceDriverConfigVO, MultipartFile file, HttpServletRequest req) throws Exception;

    ResponseEntity<?> update(ServiceDriverConfigVO serviceDriverConfigVO, MultipartFile file, HttpServletRequest req) throws Exception;

    ResponseEntity<?> delete(List<String> ids) throws Exception;

    int updateDriverstatus(ServiceDriver serviceDriver) throws Exception;

    int countAll(ServiceDriver serviceDriver) throws Exception;
}
