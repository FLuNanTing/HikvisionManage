package com.hikvision.hikvisionmanage.ledscreen.service.impl;

import com.hikvision.hikvisionmanage.core.sdk.CSDKExport;
import com.hikvision.hikvisionmanage.ledscreen.ledscreenutil.AreaInformation;
import com.hikvision.hikvisionmanage.ledscreen.ledscreenutil.LedScreenConfigurationAction;
import com.hikvision.hikvisionmanage.ledscreen.ledscreenutil.LedScreenUtil;
import com.hikvision.hikvisionmanage.ledscreen.service.LedConfigurationService;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.hikvision.hikvisionmanage.utils.SocketUtil;
import com.sun.jna.NativeLong;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: led控制Service
 * @author: LuNanTing
 * @create: 2020-05-15 17:37
 **/
@Service
public class LedConfigurationServiceImpl implements LedConfigurationService {

    /**
     * 控制LED生成文字
     *
     * @param ipAddress :地址
     * @param port      :端口
     * @param picNo     :车牌
     * @param release   :显示信息状态
     * @return
     */
    @Override
    public Map<String, Object> textControl(String ipAddress, Integer port, String picNo, Integer release) {
        LoggerUtil.info("车牌:" + picNo);
        Map<String, Object> map ;
        byte[] picNoBytes = null;
        byte[] messageBytes = null;
        byte[] resultBytes = null;
        try {
            picNoBytes = (picNo).getBytes("GBK");
            messageBytes = LedScreenUtil.releaseToMessage(release).getBytes("GBK");
            resultBytes = LedScreenUtil.releaseToResultBytes(release).getBytes("GBK");
        } catch (Exception e) {
            LoggerUtil.error("文字信息生成失败");
            e.printStackTrace();
        }
        byte program = 1, area = 1;
        int ret ;
        ret = sdkExport.vtInitialize(nWidth, nHeight, (byte) CSDKExport.VT_DIPLAY_COLOR.VT_SIGNLE_COLOR, (byte) 0);
        // 添加节目
        ret = sdkExport.vtAddProgram(program);
        // 添加时间
        ret = vtAddTimeAreaItem((byte) 0, program, area);
        if (0 == ret) {
            area++;
        }
        // 添加文本(第二行)
        ret = LedScreenConfigurationAction.addTextArea((byte) 1, program, area, picNoBytes);
        if (0 == ret) {
            area++;
        }
        // 添加文本(第三行)
        ret = LedScreenConfigurationAction.addTextArea((byte) 2, program, area, messageBytes);
        if (0 == ret) {
            area++;
        }
        // 添加文本(第四行)
        ret = LedScreenConfigurationAction.addTextArea((byte) 3, program, area, resultBytes);
        if (0 == ret) {
            area++;
        }
        // 添加节目
        ret = sdkExport.vtAddProgram((byte) (program + 1));
        ret = LedScreenConfigurationAction.addSoundArea(program, area, messageBytes);
        map = SocketUtil.socketClient(ipAddress, port, ret);
        return map;
    }

    /**
     * 控制LED生成语音
     *
     * @param ipAddress
     * @param port
     * @param picNo
     * @param release
     * @return
     */
    @Override
    public Map<String, Object> soundControl(String ipAddress, Integer port, String picNo, Integer release) {
        return null;
    }

    /**
     * {登录设备}
     *
     * @param deviceIp
     * @param devicePort
     * @param userName
     * @param password
     * @return 返回类型：NativeLong
     * @author 创建人: LuNanTing
     * @date 时间： 2019年5月29日
     */
    @Override
    public NativeLong loginDevice(String deviceIp, int devicePort, String userName, String password) {
        return null;
    }

    /**
     * 添加时间区域显示
     *
     * @param item
     * @param nProgramID
     * @param nAreaID
     * @return
     */
    private static int vtAddTimeAreaItem(byte item, byte nProgramID, byte nAreaID) {
        byte[] strTimeBytes = null;
        try {
            // strTimeBytes = sdf.format(date).getBytes("GBK");
            strTimeBytes = "%Y4-%M3-%D2 %h0:%m:%s".getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            LoggerUtil.error("日期生成失败");
            e.printStackTrace();
        }
        AreaInformation areaInfo;
        int ret ;
        areaInfo = LedScreenConfigurationAction.GetAreaInfo(item, strTimeBytes);
        if (areaInfo.flag) {
            ret = sdkExport.vtAddTimeAreaItem(nProgramID, nAreaID, areaInfo.x, areaInfo.y, areaInfo.w, areaInfo.h, (byte) CSDKExport.VT_TIME_TYPE.VT_TIME_TYPE_CALENDAR.getTimeType(),
                    (byte) CSDKExport.VT_HOUR_TYPE.VT_HOUR_TYPE_24H.getHourType(), (byte) CSDKExport.VT_TIMEZONE_TYPE.VT_TIMEZONE_TYPE_BEIJING.getTimeType(), (byte) 0, (byte) 0, strTimeBytes,
                    strTimeBytes.length, areaInfo.color.getColorType(), (byte) areaInfo.action.getActionType(), (byte) areaInfo.font.getFontType(), (byte) 60, (byte) 255);
        } else {
            ret = -1;
        }
        return ret;
    }


}
