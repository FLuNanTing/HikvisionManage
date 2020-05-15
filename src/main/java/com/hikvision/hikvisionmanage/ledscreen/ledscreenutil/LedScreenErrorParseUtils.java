package com.hikvision.hikvisionmanage.ledscreen.ledscreenutil;

import com.hikvision.hikvisionmanage.utils.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: LED异常解析工具
 * @author: LuNanTing
 * @create: 2020-05-15 18:44
 **/
public class LedScreenErrorParseUtils {

    public static Map<String, Object> parseErrorCode(int errorCode, String ipAddress) {
        Map<String, Object> map = new HashMap<>(4);
        if (errorCode == 0) {
            map.put("code", 0);
            map.put("errorMessage", "ip:" + ipAddress + "LED屏发送成功");
        } else {
            String message;
            switch (errorCode) {
                case -1:
                    message = "设备参数未设置 ";
                    break;
                case -2:
                    message = "节目 ID 号错误";
                    break;
                case -3:
                    message = "节目未初始化";
                    break;
                case -4:
                    message = "区域 ID 号错误";
                    break;
                case -5:
                    message = "区域坐标设置错误";
                    break;
                case -6:
                    message = "颜色设置错误";
                    break;
                case -7:
                    message = "动作方式设置错误";
                    break;
                case -8:
                    message = "字体设置错误 ";
                    break;
                case -9:
                    message = "一个节目中只能含有一条语音 ";
                    break;
                case -10:
                    message = "数据长度设置错误 ";
                    break;
                case -11:
                    message = "系统缓存错误 ";
                    break;
                case -12:
                    message = "协议数据帧标志错误 ";
                    break;
                case -13:
                    message = "协议数据帧长度错误";
                    break;
                case -14:
                    message = "指令错误 ";
                    break;
                default:
                    message = "异常编码错误";
                    break;
            }
            map.put("code", errorCode);
            map.put("errorMessage", message);
        }
        LoggerUtil.info(ipAddress + " : " + map.get("errorMessage").toString());
        return map;
    }
}