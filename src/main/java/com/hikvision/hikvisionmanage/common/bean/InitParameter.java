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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Bean
    public List<VidiconManage> vidiconManageListBean() {
        List<VidiconManage> list = new ArrayList<>();
        Map<String, Object> vidicon = ReadConfigurationUtil.readYamlMap("application.yml", "vidicon");
        Integer getvidiconMode = Integer.valueOf(vidicon.get("mode").toString());
        if (getvidiconMode.equals(0)) {
            //HttpClient获取
            Map<String, Object> readIniConfigurationParameter = ReadConfigurationUtil.readIniConfigurationParameter();
            String radioServicePort = readIniConfigurationParameter.get("radioServicePort").toString();
            String scheme;
            if ("443".equals(radioServicePort)) {
                scheme = "https://";
            } else {
                scheme = "http://";
            }
            String url = scheme + readIniConfigurationParameter.get("radioServiceAddress").toString() + radioServicePort + readIniConfigurationParameter.get("radioServiceProject").toString() + vidicon.get("url");
            String requestMap = HttpClientUtil.submitPostRequestMap(url, readIniConfigurationParameter);
            if(requestMap == null){
                LoggerUtil.error("未找到设备信息");
                System.exit(0);
            }
            //解析返回数据
            List<String> requestDataList = JSON.parseObject(requestMap,List.class);
            if(requestDataList!=null&&requestDataList.size()>0){
                requestDataList.forEach(str ->{
                    VidiconManage vidiconManage = new VidiconManage();
                    Map<String , Object> requestData = JSON.parseObject(str, Map.class);
                    vidiconManage.setCompanyCode(requestData.get("companyCode").toString());
                    vidiconManage.setPassword(requestData.get("password").toString());
                    vidiconManage.setServiceIp(readIniConfigurationParameter.get("hikvisionServiceAddress").toString());
                    vidiconManage.setVidiconModel(Integer.valueOf(requestData.get("vidiconModel").toString()));
                    vidiconManage.setDescription(requestData.get("description").toString());
                    vidiconManage.setDeviceIp(requestData.get("deviceIp").toString());
                    vidiconManage.setDevicePort(Integer.valueOf(requestData.get("devicePort").toString()));
                    list.add(vidiconManage);
                });
            }
        }
        return list;
    }
}
