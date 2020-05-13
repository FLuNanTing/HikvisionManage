package com.hikvision.hikvisionmanage.vidicon.service.impl;

import com.hikvision.hikvisionmanage.core.sdk.HCNetSDK;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.vidicon.service.VidiconService;
import com.sun.jna.NativeLong;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * {通过指令控制道闸起落}
     *
     * @param deviceIp
     * @param devicePort
     * @param password
     * @param command
     * @return 返回类型：Map<String,Object>
     * @author 创建人: LuNanTing
     * @date 时间： 2020年5月13日
     */
    @Override
    public Map<String, Object> controlBrakeDev(String deviceIp, Integer devicePort, String password, Integer command) {
        Map<String, Object> map = new HashMap<>();
        int portInt = devicePort.intValue();
        NativeLong user = loginDevice(deviceIp, portInt, password);
        long userId = user.longValue();
        if (userId == -1) {
            int net_DVR_GetLastError = HCNETSDK.NET_DVR_GetLastError();
            return map;
        } else {
            HCNetSDK.NET_DVR_BARRIERGATE_CFG struGateCfg = new HCNetSDK.NET_DVR_BARRIERGATE_CFG();
            struGateCfg.read();
            struGateCfg.dwSize = struGateCfg.size();
            // 通道号
            struGateCfg.dwChannel = 1;
            // 车道号
            struGateCfg.byLaneNo = 1;
            // 开道闸
            struGateCfg.byBarrierGateCtrl = command.byteValue();
            // 出入口编号，取值范围：[1,8]
            struGateCfg.byEntranceNo = 1;
            struGateCfg.write();
            boolean bCtrl = HCNETSDK.NET_DVR_RemoteControl(user, HCNetSDK.NET_DVR_BARRIERGATE_CTRL, struGateCfg.getPointer(), struGateCfg.size());
            if (!bCtrl) {
                LoggerUtil.error("NET_DVR_BARRIERGATE_CTRL道闸控制失败");
                map.put("errorCode", -2);
                map.put("errorMessage", "道闸控制失败");
            } else {
                LoggerUtil.info("NET_DVR_BARRIERGATE_CTRL道闸控制成功");
                map.put("errorCode", 0);
                map.put("errorMessage", "道闸控制成功");
            }
        }
//        logOut(user);
        map.put("date", System.currentTimeMillis());
        return map;
    }
}
