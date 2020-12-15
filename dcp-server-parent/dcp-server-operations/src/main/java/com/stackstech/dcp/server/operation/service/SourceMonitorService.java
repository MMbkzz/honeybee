package com.stackstech.dcp.server.operation.service;

import com.stackstech.dcp.server.operation.vo.InstanceVO;
import com.stackstech.dcp.server.operation.vo.SourceMonitorQueryVO;
import com.stackstech.dcp.server.operation.vo.SourceMonitorVO;

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
