package com.hikvision.hikvisionmanage.devicemanage.bo;

/**
 * @program: HikvisionManage
 * @description: 设备管理基础封装
 * @author: LuNanTing
 * @create: 2020-05-11 15:30
 **/
public class DeviceManage {

    /** 设备IP */
    private String deviceIp;

    /** 设备端口 */
    private Integer devicePort;

    /** 设备描述 */
    private String description;

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public Integer getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(Integer devicePort) {
        this.devicePort = devicePort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
