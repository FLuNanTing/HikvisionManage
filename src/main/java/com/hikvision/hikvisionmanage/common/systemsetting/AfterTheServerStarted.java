package com.hikvision.hikvisionmanage.common.systemsetting;

import com.hikvision.hikvisionmanage.core.sdk.HCNetSDK;
import com.hikvision.hikvisionmanage.devicemanage.bo.VidiconManage;
import com.hikvision.hikvisionmanage.devicemanage.service.CentralCoreService;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.vidicon.vidiconaction.VidiconAction;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: HikvisionManage
 * @description: 服务启动后操作
 * @author: LuNanTing
 * @create: 2020-05-12 10:35
 **/
@Component
public class AfterTheServerStarted implements ApplicationRunner {

    /**
     * 报警布防句柄
     */
    NativeLong lAlarmHandle;

    HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    /**
     * 报警回调函数实现
     */
    VidiconAction.FMSGCallBack_V31 fMSFCallBack_V31;

    @Autowired
    private List<VidiconManage> vidiconManageListBean;

    @Autowired
    private CentralCoreService centralCoreService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //启动后需要布防摄像机
        SetupAlarmChan();
    }

    private void SetupAlarmChan() {
        // 启动日志
        LoggerUtil.info("===启动日志===");
        if (hCNetSDK == null) {
            hCNetSDK = (HCNetSDK) Native.loadLibrary(HCNetSDK.filePath(), HCNetSDK.class);
        }
        if (vidiconManageListBean != null || vidiconManageListBean.size() > 0) {
            vidiconManageListBean.forEach(vidiconManage -> {
                lAlarmHandle = new NativeLong(-1);
                String devIp = vidiconManage.getDeviceIp();
                LoggerUtil.info("设备:" + vidiconManage.getDescription() + ",IP:" + devIp + "正在布防");
                String password = vidiconManage.getPassword();
                int port = vidiconManage.getDevicePort().intValue();
                NativeLong loginDev = centralCoreService.loginDevice(devIp, port, password);
                if (loginDev.intValue() == -1) {
//                    int net_DVR_GetLastError = hCNetSDK.NET_DVR_GetLastError();
                    LoggerUtil.error("设备登陆失败" );
                } else {
                    if (lAlarmHandle.intValue() < 0) {// 尚未布防,需要布防
                        if (fMSFCallBack_V31 == null) {
                            VidiconAction vidiconAction = new VidiconAction();
                            fMSFCallBack_V31 = vidiconAction.new FMSGCallBack_V31();
                            Pointer pUser = null;
                            if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
                                LoggerUtil.error("设置回调函数失败!");
                            }
                        }
                        HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
                        m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
                        m_strAlarmInfo.byLevel = 0;
                        m_strAlarmInfo.byAlarmInfoType = 1;
                        m_strAlarmInfo.write();
                        lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(loginDev, m_strAlarmInfo);
                        if (lAlarmHandle.intValue() == -1) {
                            LoggerUtil.error("=====设备{}布防失败=====", vidiconManage.getDeviceIp());
                        } else {
                            LoggerUtil.info("=====设备{}布防成功======", vidiconManage.getDeviceIp());
                        }
                    } else {
                        LoggerUtil.info("设备已布防");
                    }
                }
            });
        }
    }
}
