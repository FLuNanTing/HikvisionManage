package com.hikvision.hikvisionmanage.vidicon.vidiconutil;

/**
 * @program: HikvisionManage
 * @description: 车辆类型分析
 * @author: LuNanTing
 * @create: 2020-05-12 17:08
 **/
public class VehicleTypeAnalysis {

    /**
     *
     * @title {车辆颜色解析}
     * @author 创建人: LuNanTing
     * @date 时间： 2018年12月29日
     * @param byColor
     * @return
     * @return 返回类型：String
     *
     */
    public static String analysisForVehicleColor(byte byColor) {
        String colorStr = new String();
        switch (byColor) {
            case 0 :
                colorStr = "其他色";
                break;
            case 1 :
                colorStr = "白色";
                break;
            case 2 :
                colorStr = "银色";
                break;
            case 3 :
                colorStr = "灰色";
                break;
            case 4 :
                colorStr = "黑色";
                break;
            case 5 :
                colorStr = "红色";
                break;
            case 6 :
                colorStr = "深蓝";
                break;
            case 7 :
                colorStr = "蓝色";
                break;
            case 8 :
                colorStr = "黄色";
                break;
            case 9 :
                colorStr = "绿色";
                break;
            case 10 :
                colorStr = "棕色";
                break;
            case 11 :
                colorStr = "粉色";
                break;
            case 12 :
                colorStr = "紫色";
                break;
            case 13 :
                colorStr = "深灰";
                break;
            case 14 :
                colorStr = "青色";
                break;
            default :
                colorStr = "未进行车身颜色识别";
                break;
        }
        return colorStr;
    }

    public static String analysisForVehicleType(byte byVehicleType) {
        String vehicleTypeStr = new String();
        switch (byVehicleType) {
            case 0 :
                vehicleTypeStr = "未知";
                break;
            case 1 :
                vehicleTypeStr = "客车(大型)";
                break;
            case 2 :
                vehicleTypeStr = "货车(大型)";
                break;
            case 3 :
                vehicleTypeStr = "轿车(小型)";
                break;
            case 4 :
                vehicleTypeStr = "面包车";
                break;
            case 5 :
                vehicleTypeStr = "小货车";
                break;
            case 6 :
                vehicleTypeStr = "行人";
                break;
            case 7 :
                vehicleTypeStr = "二轮车";
                break;
            case 8 :
                vehicleTypeStr = "三轮车";
                break;
            case 9 :
                vehicleTypeStr = "SUV/MPV";
                break;
            case 10 :
                vehicleTypeStr = "中型客车";
                break;
            case 11 :
                vehicleTypeStr = "机动车";
                break;
            case 12 :
                vehicleTypeStr = "非机动车";
                break;
            case 13 :
                vehicleTypeStr = "小型轿车";
                break;
            case 14 :
                vehicleTypeStr = "微型轿车";
                break;
            case 15 :
                vehicleTypeStr = "皮卡车";
                break;
            default :
                break;
        }
        return vehicleTypeStr;

    }
}
