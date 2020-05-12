package com.hikvision.hikvisionmanage.utils;

import com.alibaba.fastjson.JSON;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: 读取INI配置文件
 * @author: LuNanTing
 * @create: 2020-05-11 15:42
 **/
public class ReadConfigurationUtil {

    /**
     * 读取本地ini环境配置
     * @return
     */
    public static Map<String, Object> readIniConfigurationParameter() {
        File file;
        Map<String , Object> map = new HashMap<>();
        try {
            file = ResourceUtils.getFile("classpath:hikvision_manage.ini");
            Wini ini = new Wini(file);
            //获取配置文件中本地设置的地址
            Section localAddressSection = ini.get("local_address");
            //获取配置文件中广播服务的地址
            Section radioAddressSection = ini.get("radio_address");
            map.put("hikvisionServiceAddress",localAddressSection.get("local_ip"));
            map.put("hikvisionServicePort",localAddressSection.get("local_port"));
            map.put("hikvisionServiceProject",localAddressSection.get("local_project"));
            map.put("hikvisionServiceCode",localAddressSection.get("local_code"));
            map.put("radioServiceAddress",radioAddressSection.get("radio_ip"));
            map.put("radioServicePort",radioAddressSection.get("radio_port"));
            map.put("radioServiceProject",radioAddressSection.get("local_project"));
        } catch (FileNotFoundException e) {
            LoggerUtil.error("配置文件未找到");
        } catch (IOException e) {
            LoggerUtil.error("配置文件出错");
        }
        return map;
    }

    public static Map<String, Object> readYamlMap(java.lang.String yml, java.lang.String keys) {
        Map<String , Object> temp = new HashMap<>();
        Map<String, Object> temps = readYml(yml);
        for (Map.Entry<String, Object> entry : temps.entrySet()) {
            temp.put(entry.getKey(), entry.getValue());
        }
        String[] ks = keys.split("\\.");
        for (int i = 0; i < ks.length; i++) {
            String k = ks[i];
            temp = (Map<String, Object>) temp.get(k);
            if (i == ks.length - 1) {
                return temp;
            }
        }
        return new HashMap<String, Object>();

    }

    public static Map<String, Object> readYml(String yml) {
        Map<String, Object> data = new HashMap<String, Object>();
        InputStream is = null;
        InputStreamReader in = null;
        try {
            is = ReadConfigurationUtil.class.getClassLoader().getResourceAsStream(yml);
            in = new InputStreamReader(is, "UTF-8");
            Yaml yaml = new Yaml();
            data = (Map<String, Object>) yaml.load(is);
        } catch (IOException e) {
            LoggerUtil.error(e);
        } finally {
            try {
                in.close();
                is.close();
            } catch (IOException e) {
                LoggerUtil.error(e);
            }
        }
        return data;
    }
}
