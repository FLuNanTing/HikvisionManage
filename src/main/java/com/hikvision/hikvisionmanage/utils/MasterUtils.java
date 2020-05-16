package com.hikvision.hikvisionmanage.utils;

/**
 * @program: HikvisionManage
 * @description: 基础工具类
 * @author: LuNanTing
 * @create: 2020-05-16 09:48
 **/
public class MasterUtils {

    public static Boolean checkIsNumber(Object object){
        if(object == null){
            return false;
        }
        if(object instanceof Number){
            return true;
        }else if(object instanceof String){
            try {
                Integer.parseInt(object.toString());
                return true;
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }
}
