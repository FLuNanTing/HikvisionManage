package com.hikvision.hikvisionmanage.devicemanage.bo;

/**
 * @program: HikvisionManage
 * @description: 广播服务管理
 * @author: LuNanTing
 * @create: 2020-05-12 18:02
 **/
public class RadioServiceManage {

    private String radioIp;

    private Integer radioPort;

    private String radioProject;

    public String getRadioIp() {
        return radioIp;
    }

    public void setRadioIp(String radioIp) {
        this.radioIp = radioIp;
    }

    public Integer getRadioPort() {
        return radioPort;
    }

    public void setRadioPort(Integer radioPort) {
        this.radioPort = radioPort;
    }

    public String getRadioProject() {
        return radioProject;
    }

    public void setRadioProject(String radioProject) {
        this.radioProject = radioProject;
    }
}
