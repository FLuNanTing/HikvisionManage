package com.hikvision.hikvisionmanage.devicemanage.bo;

/**
 * @program: HikvisionManage
 * @description: 广播服务管理
 * @author: LuNanTing
 * @create: 2020-05-12 18:02
 **/
public class RadioManage {

    private static final RadioManage RADIO_MANAGE = new RadioManage();
    private String radioIp;

    private Integer radioPort;

    private String radioProject;

    private RadioManage(){

    }

    private RadioManage(String sectionName){

    }

    public static RadioManage getInstance(){
        return RadioManageFactory.radioManage;
    }

    private static class RadioManageFactory {
        private static RadioManage radioManage = new RadioManage("radio_address_");
    }

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
