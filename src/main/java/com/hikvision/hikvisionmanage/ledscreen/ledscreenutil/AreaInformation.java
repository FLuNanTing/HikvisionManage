package com.hikvision.hikvisionmanage.ledscreen.ledscreenutil;

import com.hikvision.hikvisionmanage.core.sdk.CSDKExport;

/**
 * @program: HikvisionManage
 * @description: LED区域属性设置
 * @author: LuNanTing
 * @create: 2020-05-15 18:12
 **/
public class AreaInformation {

    /**
    * 标志
    */
    public boolean  flag;

    /**
     * x坐标
     */
    public int x;

    /**
     * y坐标
     */
    public int y;

    /**
     * 区域宽度
     */
    public int w;

    /**
     * 区域高度
     */
    public int h;

    /**
     * 颜色
     */
    public CSDKExport.VT_COLOR_TYPE color;

    /**
     * 动作方式
     */
    public CSDKExport.VT_ACTION_TYPE action;

    /**
     * 字体
     */
    public CSDKExport.VT_FONT_TYPE font;

    /**
     * 速度
     */
    public byte speed;

    /**
     * 发音人
     */
    public byte SoundPerson;

    /**
     * 音量
     */
    public byte SoundVolume;

    /**
     * 语速
     */
    public byte SoundSpeed;

    /**
     * 文本内容
     */
    public byte[] byteText;
}
