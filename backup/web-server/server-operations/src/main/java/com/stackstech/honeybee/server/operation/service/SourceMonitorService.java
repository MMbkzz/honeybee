package com.stackstech.honeybee.server.operation.service;

import com.stackstech.honeybee.server.operation.vo.InstanceVO;
import com.stackstech.honeybee.server.operation.vo.SourceMonitorQueryVO;
import com.stackstech.honeybee.server.operation.vo.SourceMonitorVO;

import java.util.List;

/**
 *
 */
public interface SourceMonitorService {
    List<SourceMonitorVO> queryAll(SourceMonitorQueryVO queryVO) throws Exception;

    int countAll(SourceMonitorQueryVO queryVO) throws Exception;

    List<InstanceVO> queryInstance(SourceMonitorQueryVO queryVO);

    int countInstance(SourceMonitorQueryVO queryVO);
}
