package com.hikvision.hikvisionmanage.vidicon.service;

import com.hikvision.hikvisionmanage.core.sdk.HCNetSDK;
import com.hikvision.hikvisionmanage.devicemanage.service.CentralCoreService;


import java.util.Map;

/**
 * 摄像机服务接口
 * @author LuNanTing
 * @Date 2020年5月13日10:46:01
 */
public interface VidiconService extends CentralCoreService {

    /**
     * 设备参数结构体初始化
     */
    HCNetSDK.NET_DVR_DEVICECFG M_STRIPPARACFG = new HCNetSDK.NET_DVR_DEVICECFG();



    /**
     *
     * {通过指令控制道闸起落}
     * @author 创建人: LuNanTing
     * @date 时间： 2020年5月13日
     * @param deviceIp,设备IP
     * @param devicePort,设备端口
     * @param password,登录密码
     * @param command,指令{0-关闭道闸,1-开启道闸,2-停止道闸3-锁定道闸,4~解锁道闸}
     * @param canRelease 放行提示状态
     * @return
     * @return 返回类型：Map<String,Object>
     *
     */
    Map<String, Object> controlBrakeDev(String deviceIp, Integer devicePort, String password, Integer command, Object canRelease);


    /**
     *
     * {通过给定设备IP,端口,用户名,密码来获取设备信息}
     * @author 创建人: LuNanTing
     * @date 时间： 2020年5月14日
     * @param deviceIp,设备信息集合
     * @return
     * @return 返回类型：Map<String,Object>
     *
     */
    Map<String, Object> getDeviceInformation(String deviceIp,String devicePortStr,String devicePassWord,String deviceId);

    /**
     *
     * {设备重新部署}
     * @author 创建人: LuNanTing
     * @date 时间： 2019年6月18日
     * @param deviceIp
     * @param devicePort
     * @param userName
     * @param password
     * @return
     * @return 返回类型：Map<String,Object>
     *
     */
    Map<String, Object> afreshProtection(String deviceIp, Integer devicePort, String userName, String password);
}
