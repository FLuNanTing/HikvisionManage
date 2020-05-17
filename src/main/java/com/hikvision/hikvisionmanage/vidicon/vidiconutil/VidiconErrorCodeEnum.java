package com.hikvision.hikvisionmanage.vidicon.vidiconutil;


public enum VidiconErrorCodeEnum {
    NO_RESOURECE(1,"无可用资源");

    private Integer errorCode;

    private String errorMsg;

    VidiconErrorCodeEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
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
    
    public static String getErrorMsg(Integer code){
//        Arrays.stream(VidiconErrorCodeEnum.values()).filter()
        for(VidiconErrorCodeEnum vidiconErrorCodeEnum:VidiconErrorCodeEnum.values()){
            if(vidiconErrorCodeEnum.getErrorCode().intValue() == code.intValue()){
                return vidiconErrorCodeEnum.getErrorMsg();
            }
        }
        return "系统资源错误";
    }
}
