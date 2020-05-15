package com.hikvision.hikvisionmanage.ledscreen.controller;

import com.alibaba.fastjson.JSON;
import com.hikvision.hikvisionmanage.ledscreen.service.LedConfigurationService;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: LED屏控制
 * @author: LuNanTing
 * @create: 2020-05-15 16:56
 **/
@RestController
@RequestMapping("ledConfiguration")
public class LedConfigurationController {

    @Autowired
    private LedConfigurationService ledConfigurationService;
    /**
     *
     * @param bean
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("textOperation")
    public Map<String, Object> textOperation(String bean) {
        long dateTime = System.currentTimeMillis();
        Map<String, Object> ledInfo = JSON.parseObject(bean, Map.class);
        Map<String, Object> map = new HashMap<>();
        if (ledInfo == null || ledInfo.isEmpty()) {
            map.put("code", -2001);
            map.put("errorMessage", "请求参数不能为空");
            map.put("data", null);
            return map;
        }
        String data = ledInfo.get("data").toString();
        String deviceIp = ledInfo.get("deviceIp").toString();
        String devicePortStr = ledInfo.get("devicePort").toString();
        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(deviceIp) || StringUtils.isEmpty(devicePortStr)) {
            map.put("code", -2002);
            map.put("errorMessage", "请求参数不正确或不存在");
            map.put("data", null);
            return map;
        }
        Integer devicePort = Integer.valueOf(devicePortStr);
        Map<String, Object> dataMap = JSON.parseObject(data, Map.class);
        String picNo = dataMap.get("picNo").toString();
        Integer release = Integer.valueOf(dataMap.get("release").toString());
        if (StringUtils.isEmpty(picNo)) {
            map.put("code", -2001);
            map.put("errorMessage", "请求参数不能为空");
            map.put("data", null);
            return map;
        }
        // 文字
        map = ledConfigurationService.textControl(deviceIp,devicePort,picNo, release );
        Long timeConsuming = System.currentTimeMillis() - dateTime;
        LoggerUtil.info("生成文字耗时:" + timeConsuming + "ms");
        // 语音
        ledConfigurationService.soundControl(picNo, release, deviceIp, devicePort);
        LoggerUtil.info("语音生成耗时:" + (System.currentTimeMillis() - dateTime - timeConsuming) + "ms");
        LoggerUtil.info("全程消耗:" +(System.currentTimeMillis() - (ledInfo.get("date")==null?0L:Long.valueOf(ledInfo.get("date").toString()))) + "ms");
        return map;
    }
}
