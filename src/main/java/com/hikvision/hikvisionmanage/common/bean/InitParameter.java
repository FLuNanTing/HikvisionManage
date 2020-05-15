package com.hikvision.hikvisionmanage.common.bean;

import com.alibaba.fastjson.JSON;
import com.hikvision.hikvisionmanage.devicemanage.bo.VidiconManage;
import com.hikvision.hikvisionmanage.utils.HttpClientUtil;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.utils.ReadConfigurationUtil;
import com.hikvision.hikvisionmanage.vidicon.service.VidiconService;
import com.sun.jna.NativeLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @program: HikvisionManage
 * @description: 系统初始化基本参数设置
 * @author: LuNanTing
 * @create: 2020-05-11 14:55
 **/
@Configuration
public class InitParameter {

    /**
     * 报警布防句柄
     */
    NativeLong lAlarmHandle;

    @Autowired
    private VidiconService vidiconService;

    /**
     * 将配置信息容器管理
     *
     * @return configurationParameter
     */
    @Bean
    public Map<String, Object> configurationParameter() {
        Map<String, Object> configurationParameter = ReadConfigurationUtil.readIniConfigurationParameter();
        return configurationParameter;
    }

    /**
     * 将摄像头的参数纳入set集中，并加入bean容器
     *
     * @return
     */
    @Bean
    public Set<VidiconManage> vidiconManageSetBean() {
        Set<VidiconManage> vidiconManageSet = new HashSet<>();
        Map<String, Object> vidicon = ReadConfigurationUtil.readYamlMap("application.yml", "vidicon");
        Integer getVidiconMode = Integer.valueOf(vidicon.get("mode").toString());
        if (getVidiconMode.equals(0)) {
            //HttpClient获取
            Map<String, Object> readIniConfigurationParameter = ReadConfigurationUtil.readIniConfigurationParameter();
            //获取广播服务群
            List<Map<String, Object>> radioServerList = (List<Map<String, Object>>) readIniConfigurationParameter.get("radioServerList");
            radioServerList.forEach(radioServerStr -> {
                Map<String, Object> radioServerMap = radioServerStr;
                String radioServicePort = radioServerMap.get("radioServicePort").toString();
                String scheme;
                if ("443".equals(radioServicePort)) {
                    scheme = "https://";
                } else {
                    scheme = "http://";
                }
                String url = scheme + radioServerMap.get("radioServiceAddress").toString() + ":" + radioServicePort
                        + radioServerMap.get("radioServiceProject").toString() + vidicon.get("url");
                Map<String, Object> responseMap = readIniConfigurationParameter;
                responseMap.remove("radioServerList");
                String requestMap = HttpClientUtil.submitPostRequestMap(url, responseMap);
                if (requestMap == null) {
                    LoggerUtil.error("未找到设备");
                } else {
                    //解析返回数据
                    Map<String, Object> requestDataMap = JSON.parseObject(requestMap, Map.class);
                    List<Map> requestDataList = JSON.parseArray(requestDataMap.get("data").toString(), Map.class);
                    if (requestDataList != null && requestDataList.size() > 0) {
                        requestDataList.forEach(str -> {
                            VidiconManage vidiconManage = new VidiconManage();
                            Map<String, Object> requestData = str;
                            vidiconManage.setCompanyCode(requestData.get("companyCode").toString());
                            vidiconManage.setPassword(requestData.get("password").toString());
                            vidiconManage.setServiceIp(readIniConfigurationParameter.get("hikvisionServiceAddress").toString());
                            vidiconManage.setVidiconModel(Integer.valueOf(requestData.get("vidiconModel").toString()));
                            vidiconManage.setDescription(requestData.get("description").toString());
                            vidiconManage.setDeviceIp(requestData.get("deviceIp").toString());
                            vidiconManage.setDevicePort(Integer.valueOf(requestData.get("devicePort").toString()));
                            Object deviceId = requestData.get("deviceId");
                            if(StringUtils.isEmpty(deviceId)){
                                Map<String , Object> deviceInformationMap = vidiconService.getDeviceInformation(vidiconManage.getDeviceIp(),vidiconManage.getDevicePort().toString(),vidiconManage.getPassword(),null);
                                if(deviceInformationMap!=null&&deviceInformationMap.get("errorCode").equals(0)){
                                    Map deviceInfo = JSON.parseObject(deviceInformationMap.get("data").toString(),Map.class);
                                    Map<String , Object> deviceInformation = deviceInfo;
                                    vidiconManage.setVidiconId(deviceInformation.get("deviceId").toString());
                                }else{
                                    LoggerUtil.error(vidiconManage.getDescription() + ":" + deviceInformationMap.get("errorMessage").toString());
                                }
                            }
                            vidiconManageSet.add(vidiconManage);
                        });
                    }
                }
            });
        }
        if (vidiconManageSet == null || vidiconManageSet.isEmpty()) {
            LoggerUtil.error("未找到设备");
            System.exit(0);
        }
        return vidiconManageSet;
    }
}
