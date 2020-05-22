package com.hikvision.hikvisionmanage.common.bean;

import com.alibaba.fastjson.JSON;
import com.hikvision.hikvisionmanage.devicemanage.bo.LedScreenManage;
import com.hikvision.hikvisionmanage.devicemanage.bo.VidiconManage;
import com.hikvision.hikvisionmanage.utils.HttpClientUtil;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.utils.MasterUtils;
import com.hikvision.hikvisionmanage.utils.ReadConfigurationUtil;
import com.hikvision.hikvisionmanage.vidicon.service.impl.VidiconServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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

    @Autowired
    private ApplicationContext applicationContext;

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
        Integer communicationsMode = readConfigurationCommunicationsMode();
        if (communicationsMode.equals(0)) {
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
                            vidiconManage.setServicePort(Integer.valueOf(readIniConfigurationParameter.get("hikvisionServicePort").toString()));
                            vidiconManage.setVidiconModel(Integer.valueOf(requestData.get("vidiconModel").toString()));
                            vidiconManage.setDescription(requestData.get("description").toString());
                            vidiconManage.setDeviceIp(requestData.get("deviceIp").toString());
                            vidiconManage.setDevicePort(Integer.valueOf(requestData.get("devicePort").toString()));
                            Object deviceId = requestData.get("deviceId");
                            if (StringUtils.isEmpty(deviceId)) {
                                Map<String, Object> deviceInformationMap = (new VidiconServiceImpl()).getDeviceInformation(vidiconManage.getDeviceIp(), vidiconManage.getDevicePort().toString(), vidiconManage.getPassword(), null);
                                if (deviceInformationMap != null && deviceInformationMap.get("errorCode").equals(0)) {
                                    LoggerUtil.info(deviceInformationMap.toString());
                                    Map deviceInfo = JSON.parseObject(deviceInformationMap.get("data").toString(), Map.class);
                                    Map<String, Object> deviceInformation = deviceInfo;
                                    vidiconManage.setVidiconId(deviceInformation.get("deviceId").toString());
                                } else {
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

    /**
     * 将该服务下的LED屏纳入管理
     *
     * @return
     */
    @DependsOn("vidiconManageSetBean")
    @Bean
    public Set<LedScreenManage> ledScreenManageSetBean() {
        Set<LedScreenManage> ledScreenManageSet = new HashSet<>();
        Map<String, Object> ledScreen = ReadConfigurationUtil.readYamlMap("application.yml", "ledscreen");
        Integer communicationsMode = readConfigurationCommunicationsMode();
        if (communicationsMode.equals(0)) {
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
                        + radioServerMap.get("radioServiceProject").toString() + ledScreen.get("url");
                //获取摄像头Bean对象
                Object vidiconManageObjectBean = applicationContext.getBean("vidiconManageSetBean");
                if (vidiconManageObjectBean == null) {
                    vidiconManageObjectBean = vidiconManageSetBean();
                }
                Set<VidiconManage> vidiconManageSetBean = (Set<VidiconManage>) vidiconManageObjectBean;
                List<VidiconManage> setList = new ArrayList<>(vidiconManageSetBean);
                List<Map<String, Object>> responseList = new ArrayList<>();
                setList.forEach(vidiconManage -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("deviceIp", vidiconManage.getDeviceIp());
                    map.put("devicePort", vidiconManage.getDevicePort());
                    map.put("serviceIp", vidiconManage.getServiceIp());
                    map.put("servicePort", vidiconManage.getServicePort());
                    responseList.add(map);
                });
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("deviceList", JSON.toJSON(responseList));
                String requestMap = HttpClientUtil.submitPostRequestMap(url, responseMap);
                if (requestMap == null || StringUtils.isEmpty(requestMap)) {
                    LoggerUtil.error("未找到设备");
                } else {
                    //解析返回数据
                    Map<String, Object> requestData = JSON.parseObject(requestMap, Map.class);
                    Object requestForData = requestData.get("data");
                    if (requestForData != null) {
                        List<Map> requestDataList = JSON.parseArray(requestForData.toString(),Map.class);
                        requestDataList.forEach(str -> {
                            LedScreenManage ledScreenManage = new LedScreenManage();
                            Map<String, Object> requestDataMap = str;
                            ledScreenManage.setDeviceIp(requestDataMap.get("ledScreenIp").toString());
                            ledScreenManage.setDevicePort(Integer.valueOf(requestDataMap.get("ledScreenPort").toString()));
                            ledScreenManage.setDescription(requestDataMap.get("description").toString());
                            String vidiconIp = requestDataMap.get("vidiconIp").toString();
                            Optional<VidiconManage> optionalVidiconManage = setList.stream().filter(vidicon -> vidicon.getDeviceIp().equals(vidiconIp)).findAny();
                            if (optionalVidiconManage != null) {
                                ledScreenManage.setVidiconManage(optionalVidiconManage.get());
                            }
                            ledScreenManageSet.add(ledScreenManage);
                        });
                    }
                }
            });
        }
        return ledScreenManageSet;
    }

    /**
     * 读取YML配置文件中关于通信方式的内部方法
     *
     * @return mode:0HttpClient
     */
    private Integer readConfigurationCommunicationsMode() {
        Map<String, Object> communications = ReadConfigurationUtil.readYamlMap("application.yml", "communications");
        if (communications == null || communications.isEmpty()) {
            LoggerUtil.error("未配置服务通信类型");
            System.exit(0);
        }
        Object modeObject = communications.get("mode");
        if (!MasterUtils.checkIsNumber(modeObject)) {
            LoggerUtil.error("通信类型未配置或配置错误");
        }
        Integer communicationsMode = Integer.valueOf(communications.get("mode").toString());
        return communicationsMode;
    }
}
