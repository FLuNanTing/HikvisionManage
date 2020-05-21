package com.hikvision.hikvisionmanage.utils;

import com.hikvision.hikvisionmanage.core.sdk.CSDKExport;
import com.hikvision.hikvisionmanage.ledscreen.ledscreenutil.LedScreenErrorParseUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: HikvisionLEDManage
 * @description: Socket工具
 * @author: LuNanTing
 * @create: 2020年5月15日18:41:20
 **/
public class SocketUtil {

    private static CSDKExport sdkExport = CSDKExport.INSTANCE;

    public static Map<String, Object> socketClient(String ipAddress, Integer port, int ret) {
        Map<String, Object> map = new HashMap<>(16);
        Socket socket = null;
        try {
            socket = new Socket(ipAddress, port);
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            byte[] pProgram = new byte[1024];
            long nSize = 0;
            byte[] buffer = new byte[1024];
            do {
                //获取节目数据
                ret = sdkExport.vtGetProgramPack((byte) 0x01, (byte) 1, pProgram, nSize);
                map = LedScreenErrorParseUtils.parseErrorCode(ret, ipAddress);
                bos.write(pProgram, 0, pProgram.length);
                bos.flush();
                bis.read(buffer);
            } while (ret > 0);
            byte[] pOut = new byte[1024];
            //协议数据解析
            ret = sdkExport.vt_ProtocolAnalyze(buffer, buffer.length, pOut, 0);
            map = LedScreenErrorParseUtils.parseErrorCode(ret, ipAddress);
            bos.close();
            bis.close();
            socket.close();
            // 释放资源
            ret = sdkExport.vtUninitialize();
            map = LedScreenErrorParseUtils.parseErrorCode(ret, ipAddress);
        } catch (java.net.ConnectException e){
            map.put("code", -1);
            map.put("errorMessage", "设备ip:" + ipAddress + "无法连接");
            map.put("data", null);
        } catch (IOException e) {
            map.put("code", -1);
            map.put("errorMessage", "向ip:" + ipAddress + "发送失败,失败原因:" + e.getMessage());
            map.put("data", null);
            LoggerUtil.error("通信失败,失败原因{}",map.get("message"));
            e.printStackTrace();
        }finally {
            try {
                if (socket!= null){
                    socket.close();
                }
            } catch (IOException e) {
                LoggerUtil.error("通信关闭异常");
                e.printStackTrace();
            }
        }
        return map;
    }
}
