package com.hikvision.hikvisionmanage.vidicon.service.impl;

import com.hikvision.hikvisionmanage.core.sdk.HCNetSDK;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.vidicon.service.VidiconService;
import com.hikvision.hikvisionmanage.vidicon.vidiconaction.VidiconAction;
import com.hikvision.hikvisionmanage.vidicon.vidiconaction.VidiconAction.FMSGCallBack_V31;
import com.hikvision.hikvisionmanage.vidicon.vidiconutil.HikvisionErrorParseUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
     * lAlarmHandle:报警布防句柄
     */
    NativeLong lAlarmHandle = new NativeLong(-1);

    /**
     * fMSFCallBack_V31:报警回调函数实现
     */
    FMSGCallBack_V31 fMsfCallBackV31;

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

    /**
     * {通过给定设备IP,端口,用户名,密码来获取设备信息}
     *
     * @param deviceIp
     * @return 返回类型：Map<String,Object>
     * @author 创建人: LuNanTing
     * @date 时间： 2020年5月14日
     */
    @Override
    public Map<String, Object> getDeviceInformation(String deviceIp,String devicePortStr,String devicePassWord,String deviceId) {
        Map<String, Object> deviceInformationMap = new HashMap<>();
        Map<String, Object> devMap = new HashMap<>();
        if (!StringUtils.isEmpty(deviceIp) && (!StringUtils.isEmpty(devicePortStr) || !devicePortStr.equals("0"))) {
            Integer devicePort = Integer.valueOf(devicePortStr);
            String deviceUserName = "admin";
            NativeLong user = loginDevice(deviceIp, devicePort, devicePassWord);
            long userID = user.longValue();
            if (userID == -1) {
                int netDvrGetLastError = HCNETSDK.NET_DVR_GetLastError();
                Map<String, Object> deviceErrorMap = HikvisionErrorParseUtils.getErrorMassage(netDvrGetLastError);
                return deviceErrorMap;
            }
            // 获取IP接入配置参数
            IntByReference ibrBytesReturned = new IntByReference(0);
            M_STRIPPARACFG.write();
            Pointer lpIpParaConfig = M_STRIPPARACFG.getPointer();
            boolean net_DVR_GetDVRConfig = HCNETSDK.NET_DVR_GetDVRConfig(user, HCNetSDK.NET_DVR_GET_DEVICECFG, new NativeLong(0), lpIpParaConfig, M_STRIPPARACFG.size(), ibrBytesReturned);
            M_STRIPPARACFG.read();
            if (net_DVR_GetDVRConfig != true) {
                int errorCode = HCNETSDK.NET_DVR_GetLastError();
                NativeLongByReference pCurrentFormatDisk = new NativeLongByReference(user);
                String net_DVR_GetErrorMsg = HCNETSDK.NET_DVR_GetErrorMsg(pCurrentFormatDisk);

                devMap.put("errorCode", errorCode);
                devMap.put("errorMessage", net_DVR_GetErrorMsg);
                devMap.put("data", null);
                return devMap;
            }
            M_STRIPPARACFG.read();
            Map<String, Object> devList = new HashMap<>(5);
            // 设备名称
            devList.put("deviceName", new String(M_STRIPPARACFG.sDVRName).trim());
            // 设备序列号
            devList.put("devSeriesNumber", new String(M_STRIPPARACFG.sSerialNumber).trim());
            // 设备编号
            devList.put("deviceId", M_STRIPPARACFG.dwDVRID);
            devList.put("devType", M_STRIPPARACFG.byDVRType);
            devMap.put("errorCode", 0);
            devMap.put("errorMessage", "success");
            devMap.put("data", devList);
        } else if (!StringUtils.isEmpty(deviceId)) {

        } else {
            deviceInformationMap.put("errorCode", -1);
            deviceInformationMap.put("errorMessage", "设备IP,端口或者设备ID为空,无法追踪设备");
            deviceInformationMap.put("data", null);
        }


//        logOut(user);
        return devMap;
    }

    /**
     * {设备重新部署}
     *
     * @param deviceIp
     * @param devicePort
     * @param userName
     * @param password
     * @return 返回类型：Map<String,Object>
     * @author 创建人: LuNanTing
     * @date 时间： 2019年6月18日
     */
    @Override
    public Map<String, Object> afreshProtection(String deviceIp, Integer devicePort, String userName, String password) {
        Map<String, Object> map = new HashMap<>();
        // 首先登陆
        NativeLong loginDev = loginDevice(deviceIp, devicePort, password);
        if (loginDev.intValue() == -1) {
            int net_DVR_GetLastError = HCNETSDK.NET_DVR_GetLastError();
            map = HikvisionErrorParseUtils.getErrorMassage(net_DVR_GetLastError);
            LoggerUtil.error("设备登陆失败,失败原因为:" + map.get("errorMessage"));
            return map;
        }
        // 尚未布防,需要布防
        if (lAlarmHandle.intValue() > -1) {
            // 其次先注销该用户的布防
            if (HCNETSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle)) {
                LoggerUtil.info("撤防成功");
                lAlarmHandle = new NativeLong(-1);
            }
        } else {
            HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
            m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
            m_strAlarmInfo.byLevel = 1;
            m_strAlarmInfo.byAlarmInfoType = 1;
            m_strAlarmInfo.write();
            lAlarmHandle = HCNETSDK.NET_DVR_SetupAlarmChan_V41(loginDev, m_strAlarmInfo);
            if (lAlarmHandle.intValue() == -1) {
                LoggerUtil.error("============设备{}布防失败==============", deviceIp);
                int error = HCNETSDK.NET_DVR_GetLastError();
                map = HikvisionErrorParseUtils.getErrorMassage(error);
            } else {
                LoggerUtil.info("=============设备{}布防成功==============", deviceIp);
                map.put("errorCode", 0);
                map.put("errorMessage", deviceIp + "重新布防成功");
                map.put("data", null);
            }
        }
//        logOut(loginDev);
        return map;
    }
}
