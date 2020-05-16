package com.hikvision.hikvisionmanage.devicemanage.bo;

/**
 * @program: HikvisionManage
 * @description: LED屏幕管理
 * @author: LuNanTing
 * @create: 2020-05-16 09:26
 **/
public class LedScreenManage extends DeviceManage{

    /** 挂载摄像头 */
    private VidiconManage vidiconManage;

    public VidiconManage getVidiconManage() {
        return vidiconManage;
    }

    public void setVidiconManage(VidiconManage vidiconManage) {
        this.vidiconManage = vidiconManage;
    }
}
