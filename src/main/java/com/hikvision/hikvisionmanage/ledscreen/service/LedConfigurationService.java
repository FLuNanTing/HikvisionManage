package com.hikvision.hikvisionmanage.ledscreen.service;

import com.hikvision.hikvisionmanage.devicemanage.service.CentralCoreService;

import java.util.Map;

public interface LedConfigurationService extends CentralCoreService {

    /**
     * 显示屏宽度(像素)
     */
    Integer nWidth = 64;

    /**
     * 显示屏高度(像素)
     */
    Integer nHeight = 64;

    /**
     * 控制LED生成文字
     * @param ipAddress:地址
     * @param port:端口
     * @param picNo:车牌
     * @param release:显示信息状态
     * @return
     */
    Map<String, Object> textControl(String ipAddress, Integer port,String picNo,Integer release);

    /**
     * 控制LED生成语音
     * @param ipAddress
     * @param port
     * @param picNo
     * @param release
     * @return
     */
    Map<String, Object> soundControl(String ipAddress, Integer port,String picNo,Integer release);
}
