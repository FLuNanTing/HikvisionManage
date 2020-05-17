package com.hikvision.hikvisionmanage.ledscreen.ledscreenutil;

import com.hikvision.hikvisionmanage.core.sdk.CSDKExport;

/**
 * @program: HikvisionManage
 * @description: led参数设置中心
 * @author: LuNanTing
 * @create: 2020-05-15 18:27
 **/
public class LedScreenConfigurationAction {

    static CSDKExport csdkExport = CSDKExport.INSTANCE;
    /**
     * 添加文本区域
     *
     * @param item:行数
     * @param nProgramID
     * @param nAreaID
     * @return
     */
    public static int addTextArea(byte item, byte nProgramID, byte nAreaID, byte[] messageByte) {
        AreaInformation AreaInfo ;
        int ret;
        // 获取本行的区域信息
        AreaInfo = GetAreaInfo(item, messageByte);
        if (AreaInfo.flag == true) {
            ret = csdkExport.vtAddTextAreaItem(nProgramID, nAreaID,
                    AreaInfo.x, AreaInfo.y, AreaInfo.w, AreaInfo.h,
                    AreaInfo.byteText, AreaInfo.byteText.length,
                    AreaInfo.color.getColorType(),
                    (byte) AreaInfo.action.getActionType(),
                    (byte) AreaInfo.font.getFontType(),
                    AreaInfo.speed,
                    // 分页停留时间 可以自己定义 参见协议文档
                    (byte) 20);
        } else {
            return -1;
        }
        return ret;
    }

    public static int addSoundArea(byte nProgramID, byte nAreaID, byte[] messageByte) {
        AreaInformation AreaInfo ;
        int ret = 0;
        AreaInfo = GetAreaInfo((byte) 4, messageByte);
        if (AreaInfo.flag == true) {
            ret = csdkExport.vtAddSoundItem(nProgramID, nAreaID, AreaInfo.SoundPerson, AreaInfo.SoundVolume, AreaInfo.SoundSpeed, AreaInfo.byteText, AreaInfo.byteText.length);
        } else {
            return -1;
        }
        return ret;
    }

    /**
     * 设置区域参数
     *
     * @param item
     * @return
     */
    public static AreaInformation GetAreaInfo(byte item, byte[] noteStr) {
        AreaInformation area = new AreaInformation();
        switch (item) {
            case 0 :
                area.flag = true;
                area.x = 0;
                area.y = 0;
                area.w = 64;
                area.h = 16;
                // 颜色
                area.color = CSDKExport.VT_COLOR_TYPE.VT_COLOR_RED;
                // 动作
                area.action = CSDKExport.VT_ACTION_TYPE.VT_ACTION_CLEFT;
                // 字体
                area.font = CSDKExport.VT_FONT_TYPE.VT_FONT_16;
                // 速度
                area.speed = LedScreenUtil.cbxIndexToSpeed(item);
                area.byteText = noteStr;
                break;
            case 1 :
                area.flag = true;
                area.x = 0;
                area.y = 16;
                area.w = 64;
                area.h = 16;
                area.speed = LedScreenUtil.cbxIndexToSpeed(item);
                area.color = CSDKExport.VT_COLOR_TYPE.VT_COLOR_YELLOW;
                area.action = CSDKExport.VT_ACTION_TYPE.VT_ACTION_HOLD;
                area.font = CSDKExport.VT_FONT_TYPE.VT_FONT_16;
                area.byteText = noteStr;
                break;
            case 2 :
                area.flag = true;
                area.x = 0;
                area.y = 32;
                area.w = 64;
                area.h = 16;
                area.speed = LedScreenUtil.cbxIndexToSpeed(item);
                area.color = CSDKExport.VT_COLOR_TYPE.VT_COLOR_GREEN;
                area.action = CSDKExport.VT_ACTION_TYPE.VT_ACTION_CLEFT;
                area.font = CSDKExport.VT_FONT_TYPE.VT_FONT_16;
                area.byteText = noteStr;
                break;
            case 3 :
                area.flag = true;
                area.x = 0;
                area.y = 48;
                area.w = 64;
                area.h = 16;
                area.speed = LedScreenUtil.cbxIndexToSpeed(item);
                area.color = CSDKExport.VT_COLOR_TYPE.VT_COLOR_RED;
                area.action = CSDKExport.VT_ACTION_TYPE.VT_ACTION_HOLD;
                area.font = CSDKExport.VT_FONT_TYPE.VT_FONT_16;
                area.byteText = noteStr;
                break;
            case 4 :
                area.flag = true;
                area.SoundPerson = (byte) 0;
                area.SoundVolume = (byte) 10;
                area.SoundSpeed = (byte) 50;
                area.byteText = noteStr;
                break;
            default :
                area.flag = false;
                break;
        }
        return area;
    }

}
