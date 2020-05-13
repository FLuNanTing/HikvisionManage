package com.hikvision.hikvisionmanage.vidicon.vidiconaction;

import com.alibaba.fastjson.JSON;
import com.hikvision.hikvisionmanage.core.sdk.HCNetSDK;
import com.hikvision.hikvisionmanage.devicemanage.bo.LocalServiceManage;
import com.hikvision.hikvisionmanage.devicemanage.bo.RadioServiceManage;
import com.hikvision.hikvisionmanage.vidicon.vidiconutil.VehicleTypeAnalysis;
import com.hikvision.hikvisionmanage.utils.HttpClientUtil;
import com.hikvision.hikvisionmanage.utils.LoggerUtil;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: HikvisionManage
 * @description: 摄像机设置
 * @author: LuNanTing
 * @create: 2020-05-12 14:49
 **/
public class VidiconAction {

    HCNetSDK HCNETSDK = HCNetSDK.INSTANCE;

    /**
     * lAlarmHandle:报警布防句柄
     */
    NativeLong lAlarmHandle;

    /**
     * fMSFCallBack_V31:报警回调函数实现
     */
    FMSGCallBack_V31 fMSFCallBack_V31;

    public VidiconAction(){
        lAlarmHandle = new NativeLong(-1);
        fMSFCallBack_V31 = null;
        boolean initSuc = HCNETSDK.NET_DVR_Init();
        if (initSuc != true) {
            LoggerUtil.error("初始化失败");
            return;
        }
        HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG struGeneralCfg = new HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG();
        // 控制JSON透传报警数据和图片是否分离，0-不分离，1-分离（分离后走COMM_ISAPI_ALARM回调返回）
        struGeneralCfg.byAlarmJsonPictureSeparate = 1;
        struGeneralCfg.write();

        if (!HCNETSDK.NET_DVR_SetSDKLocalCfg(17, struGeneralCfg.getPointer())) {
            LoggerUtil.error("NET_DVR_SetSDKLocalCfg失败");
        }
    }

    /**
     * 报警函数设置
     */
    public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {

        @Override
        public boolean invoke(NativeLong lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
            callBacksAction(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
            return false;
        }
    }

    /**
     * 抓拍机回调处理函数
     * @param lCommand
     * @param pAlarmer
     * @param pAlarmInfo
     * @param dwBufLen
     * @param pUser
     */
    private void callBacksAction(NativeLong lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser){
        // 标志1-Start:抓拍时间
        LoggerUtil.info("进入回调函数方法" );
        String sAlarmType = new String();
        // 报警时间
        LoggerUtil.info("事件ID:" + lCommand.intValue());
        // lCommand是传的报警类型
        switch (lCommand.intValue()) {
            case HCNetSDK.COMM_UPLOAD_PLATE_RESULT :
                HCNetSDK.NET_DVR_PLATE_RESULT strPlateResult = new HCNetSDK.NET_DVR_PLATE_RESULT();
                strPlateResult.write();
                Pointer pPlateInfo = strPlateResult.getPointer();
                pPlateInfo.write(0, pAlarmInfo.getByteArray(0, strPlateResult.size()), 0, strPlateResult.size());
                strPlateResult.read();
                try {
                    String srt3 = new String(strPlateResult.struPlateInfo.sLicense, "GBK");
                    sAlarmType = sAlarmType + "：交通抓拍上传，车牌：" + srt3;
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                if (strPlateResult.dwPicLen > 0) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        fout = new FileOutputStream("E://" + newName + "01.jpg");
                        // 将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strPlateResult.pBuffer1.getByteBuffer(offset, strPlateResult.dwPicLen);
                        byte[] bytes = new byte[strPlateResult.dwPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 摄像头抓拍结果
            case HCNetSDK.COMM_ITS_PLATE_RESULT :
                Date date = new Date();
                LoggerUtil.info("摄像头抓拍数据处理");
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                HCNetSDK.NET_ITS_PLATE_RESULT strItsPlateResult = new HCNetSDK.NET_ITS_PLATE_RESULT();
                strItsPlateResult.write();
                Pointer pItsPlateInfo = strItsPlateResult.getPointer();
                pItsPlateInfo.write(0, pAlarmInfo.getByteArray(0, strItsPlateResult.size()), 0, strItsPlateResult.size());
                strItsPlateResult.read();
                String picColor = null;
                String picNo = null;
                try {
                    LoggerUtil.info("截取数据,返回数据:" + strItsPlateResult.struPlateInfo.sLicense);
                    String srt3 = new String(strItsPlateResult.struPlateInfo.sLicense, "GBK").trim();
                    LoggerUtil.info("返回数据:" + srt3);
                    picColor = srt3.substring(0, 1);// 车牌颜色
                    picNo = srt3.substring(1, srt3.length());
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                LoggerUtil.info("截取数据End");
                String colorStr = VehicleTypeAnalysis.analysisForVehicleColor(strItsPlateResult.struVehicleInfo.byColor);// 车身颜色
                map.put("color", colorStr);
                String vehicleTypeStr = VehicleTypeAnalysis.analysisForVehicleType(strItsPlateResult.byVehicleType);// 车辆类型
                map.put("vehicleType", vehicleTypeStr);
                // 报警设备IP地址
                String devIP = new String(pAlarmer.sDeviceIP).trim();
                LoggerUtil.info("车牌:" + picNo);
                LoggerUtil.info("设备IP:" + devIP);
                short devPort = pAlarmer.wLinkPort;
                map.put("devIp", devIP);
                map.put("devPort", devPort);
                // 车牌颜色
                map.put("picColor", picColor);
                // 车牌号码
                map.put("picNo", picNo);
                map.put("date",System.currentTimeMillis());
                HCNetSDK.NET_ITS_PICTURE_INFO[] struPicInfo = strItsPlateResult.struPicInfo;
                map.put("struPicInfo", struPicInfo.length);
                map.put("serialNumber", new String(pAlarmer.sSerialNumber).trim());
                LoggerUtil.info("标志1-end:抓拍业务耗时:" + (System.currentTimeMillis() - date.getTime()) + "ms");
                informationPush(map, LocalServiceManage.getInstance());
                LoggerUtil.info("HttpClient完成请求:" + (System.currentTimeMillis() - date.getTime()) + "ms");
                break;
            default :
                // 报警类型
                break;
        }
    }

    private void informationPush(Map<String ,Object> map ,LocalServiceManage entity){
        Map<String, Object> localhostMap = new HashMap<>(10);
        Map<String, Object> pushInfo = new HashMap<>(3);
        localhostMap.put("localhostIp", entity.getLocalIp());
        localhostMap.put("localhostPort", entity.getLocalPort());
        localhostMap.put("localhostProject", entity.getLocalProject());
        localhostMap.put("localhostCode", entity.getLocalCode());
        pushInfo.put("localhost", JSON.toJSON(localhostMap));
        pushInfo.put("info", JSON.toJSON(map));
        List<RadioServiceManage> radioServiceManageList = entity.getRadioServerList();

        Long date = System.currentTimeMillis();
        radioServiceManageList.forEach( radioServiceManage -> {
            String httpHead = null;
            if(radioServiceManage.getRadioPort().equals(80)){
                httpHead = "http://";
            }else if(radioServiceManage.getRadioPort().equals(443)){
                httpHead = "https://";
            }else{
                httpHead = "http://";
            }
            String url = httpHead + radioServiceManage.getRadioIp() + radioServiceManage.getRadioPort() + radioServiceManage.getRadioProject() + "/comDevManageC/controlGate";
            HttpClientUtil.submitPostRequestMap(url.trim(), pushInfo);
            LoggerUtil.info(url + "响应耗时:" + (System.currentTimeMillis() - date) + "ms");
        });
    }

}
