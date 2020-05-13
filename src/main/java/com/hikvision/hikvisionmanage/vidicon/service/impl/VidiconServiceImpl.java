package com.hikvision.hikvisionmanage.vidicon.service.impl;

import com.hikvision.hikvisionmanage.vidicon.service.VidiconService;
import com.sun.jna.NativeLong;
import org.springframework.stereotype.Service;

/**
 * @program: HikvisionManage
 * @description: 摄像机接口实现
 * @author: LuNanTing
 * @create: 2020-05-13 10:46
 **/
@Service
public class VidiconServiceImpl implements VidiconService {
    /**
     * {登录设备}
     *
     * @param deviceIp
     * @param devicePort
     * @param password
     * @return 返回类型：NativeLong
     * @author 创建人: LuNanTing
     * @date 时间： 2019年5月29日
     */
    @Override
    public NativeLong loginDevice(String deviceIp, int devicePort, String password) {
        // 注册之前先注销已注册的用户,预览情况下不可注销
        NativeLong lUserId = new NativeLong(-1);
        String devUserName = "admin";
        lUserId = HCNETSDK.NET_DVR_Login_V30(deviceIp, (short) devicePort, devUserName, password, M_STRDEVICEINFO);
        return lUserId;
    }
}
