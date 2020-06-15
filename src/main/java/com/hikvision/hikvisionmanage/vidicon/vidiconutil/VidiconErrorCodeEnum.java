package com.hikvision.hikvisionmanage.vidicon.vidiconutil;


public enum VidiconErrorCodeEnum {
    NET_DVR_PASSWORD_ERROR(1, "用户名密码错误。注册时输入的用户名或者密码错误。", "注册时输入的用户名或者密码错误。"),

    NET_DVR_NOENOUGHPRI(2, "权限不足。", "一般和通道相关，例如有预览通道1权限，无预览通道2权限，即有预览权限但不完全，预览通道2返回此错误。"),

    NET_DVR_NOINIT(3, "SDK未初始化", "必须先调用NET_DVR_Init，该接口是调用其他SDK函数的前提且一个程序进程只需要调用一次"),

    NET_DVR_CHANNEL_ERROR(4, "通道号错误。", "设备通道分模拟通道和数字通道（IP通道），NET_DVR_Login_V40登录设备成功之后会返回设备支持的通道个数和起始通道号取值，详见“通道和通道号号相关说明”。"),

    NET_DVR_OVER_MAXLINK(5, "设备总的连接数超过最大。", "例如网络摄像机只支持6路预览，预览第7路即会返回失败，错误码返回5。不同设备性能不一样，支持路数也不同。"),

    NET_DVR_VERSIONNOMATCH(6, "版本不匹配。", "SDK和设备的版本不匹配。"),

    NET_DVR_NETWORK_FAIL_CONNECT(7, "连接设备失败。", "设备不在线或网络原因引起的连接超时等。"),

    NET_DVR_NETWORK_SEND_ERROR(8, "向设备发送失败。", null),

    NET_DVR_NETWORK_RECV_ERROR(9, "从设备接收数据失败。", null),

    NET_DVR_NETWORK_RECV_TIMEOUT(10, "从设备接收数据超时。", null),

    NET_DVR_NETWORK_ERRORDATA(11, "传送的数据有误。", "发送给设备或者从设备接收到的数据错误，如远程参数配置时输入设备不支持的值。"),

    NET_DVR_ORDER_ERROR(12, "调用次序错误。", null),

    NET_DVR_OPERNOPERMIT(13, "无此权限。", "用户对某个功能模块的权限，例如无预览权限用户预览返回此错误。"),

    NET_DVR_COMMANDTIMEOUT(14, "设备命令执行超时。", null),

    NET_DVR_ERRORSERIALPORT(15, "串口号错误。", "指定的设备串口号不存在。"),

    NET_DVR_ERRORALARMPORT(16, "报警端口错误。", "指定的设备报警输入或者输出端口不存在。"),

    NET_DVR_PARAMETER_ERROR(17, "参数错误。", "SDK接口中给入的输入或输出参数为空，或者参数格式或值不符合要求。"),

    NET_DVR_CHAN_EXCEPTION(18, "设备通道处于错误状态", null),

    NET_DVR_NODISK(19, "设备无硬盘", "当设备无硬盘时，对设备的录像文件、硬盘配置等操作失败。"),

    NET_DVR_ERRORDISKNUM(20, "硬盘号错误.", "当对设备进行硬盘管理操作时，指定的硬盘号不存在时返回该错误"),

    NET_DVR_DISK_FULL(21, "设备硬盘满。", null),

    NET_DVR_DISK_ERROR(22, "设备硬盘出错", null),

    NET_DVR_NOSUPPORT(23, "设备不支持。", null),

    NET_DVR_BUSY(24, "设备忙。", null),

    NET_DVR_MODIFY_FAIL(25, "设备修改不成功.", null),

    NET_DVR_PASSWORD_FORMAT_ERROR(26, "密码输入格式不正确", null),

    NET_DVR_DISK_FORMATING(27, "硬盘正在格式化，不能启动操作。", "不能启动操作"),

    NET_DVR_DVRNORESOURCE(28, "设备资源不足。", null),

    NET_DVR_DVROPRATEFAILED(29, "设备操作失败。", null),

    NET_DVR_OPENHOSTSOUND_FAIL(30, "语音对讲、语音广播操作中采集本地音频或打开音频输出失败。", null),

    NET_DVR_ERR_NPQ_STREAM_CFG_CONFLICT(8111, "编码参数存在冲突配置", null);

    private Integer errorCode;

    private String errorMsg;

    private String solutions;

    VidiconErrorCodeEnum(Integer errorCode, String errorMsg, String solutions) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.solutions = solutions;
    }

    public static String getErrorMsg(Integer code) {
        for (VidiconErrorCodeEnum vidiconErrorCodeEnum : VidiconErrorCodeEnum.values()) {
            if (vidiconErrorCodeEnum.getErrorCode().intValue() == code.intValue()) {
                return vidiconErrorCodeEnum.getErrorMsg();
            }
        }
        return "系统资源错误";
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSolutions() {
        return solutions;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }
}