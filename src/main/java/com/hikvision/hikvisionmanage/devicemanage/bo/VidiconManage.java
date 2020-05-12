package com.hikvision.hikvisionmanage.devicemanage.bo;

/**
 * @program: HikvisionManage
 * @description: 摄像头管理封装
 * @author: LuNanTing
 * @create: 2020-05-11 15:29
 **/
public class VidiconManage extends DeviceManage {

    /** 设备ID */
    private String vidiconId;

    /** 设备登陆密码 */
    private String password;

    /** devModel:入出口模式0入口1出口 */
    private Integer vidiconModel;

    /** 服务器IP */
    private String serviceIp;

    /** 服务器端口 */
    private Integer servicePort;

    /** 设备所属公司 */
    private String companyCode;

    public String getVidiconId() {
        return vidiconId;
    }

    public void setVidiconId(String vidiconId) {
        this.vidiconId = vidiconId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getVidiconModel() {
        return vidiconModel;
    }

    public void setVidiconModel(Integer vidiconModel) {
        this.vidiconModel = vidiconModel;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public Integer getServicePort() {
        return servicePort;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
