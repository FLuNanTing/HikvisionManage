package com.hikvision.hikvisionmanage.devicemanage.service;

import com.hikvision.hikvisionmanage.core.sdk.HCNetSDK;
import com.sun.jna.NativeLong;

/**
 * 核心服务接口
 * @date :2020年5月12日14:42:06
 * @author :LuNanTing
 */
public interface CentralCoreService {

    static HCNetSDK HCNETSDK = HCNetSDK.INSTANCE;

    /** m_strDeviceInfo:设备信息 */
    HCNetSDK.NET_DVR_DEVICEINFO_V30 M_STRDEVICEINFO = null;
    /**
     *
     * {登录设备}
     * @author 创建人: LuNanTing
     * @date 时间： 2019年5月29日
     * @param deviceIp,设备IP
     * @param password,登录密码
     * @param devicePort,设备端口
     * @return
     * @return 返回类型：NativeLong
     *
     */
    NativeLong loginDevice(String deviceIp, int devicePort, String password);
}
