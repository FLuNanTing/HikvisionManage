package com.hikvision.hikvisionmanage.common.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hikvision.hikvisionmanage.core.sdk.HCNetSDK;
import com.hikvision.hikvisionmanage.devicemanage.bo.VidiconManage;
import com.hikvision.hikvisionmanage.utils.HttpClientUtil;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.utils.ReadConfigurationUtil;
import com.sun.jna.NativeLong;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.logging.Logger;

/**
 * @program: HikvisionManage
 * @description: 系统初始化基本参数设置
 * @author: LuNanTing
 * @create: 2020-05-11 14:55
 **/
@Configuration
public class InitParameter {

    static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;

    /**
     * 报警布防句柄
     */
    NativeLong lAlarmHandle;

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
            List<Map<String,Object>> radioServerList = (List<Map<String, Object>>) readIniConfigurationParameter.get("radioServerList");
//            List<Map> radioServerList = JSON.parseArray(readIniConfigurationParameter.get("radioServerList").toString(), Map.class);
            radioServerList.forEach(radioServerStr -> {
//                Map<String, Object> radioServerMap = JSON.parseObject(radioServerStr, Map.class);
                Map<String , Object> radioServerMap = radioServerStr;
                String radioServicePort = radioServerMap.get("radioServicePort").toString();
                String scheme;
                if ("443".equals(radioServicePort)) {
                    scheme = "https://";
                } else {
                    scheme = "http://";
                }
                String url = scheme + radioServerMap.get("radioServiceAddress").toString()+":" + radioServicePort
                        + radioServerMap.get("radioServiceProject").toString() + vidicon.get("url");
                Map<String, Object> responseMap = readIniConfigurationParameter;
                responseMap.remove("radioServerList");
                String requestMap = HttpClientUtil.submitPostRequestMap(url, responseMap);
                if (requestMap != null) {
                    LoggerUtil.error("未找到设备");
                } else {
                    //解析返回数据
                    List<String> requestDataList = JSON.parseObject(requestMap, List.class);
                    if (requestDataList != null && requestDataList.size() > 0) {
                        requestDataList.forEach(str -> {
                            VidiconManage vidiconManage = new VidiconManage();
                            Map<String, Object> requestData = JSON.parseObject(str, Map.class);
                            vidiconManage.setCompanyCode(requestData.get("companyCode").toString());
                            vidiconManage.setPassword(requestData.get("password").toString());
                            vidiconManage.setServiceIp(readIniConfigurationParameter.get("hikvisionServiceAddress").toString());
                            vidiconManage.setVidiconModel(Integer.valueOf(requestData.get("vidiconModel").toString()));
                            vidiconManage.setDescription(requestData.get("description").toString());
                            vidiconManage.setDeviceIp(requestData.get("deviceIp").toString());
                            vidiconManage.setDevicePort(Integer.valueOf(requestData.get("devicePort").toString()));
                            vidiconManageSet.add(vidiconManage);
                        });
                    }
                }
            });
        }
        if(vidiconManageSet==null||vidiconManageSet.isEmpty()){
            LoggerUtil.error("设备未找到");
            System.exit(0);
        }
        return vidiconManageSet;
    }
}
