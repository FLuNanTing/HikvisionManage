package com.hikvision.hikvisionmanage.devicemanage.bo;

import com.alibaba.fastjson.JSON;
import com.hikvision.hikvisionmanage.common.systemsetting.DataValidateFailException;
import com.hikvision.hikvisionmanage.utils.ReadConfigurationUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: 服务管理中心
 * @author: LuNanTing
 * @create: 2020-05-12 17:31
 **/
public class LocalServiceManage {

    private static LocalServiceManage LOCAL_SERVICE_MANAGE = new LocalServiceManage();

    private String localIp;

    private Integer localPort;

    private String localProject;

    private Integer localCode;

    private List<RadioServiceManage> radioServerList ;

    /**
     * 无参构造函数
     */
    private LocalServiceManage(){

    }

    private LocalServiceManage(String sectionName){
        if(LOCAL_SERVICE_MANAGE.isEmpty()){
            Map<String, Object> readIniConfigurationParameter = ReadConfigurationUtil.readIniConfigurationParameter();
            LOCAL_SERVICE_MANAGE.setLocalIp(readIniConfigurationParameter.get("hikvisionServiceAddress").toString());
            LOCAL_SERVICE_MANAGE.setLocalPort(Integer.valueOf(readIniConfigurationParameter.get("hikvisionServicePort").toString()));
            LOCAL_SERVICE_MANAGE.setLocalProject(readIniConfigurationParameter.get("hikvisionServiceProject").toString());
            LOCAL_SERVICE_MANAGE.setLocalCode(Integer.valueOf(readIniConfigurationParameter.get("hikvisionServiceCode").toString()));
            String radioServerListStr = readIniConfigurationParameter.get("radioServerList").toString();
            List<String> radioServerList = JSON.parseObject(radioServerListStr,List.class);
            List<RadioServiceManage> radioServiceManageList = new ArrayList<>();
            radioServerList.forEach(radioServerStr -> {
                RadioServiceManage radioServiceManage = new RadioServiceManage();
                Map<String, Object> radioServerMap = JSON.parseObject(radioServerStr, Map.class);
                radioServiceManage.setRadioIp(radioServerMap.get("radioServiceAddress").toString());
                radioServiceManage.setRadioPort(Integer.valueOf(radioServerMap.get("radioServicePort").toString()));
                radioServiceManage.setRadioProject(radioServerMap.get("radioServiceProject").toString());
                radioServiceManageList.add(radioServiceManage);
            });
            if(radioServiceManageList == null || radioServerList.size()<1){
                throw new DataValidateFailException("系统未配置广播服务器");
            }else {
                LOCAL_SERVICE_MANAGE.setRadioServerList(radioServiceManageList);
            }
        }
    }

    public static LocalServiceManage getInstance(){
        return LocalServiceManage.LocalServiceManageFactory.localServiceManage;
    }

    private static class LocalServiceManageFactory{
        private static LocalServiceManage localServiceManage = new LocalServiceManage("hikvision_manage.ini");
    }


    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public String getLocalProject() {
        return localProject;
    }

    public void setLocalProject(String localProject) {
        this.localProject = localProject;
    }

    public Integer getLocalCode() {
        return localCode;
    }

    public void setLocalCode(Integer localCode) {
        this.localCode = localCode;
    }

    public List<RadioServiceManage> getRadioServerList() {
        return radioServerList;
    }

    public void setRadioServerList(List<RadioServiceManage> radioServerList) {
        this.radioServerList = radioServerList;
    }

    public Boolean isEmpty(){
        Boolean empty = false;
        if(LOCAL_SERVICE_MANAGE == null){
            empty = true;
        }else if(StringUtils.isEmpty(localIp)){
            empty = true;
        }else if(StringUtils.isEmpty(localPort)){
            empty = true;
        }else if(StringUtils.isEmpty(localProject)){
            empty = true;
        }else if(radioServerList==null||radioServerList.size()==0) {
            empty = true;
        }else{
            empty = false;
        }
        return empty;
    }
}
