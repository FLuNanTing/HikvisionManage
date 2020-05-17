package com.hikvision.hikvisionmanage.ledscreen.ledscreenutil;

import com.hikvision.hikvisionmanage.utils.LoggerUtil;

/**
 * @program: HikvisionManage
 * @description: LED工具设置
 * @author: LuNanTing
 * @create: 2020-05-15 17:56
 **/
public class LedScreenUtil {

    /**
     * 根据放行状态返回提示信息
     *
     * @param release
     * @return
     */
    public static String releaseToMessage(Integer release) {
        String messageBytes = null;
        switch (release) {
            case 0:
                messageBytes = "一路顺风";
                break;
            case 2:
                messageBytes = "请联系接待您的服务顾问交车";
                break;
            case 3:
                messageBytes = "请联系安保门卫放行";
                break;
            case 4:
                messageBytes = "请联系安保门卫放行";
                break;
            default:
                messageBytes = "请联系安保门卫放行";
                break;
        }
        LoggerUtil.info(messageBytes);
        return messageBytes;
    }

    /**
     * 根据放行状态返回出入放行信息
     *
     * @param release
     * @return
     */
    public static String releaseToResultBytes(Integer release) {
        String resultBytes = null;
        switch (release) {
            case 0:
                resultBytes = "一路顺风";
                break;
            case 2:
                resultBytes = "禁止出厂";
                break;
            case 3:
                resultBytes = "禁止出厂";
                break;
            case 4:
                resultBytes = "禁止出厂";
                break;
            default:
                resultBytes = "禁止出厂";
                break;
        }
        LoggerUtil.info(resultBytes);
        return resultBytes;
    }

    /**
     * 设置播放速度
     *
     * @param item
     * @return
     */
    public static byte cbxIndexToSpeed(byte item) {
        // 此速度表仅供参考，更多请查阅协议文档
        byte[] speed_example_list = {10, 30, 45};
        byte speed_value = 10;

        if (item > 2) {
            item = 2;
        }
        speed_value = speed_example_list[item];
        return speed_value;
    }
}
