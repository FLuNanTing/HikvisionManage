/**
 * Copyright by LuNanTing 2019 DaChang Inc.
 * Date:2019年7月23日下午3:28:42
 * @author : LuNanTing
 */
package com.hikvision.hikvisionmanage.core.sdk;

import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @{LEDSDK}
 * @author 创建人: LuNanTing
 * @Title 标 题: LEDSDK.java
 * @update 修改人：LuNanTing
 * @date 修改事件：2019年7月23日
 */
public interface CSDKExport extends StdCallLibrary {

	public static String filePath() {

		String file = CSDKExport.class.getResource("/").getPath() + "static/vtLEDProtocol";
		file = file.substring(1, file.length());
		LoggerUtil.info("CSDKExport地址为:" + file);
		return file;
	}

	 CSDKExport INSTANCE = (CSDKExport) Native.loadLibrary(filePath(),CSDKExport.class);

	/*** 帧定义 ***/
	/** FH:帧头 */
//	public static final byte[] FH = {(byte) 0x55, (byte) 0xaa, 0x00, 0x00};

	/** address:地址 */
//	public static final byte ADDRESS = (byte) 0x01;

	/** RESERVE:保留 */
//	public static final byte RESERVE = (byte) 0x00;

	// 操作码
	/** SENDDISPLAYINFO:发送显示数据 */
//	public static final byte SENDDISPLAYINFO = (byte) 0xDA;

	/** INSTANTMESSAGE:即时信息操作码 */
//	public static final byte INSTANTMESSAGE = (byte) 0xDB;

	/** FRAMESEQ:帧序号 */
//	public static final short FRAMESEQ = 0000;

	/** INFOLENTH:数据长度 */
//	public static final int INFOLENTH = 0;

	/** FR:帧尾 */
//	public static final byte[] FR = {0X00, 0x00, 0x0d, 0x0a};

	/** MAX_BUFFER_SIZE:数据 */
//	public static final byte[] MAX_BUFFER_SIZE = new byte[512];

	int vt_ProtocolAnalyze(byte[] data, long size, byte[] pOut, long pnLen);

	/**
	 * SDK节目初始化
	 * @param nWidth
	 * @param nHeight
	 * @param nColor
	 * @param nCardType
	 * @return int
 	 */
	int vtInitialize(int nWidth, int nHeight, byte nColor, byte nCardType);

	int vtUninitialize();

	/**
	 * 添加节目
	 * @param nProgramID
	 * @return int
 	 */
	int vtAddProgram(byte nProgramID);

    /**
     * 获取节目数据 已按通讯协议数据组包
     * @param nDeviceGUID：通信地址
     * @param nType：操作码{0显示数据1及时消息}
     * @param pOut
     * @param pnLen
     * @return
     */
	int vtGetProgramPack(byte nDeviceGUID, byte nType, byte[] pOut, long pnLen);

	// 添加一个文本显示区域
	int vtAddTextAreaItem(byte nProgramID, byte nAreaID, int nX, int nY, int nWidth, int nHeight, byte[] pText, int nTextSize, byte nTextColor, byte nStyle, byte nFontType, byte nShowSpeed,
                          byte nStayTime);

	// 添加一个语言播报数据
	int vtAddSoundItem(byte nProgramID, byte nAreaID, byte SoundPerson, byte SoundVolume, byte SoundSpeed, byte[] pSoundText, int sound_len);

	// 添加一个点阵图片显示区域
	int vtAddImageItem(byte nProgramID, byte nAreaID, int nX, int nY, int nWidth, int nHeight, byte nPageNum);

	// 添加一页位图
	int vtAddImagePage(byte nProgramID, byte nAreaID, byte nPageItem, byte nStyle, byte nShowSpeed, byte nStayTime, char[] ImageData, long ImageDataSize);

	// 添加一个时间显示区域
	int vtAddTimeAreaItem(byte nProgramID, byte nAreaID, int nX, int nY, int nWidth, int nHeight, byte nTimeType, byte nHourType, byte nTimeZone, byte nTimeZoneHour, byte nTimeZoneMin,
                          byte[] strTimes, int nTimesFormatSize, byte nTextColor, byte nStyle, byte nFontType, byte nShowSpeed, byte nStayTime);

	// 中断即时信息
	int vtStopRealTimeProgram(byte nDeviceGUID, byte[] pOut, long pnLen);
	// 同步时间
	int vtSynchronizationTimes(byte nDeviceGUID, long year, // 2019
                               // 年：实际年数
                               byte mon, // 2 月：1-12
                               byte day, // 12 日：1-31
                               byte hour, // 18 时：0-23
                               byte min, // 30 分：0-59
                               byte sec, // 30 秒：0-59
                               byte week, // 2 星期：0-6 星期天用0表示
                               byte[] pOut, long pnLen);
	// 开关屏
	int vtSettingOnOff(byte nDeviceGUID, byte Status, byte[] pOut, long pnLen);
	// 亮度调节
	int vtSettingLight(byte nDeviceGUID, byte light, byte[] pOut, long pnLen);

	int vtGetDeviceInfo(byte nDeviceGUID, byte[] pOut, long pnLen);

	/// <summary>
	/// 显示屏颜色类型
	/// </summary>
	public class VT_DIPLAY_COLOR {
		/// <summary>
		/// 单色显示屏
		/// </summary>
		public static int VT_SIGNLE_COLOR=1;
		/// <summary>
		/// 双色显示屏 可显示 红 绿 黄 三色
		/// </summary>
        public static int VT_DOUBLE_COLOR=2;
		/// <summary>
		/// 全彩色显示屏 可显示7彩色
		/// </summary>
//        public static int VT_FULL_COLOR=3;
	}

	/// <summary>
	/// 动作方式
	/// </summary>
	public enum VT_ACTION_TYPE {
		/// <summary>
		/// 静止显示/立即显示/翻页显示
		/// </summary>
		VT_ACTION_HOLD(0x01), // :
		/// <summary>
		/// 向上移动
		/// </summary>
		VT_ACTION_UP(0x1A), // :
		/// <summary>
		/// 向下移动
		/// </summary>
		VT_ACTION_DOWN(0x1B), // :
		/// <summary>
		/// 向左移动
		/// </summary>
		VT_ACTION_LEFT(0x1C), // :
		/// <summary>
		/// 向右移动
		/// </summary>
		VT_ACTION_RIGHT(0x1D), // :
		/// <summary>
		/// 向上连续移动
		/// </summary>
		VT_ACTION_CUP(0x1E), // :
		/// <summary>
		/// 向下连续移动
		/// </summary>
		VT_ACTION_CDOWN(0x1F), // :
		/// <summary>
		/// 向左连续移动
		/// </summary>
		VT_ACTION_CLEFT(0x20), // :
		/// <summary>
		/// 向右连续移动
		/// </summary>
		VT_ACTION_CRIGHT(0x21), // :
		/// <summary>
		/// 闪烁
		/// </summary>
		VT_ACTION_FLASH(0x29);// :

		private int actionType;

		VT_ACTION_TYPE(final int actionType) {
			this.actionType = actionType;
		}

		public int getActionType() {
			return actionType;
		}
	};
	/// <summary>
	/// 显示字符颜色
	/// </summary>
	public enum VT_COLOR_TYPE {
		/// <summary>
		/// 红
		/// </summary>
		VT_COLOR_RED((byte) 0x01),
		/// <summary>
		/// 绿
		/// </summary>
		VT_COLOR_GREEN((byte) 0x02), // :
		/// <summary>
		/// 黄(红+绿)
		/// </summary>
		VT_COLOR_YELLOW((byte) 0x04); // :
		/// <summary>
		/// 蓝
		/// </summary>
//		VT_COLOR_BLUE((byte) 0x08), // :
		/// <summary>
		/// 紫(红+蓝)
		/// </summary>
//		VT_COLOR_Cyan((byte) 0x10), // :
		/// <summary>
		/// 青(绿+蓝)
		/// </summary>
//		VT_COLOR_Purple((byte) 0x20), // :
		/// <summary>
		/// 白(红+绿+蓝)
		/// </summary>
//		VT_COLOR_WHITE((byte) 0x40);// :

		private byte colorType;

		VT_COLOR_TYPE(final byte colorType) {
			this.colorType = colorType;
		}

		public byte getColorType() {
			return colorType;
		}
	};
	/// <summary>
	/// 显示字符大小类型
	/// </summary>
	public enum VT_FONT_TYPE {
		/// <summary>
		/// 16点阵高度
		/// </summary>
		VT_FONT_16(0x10),
		/// <summary>
		/// 24点阵高度
		/// </summary>
		VT_FONT_24(0x18),
		/// <summary>
		/// 32点阵高度
		/// </summary>
		VT_FONT_32(0x20);

		private int fontType;
		VT_FONT_TYPE(final int fontType) {
			this.fontType = fontType;
		}
		public int getFontType() {
			return fontType;
		}
	};

	/// <summary>
	/// 时间类型
	/// </summary>
	public enum VT_TIME_TYPE {
		/// <summary>
		/// 日历时间类型
		/// </summary>
		VT_TIME_TYPE_CALENDAR(0x00), // 日历时间
		/// <summary>
		/// 倒计时
		/// </summary>
		VT_TIME_TYPE_COUNT_DOWN(0x01);//
		private int timeType;
		VT_TIME_TYPE(final int timeType) {
			this.timeType = timeType;
		}
		public int getTimeType() {
			return timeType;
		}
	};
	/// <summary>
	/// 小时制式
	/// </summary>
	public enum VT_HOUR_TYPE {
		/// <summary>
		/// 24小时类型
		/// </summary>
		VT_HOUR_TYPE_24H(0x00), //
		/// <summary>
		/// 12小时类型
		/// </summary>
		VT_HOUR_TYPE_12H(0x01);//
		private int hourType;
		VT_HOUR_TYPE(final int hourType) {
			this.hourType = hourType;
		}
		public int getHourType() {
			return hourType;
		}
	};
	/// <summary>
	/// 时区类型
	/// </summary>
	public enum VT_TIMEZONE_TYPE {
		/// <summary>
		/// 北京时间 默认
		/// </summary>
		VT_TIMEZONE_TYPE_BEIJING(0x00), //
		/// <summary>
		/// 东区
		/// </summary>
		VT_TIMEZONE_TYPE_E(0x01), //
		/// <summary>
		/// 西区
		/// </summary>
		VT_TIMEZONE_TYPE_W(0x02);//
		private int timeType;
		VT_TIMEZONE_TYPE(final int timeType) {
			this.timeType = timeType;
		}
		public int getTimeType() {
			return timeType;
		}
	}
}
