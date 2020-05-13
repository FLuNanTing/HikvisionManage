package com.hikvision.hikvisionmanage.vidicon.service;

import com.hikvision.hikvisionmanage.devicemanage.service.CentralCoreService;

import java.util.Map;

/**
 * 摄像机服务接口
 * @author LuNanTing
 * @Date 2020年5月13日10:46:01
 */
public interface VidiconService extends CentralCoreService {

    /**
     *
     * {通过指令控制道闸起落}
     * @author 创建人: LuNanTing
     * @date 时间： 2020年5月13日
     * @param deviceIp,设备IP
     * @param devicePort,设备端口
     * @param password,登录密码
     * @param command,指令{0-关闭道闸,1-开启道闸,2-停止道闸3-锁定道闸,4~解锁道闸}
     * @return
     * @return 返回类型：Map<String,Object>
     *
     */
    Map<String, Object> controlBrakeDev(String deviceIp, Integer devicePort, String password, Integer command);
}
