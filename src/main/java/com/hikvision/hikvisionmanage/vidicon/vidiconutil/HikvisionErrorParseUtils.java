package com.hikvision.hikvisionmanage.vidicon.vidiconutil;


import java.util.HashMap;
import java.util.Map;

/**
 * @program: HikvisionManage
 * @description: 海康威视设备异常解析工具
 * @author: LuNanTing
 * @create: 2020-05-14 10:23
 **/
public class HikvisionErrorParseUtils {

    public static Map<String, Object> getErrorMassage(int errorCode, String devIp) {
        Map<String, Object> errorInfo = new HashMap<>();
        String message = parseErrorCode(errorCode);
        errorInfo.put("errorCode", errorCode);
        errorInfo.put("errorMessage", "设备" + devIp + "异常:" + message);
        errorInfo.put("data", null);
        return errorInfo;
    }

    public static Map<String, Object> getErrorMassage(int errorCode) {
        Map<String, Object> errorInfo = new HashMap<>();
        String message = parseErrorCode(errorCode);
        errorInfo.put("errorCode", errorCode);
        errorInfo.put("errorMessage", message);
        errorInfo.put("data", null);
        return errorInfo;
    }

    private static String parseErrorCode(int errorCode) {
        String message = "";
        String msg = VidiconErrorCodeEnum.getErrorMsg(errorCode);
        switch (errorCode) {
            case 1 :
                message = "设备登录密码错误";
                break;
            case 2 :
                message = "权限不足。";
                break;
            case 3 :
                message = "SDK未初始化";
                break;
            case 4 :
                message = "通道号错误。设备没有对应的通道号";
                break;
            case 5 :
                message = "设备总的连接数超过最大";
                break;
            case 6 :
                message = "版本不匹配。SDK和设备的版本不匹配";
                break;
            case 7 :
                message = "设备不在线或网络原因引起的连接超时";
                break;
            case 8 :
                message = "向设备发送失败";
                break;
            case 9 :
                message = "从设备接收数据失败";
                break;
            case 10 :
                message = "从设备接收数据超时";
                break;
            case 17 :
                message = "参数错误,SDK接口中给入的输入或输出参数为空，或者参数格式或值不符合要求";
                break;
            case 18 :
                message = "设备通道处于错误状态";
                break;
            case 19 :
                message = "设备无硬盘。当设备无硬盘时，对设备的录像文件、硬盘配置等操作失败";
                break;
            case 20 :
                message = "硬盘号错误。当对设备进行硬盘管理操作时，指定的硬盘号不存在时返回该错误";
                break;
            case 21 :
                message = "设备硬盘满";
                break;
            case 22 :
                message = "设备硬盘出错";
                break;
            case 23 :
                message = "设备不支持";
                break;
            case 24 :
                message = "设备忙";
                break;
            case 25 :
                message = "设备修改不成功。";
                break;
            case 26 :
                message = "密码输入格式不正确";
                break;
            case 27 :
                message = "硬盘正在格式化，不能启动操作。";
                break;
            case 28 :
                message = "设备资源不足。";
                break;
            case 29 :
                message = "设备操作失败";
                break;
            case 30 :
                message = "语音对讲、语音广播操作中采集本地音频或打开音频输出失败";
                break;
            case 31 :
                message = "设备语音对讲被占用";
                break;
            case 32 :
                message = "时间输入不正确";
                break;
            case 33 :
                message = "回放时设备没有指定的文件";
                break;
            case 34 :
                message = "创建文件出错。本地录像、保存图片、获取配置文件和远程下载录像时创建文件失败。";
                break;
            case 35 :
                message = "打开文件出错。可能因为文件不存在或者路径错误。";
                break;
            case 36 :
                message = "上次的操作还没有完成。";
                break;
            case 37 :
                message = "获取当前播放的时间出错";
                break;
            case 38 :
                message = "播放出错。";
                break;
            case 39 :
                message = "文件格式不正确。";
                break;
            case 40 :
                message = "路径错误";
                break;
            case 41 :
                message = "SDK资源分配错误";
                break;
            case 42 :
                message = "声卡模式错误。当前打开声音播放模式与实际设置的模式不符出错";
                break;
            case 43 :
                message = "缓冲区太小。接收设备数据的缓冲区或存放图片缓冲区不足";
                break;
            case 44 :
                message = "创建SOCKET出错";
                break;
            case 45 :
                message = "设置SOCKET出错";
                break;
            case 46 :
                message = "个数达到最大。分配的注册连接数、预览连接数超过SDK支持的最大数";
                break;
            default :
                message = "系统异常";
                break;
        }
        return message;
    }
}
