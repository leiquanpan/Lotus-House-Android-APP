package com.nestlabs.sdk.sample;

import com.google.gson.annotations.SerializedName;


public class Inverter_data {
    @SerializedName("dataloggerSn")
    private String dataloggerSn;

    @SerializedName("device_sn")
    private String device_sn;

    @SerializedName("error_code")
    private Integer error_code;

    @SerializedName("error_msg")
    private String error_msg;

    @SerializedName("data")
    private Data data;

    public String getDataloggerSn() {
        return dataloggerSn;
    }

    public void setDataloggerSn(String dataloggerSn) {
        this.dataloggerSn = dataloggerSn;
    }

    public String getDevice_sn() {
        return device_sn;
    }

    public void setDevice_sn(String device_sn) {
        this.device_sn = device_sn;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Inverter_data(String dataloggerSn, String device_sn, Integer error_code, String error_msg, Data data) {
        this.dataloggerSn = dataloggerSn;
        this.device_sn = device_sn;
        this.error_code = error_code;
        this.error_msg = error_msg;
        this.data = data;
    }

    public class Data {
        @SerializedName("again")
        private Boolean again;

        @SerializedName("bigDevice")
        private Boolean bigDevice;

        @SerializedName("currentString1")
        private String currentString1;

        @SerializedName("currentString2")
        private String currentString2;

        @SerializedName("currentString3")
        private String currentString3;

        @SerializedName("currentString4")
        private String currentString4;

        @SerializedName("currentString5")
        private String currentString5;

        @SerializedName("currentString6")
        private String currentString6;

        @SerializedName("currentString7")
        private String currentString7;

        @SerializedName("currentString8")
        private String currentString8;

        @SerializedName("dwStringWarningValue1")
        private String dwStringWarningValue1;

        @SerializedName("epv1Today")
        private Double epv1Today;

        @SerializedName("epv1Total")
        private Double epv1Total;

        @SerializedName("epv2Today")
        private Double epv2Today;

        @SerializedName("epv2Total")
        private Double epv2Total;

        @SerializedName("epvToday")
        private Double epvToday;

        @SerializedName("eRacToday")
        private Double eRacToday;

        @SerializedName("eRacTotal")
        private Double eRacTotal;

        @SerializedName("fac")
        private Double fac;

        @SerializedName("faultType")
        private Integer faultType;

        @SerializedName("iacr")
        private Double iacr;

        @SerializedName("iacs")
        private Double iacs;

        @SerializedName("iact")
        private Double iact;

        @SerializedName("id")
        private Integer id;

        @SerializedName("inverterId")
        private String inverterId;

        @SerializedName("iPidPvape")
        private Double iPidPvape;

        @SerializedName("iPidPvbpe")
        private Double iPidPvbpe;

        @SerializedName("ipmTemperature")
        private Double ipmTemperature;

        @SerializedName("ipv1")
        private Double ipv1;

        @SerializedName("ipv2")
        private Double ipv2;

        @SerializedName("nBusVoltage")
        private Double nBusVoltage;

        @SerializedName("opFullwatt")
        private Double opFullwatt;

        @SerializedName("pac")
        private Double pac;

        @SerializedName("pacr")
        private Double pacr;

        @SerializedName("pacs")
        private Double pacs;

        @SerializedName("pact")
        private Double pact;

        @SerializedName("pBusVoltage")
        private Double pBusVoltage;

        @SerializedName("pf")
        private Integer pf;

        @SerializedName("pidStatus")
        private Integer pidStatus;

        @SerializedName("powerToday")
        private Double powerToday;

        @SerializedName("powerTotal")
        private Double powerTotal;

        @SerializedName("ppv")
        private Double ppv;

        @SerializedName("ppv1")
        private Double ppv1;

        @SerializedName("ppv2")
        private Double ppv2;

        @SerializedName("rac")
        private Double rac;

        @SerializedName("realOPPercent")
        private Double realOPPercent;

        @SerializedName("status")
        private Integer status;

        @SerializedName("statusText")
        private String statusText;

        @SerializedName("strFault")
        private Integer strFault;

        @SerializedName("temperature")
        private Double temperature;

        @SerializedName("time")
        private String time;

        @SerializedName("timeTotal")
        private Double timeTotal;

        @SerializedName("timeTotalText")
        private String timeTotalText;

        @SerializedName("vacr")
        private Double vacr;

        @SerializedName("vacs")
        private Double vacs;

        @SerializedName("vact")
        private Double vact;

        @SerializedName("vPidPvape")
        private Double vPidPvape;

        @SerializedName("vPidPvbpe")
        private Double vPidPvbpe;

        @SerializedName("vpv1")
        private Double vpv1;

        @SerializedName("vpv2")
        private Double vpv2;

        @SerializedName("vString1")
        private String vString1;

        @SerializedName("vString2")
        private String vString2;

        @SerializedName("vString3")
        private String vString3;

        @SerializedName("vString4")
        private String vString4;

        @SerializedName("vString5")
        private String vString5;

        @SerializedName("vString6")
        private String vString6;

        @SerializedName("vString7")
        private String vString7;

        @SerializedName("vString8")
        private String vString8;

        @SerializedName("warnCode")
        private Integer warnCode;

        @SerializedName("warningValue1")
        private String warningValue1;

        @SerializedName("warningValue2")
        private String warningValue2;

        @SerializedName("wPIDFaultValue")
        private String wPIDFaultValue;

        public Data(Boolean again, Boolean bigDevice, String currentString1, String currentString2, String currentString3, String currentString4, String currentString5, String currentString6, String currentString7, String currentString8, String dwStringWarningValue1, Double epv1Today, Double epv1Total, Double epv2Today, Double epv2Total, Double epvToday, Double eRacToday, Double eRacTotal, Double fac, Integer faultType, Double iacr, Double iacs, Double iact, Integer id, String inverterId, Double iPidPvape, Double iPidPvbpe, Double ipmTemperature, Double ipv1, Double ipv2, Double nBusVoltage, Double opFullwatt, Double pac, Double pacr, Double pacs, Double pact, Double pBusVoltage, Integer pf, Integer pidStatus, Double powerToday, Double powerTotal, Double ppv, Double ppv1, Double ppv2, Double rac, Double realOPPercent, Integer status, String statusText, Integer strFault, Double temperature, String time, Double timeTotal, String timeTotalText, Double vacr, Double vacs, Double vact, Double vPidPvape, Double vPidPvbpe, Double vpv1, Double vpv2, String vString1, String vString2, String vString3, String vString4, String vString5, String vString6, String vString7, String vString8, Integer warnCode, String warningValue1, String warningValue2, String wPIDFaultValue, String wStringStatusValue, String timeCalendar) {
            this.again = again;
            this.bigDevice = bigDevice;
            this.currentString1 = currentString1;
            this.currentString2 = currentString2;
            this.currentString3 = currentString3;
            this.currentString4 = currentString4;
            this.currentString5 = currentString5;
            this.currentString6 = currentString6;
            this.currentString7 = currentString7;
            this.currentString8 = currentString8;
            this.dwStringWarningValue1 = dwStringWarningValue1;
            this.epv1Today = epv1Today;
            this.epv1Total = epv1Total;
            this.epv2Today = epv2Today;
            this.epv2Total = epv2Total;
            this.epvToday = epvToday;
            this.eRacToday = eRacToday;
            this.eRacTotal = eRacTotal;
            this.fac = fac;
            this.faultType = faultType;
            this.iacr = iacr;
            this.iacs = iacs;
            this.iact = iact;
            this.id = id;
            this.inverterId = inverterId;
            this.iPidPvape = iPidPvape;
            this.iPidPvbpe = iPidPvbpe;
            this.ipmTemperature = ipmTemperature;
            this.ipv1 = ipv1;
            this.ipv2 = ipv2;
            this.nBusVoltage = nBusVoltage;
            this.opFullwatt = opFullwatt;
            this.pac = pac;
            this.pacr = pacr;
            this.pacs = pacs;
            this.pact = pact;
            this.pBusVoltage = pBusVoltage;
            this.pf = pf;
            this.pidStatus = pidStatus;
            this.powerToday = powerToday;
            this.powerTotal = powerTotal;
            this.ppv = ppv;
            this.ppv1 = ppv1;
            this.ppv2 = ppv2;
            this.rac = rac;
            this.realOPPercent = realOPPercent;
            this.status = status;
            this.statusText = statusText;
            this.strFault = strFault;
            this.temperature = temperature;
            this.time = time;
            this.timeTotal = timeTotal;
            this.timeTotalText = timeTotalText;
            this.vacr = vacr;
            this.vacs = vacs;
            this.vact = vact;
            this.vPidPvape = vPidPvape;
            this.vPidPvbpe = vPidPvbpe;
            this.vpv1 = vpv1;
            this.vpv2 = vpv2;
            this.vString1 = vString1;
            this.vString2 = vString2;
            this.vString3 = vString3;
            this.vString4 = vString4;
            this.vString5 = vString5;
            this.vString6 = vString6;
            this.vString7 = vString7;
            this.vString8 = vString8;
            this.warnCode = warnCode;
            this.warningValue1 = warningValue1;
            this.warningValue2 = warningValue2;
            this.wPIDFaultValue = wPIDFaultValue;
            this.wStringStatusValue = wStringStatusValue;
            this.timeCalendar = timeCalendar;
        }

        public Boolean getAgain() {
            return again;
        }

        public void setAgain(Boolean again) {
            this.again = again;
        }

        public Boolean getBigDevice() {
            return bigDevice;
        }

        public void setBigDevice(Boolean bigDevice) {
            this.bigDevice = bigDevice;
        }

        public String getCurrentString1() {
            return currentString1;
        }

        public void setCurrentString1(String currentString1) {
            this.currentString1 = currentString1;
        }

        public String getCurrentString2() {
            return currentString2;
        }

        public void setCurrentString2(String currentString2) {
            this.currentString2 = currentString2;
        }

        public String getCurrentString3() {
            return currentString3;
        }

        public void setCurrentString3(String currentString3) {
            this.currentString3 = currentString3;
        }

        public String getCurrentString4() {
            return currentString4;
        }

        public void setCurrentString4(String currentString4) {
            this.currentString4 = currentString4;
        }

        public String getCurrentString5() {
            return currentString5;
        }

        public void setCurrentString5(String currentString5) {
            this.currentString5 = currentString5;
        }

        public String getCurrentString6() {
            return currentString6;
        }

        public void setCurrentString6(String currentString6) {
            this.currentString6 = currentString6;
        }

        public String getCurrentString7() {
            return currentString7;
        }

        public void setCurrentString7(String currentString7) {
            this.currentString7 = currentString7;
        }

        public String getCurrentString8() {
            return currentString8;
        }

        public void setCurrentString8(String currentString8) {
            this.currentString8 = currentString8;
        }

        public String getDwStringWarningValue1() {
            return dwStringWarningValue1;
        }

        public void setDwStringWarningValue1(String dwStringWarningValue1) {
            this.dwStringWarningValue1 = dwStringWarningValue1;
        }

        public Double getEpv1Today() {
            return epv1Today;
        }

        public void setEpv1Today(Double epv1Today) {
            this.epv1Today = epv1Today;
        }

        public Double getEpv1Total() {
            return epv1Total;
        }

        public void setEpv1Total(Double epv1Total) {
            this.epv1Total = epv1Total;
        }

        public Double getEpv2Today() {
            return epv2Today;
        }

        public void setEpv2Today(Double epv2Today) {
            this.epv2Today = epv2Today;
        }

        public Double getEpv2Total() {
            return epv2Total;
        }

        public void setEpv2Total(Double epv2Total) {
            this.epv2Total = epv2Total;
        }

        public Double getEpvToday() {
            return epvToday;
        }

        public void setEpvToday(Double epvToday) {
            this.epvToday = epvToday;
        }

        public Double geteRacToday() {
            return eRacToday;
        }

        public void seteRacToday(Double eRacToday) {
            this.eRacToday = eRacToday;
        }

        public Double geteRacTotal() {
            return eRacTotal;
        }

        public void seteRacTotal(Double eRacTotal) {
            this.eRacTotal = eRacTotal;
        }

        public Double getFac() {
            return fac;
        }

        public void setFac(Double fac) {
            this.fac = fac;
        }

        public Integer getFaultType() {
            return faultType;
        }

        public void setFaultType(Integer faultType) {
            this.faultType = faultType;
        }

        public Double getIacr() {
            return iacr;
        }

        public void setIacr(Double iacr) {
            this.iacr = iacr;
        }

        public Double getIacs() {
            return iacs;
        }

        public void setIacs(Double iacs) {
            this.iacs = iacs;
        }

        public Double getIact() {
            return iact;
        }

        public void setIact(Double iact) {
            this.iact = iact;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getInverterId() {
            return inverterId;
        }

        public void setInverterId(String inverterId) {
            this.inverterId = inverterId;
        }

        public Double getiPidPvape() {
            return iPidPvape;
        }

        public void setiPidPvape(Double iPidPvape) {
            this.iPidPvape = iPidPvape;
        }

        public Double getiPidPvbpe() {
            return iPidPvbpe;
        }

        public void setiPidPvbpe(Double iPidPvbpe) {
            this.iPidPvbpe = iPidPvbpe;
        }

        public Double getIpmTemperature() {
            return ipmTemperature;
        }

        public void setIpmTemperature(Double ipmTemperature) {
            this.ipmTemperature = ipmTemperature;
        }

        public Double getIpv1() {
            return ipv1;
        }

        public void setIpv1(Double ipv1) {
            this.ipv1 = ipv1;
        }

        public Double getIpv2() {
            return ipv2;
        }

        public void setIpv2(Double ipv2) {
            this.ipv2 = ipv2;
        }

        public Double getnBusVoltage() {
            return nBusVoltage;
        }

        public void setnBusVoltage(Double nBusVoltage) {
            this.nBusVoltage = nBusVoltage;
        }

        public Double getOpFullwatt() {
            return opFullwatt;
        }

        public void setOpFullwatt(Double opFullwatt) {
            this.opFullwatt = opFullwatt;
        }

        public Double getPac() {
            return pac;
        }

        public void setPac(Double pac) {
            this.pac = pac;
        }

        public Double getPacr() {
            return pacr;
        }

        public void setPacr(Double pacr) {
            this.pacr = pacr;
        }

        public Double getPacs() {
            return pacs;
        }

        public void setPacs(Double pacs) {
            this.pacs = pacs;
        }

        public Double getPact() {
            return pact;
        }

        public void setPact(Double pact) {
            this.pact = pact;
        }

        public Double getpBusVoltage() {
            return pBusVoltage;
        }

        public void setpBusVoltage(Double pBusVoltage) {
            this.pBusVoltage = pBusVoltage;
        }

        public Integer getPf() {
            return pf;
        }

        public void setPf(Integer pf) {
            this.pf = pf;
        }

        public Integer getPidStatus() {
            return pidStatus;
        }

        public void setPidStatus(Integer pidStatus) {
            this.pidStatus = pidStatus;
        }

        public Double getPowerToday() {
            return powerToday;
        }

        public void setPowerToday(Double powerToday) {
            this.powerToday = powerToday;
        }

        public Double getPowerTotal() {
            return powerTotal;
        }

        public void setPowerTotal(Double powerTotal) {
            this.powerTotal = powerTotal;
        }

        public Double getPpv() {
            return ppv;
        }

        public void setPpv(Double ppv) {
            this.ppv = ppv;
        }

        public Double getPpv1() {
            return ppv1;
        }

        public void setPpv1(Double ppv1) {
            this.ppv1 = ppv1;
        }

        public Double getPpv2() {
            return ppv2;
        }

        public void setPpv2(Double ppv2) {
            this.ppv2 = ppv2;
        }

        public Double getRac() {
            return rac;
        }

        public void setRac(Double rac) {
            this.rac = rac;
        }

        public Double getRealOPPercent() {
            return realOPPercent;
        }

        public void setRealOPPercent(Double realOPPercent) {
            this.realOPPercent = realOPPercent;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public Integer getStrFault() {
            return strFault;
        }

        public void setStrFault(Integer strFault) {
            this.strFault = strFault;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Double getTimeTotal() {
            return timeTotal;
        }

        public void setTimeTotal(Double timeTotal) {
            this.timeTotal = timeTotal;
        }

        public String getTimeTotalText() {
            return timeTotalText;
        }

        public void setTimeTotalText(String timeTotalText) {
            this.timeTotalText = timeTotalText;
        }

        public Double getVacr() {
            return vacr;
        }

        public void setVacr(Double vacr) {
            this.vacr = vacr;
        }

        public Double getVacs() {
            return vacs;
        }

        public void setVacs(Double vacs) {
            this.vacs = vacs;
        }

        public Double getVact() {
            return vact;
        }

        public void setVact(Double vact) {
            this.vact = vact;
        }

        public Double getvPidPvape() {
            return vPidPvape;
        }

        public void setvPidPvape(Double vPidPvape) {
            this.vPidPvape = vPidPvape;
        }

        public Double getvPidPvbpe() {
            return vPidPvbpe;
        }

        public void setvPidPvbpe(Double vPidPvbpe) {
            this.vPidPvbpe = vPidPvbpe;
        }

        public Double getVpv1() {
            return vpv1;
        }

        public void setVpv1(Double vpv1) {
            this.vpv1 = vpv1;
        }

        public Double getVpv2() {
            return vpv2;
        }

        public void setVpv2(Double vpv2) {
            this.vpv2 = vpv2;
        }

        public String getvString1() {
            return vString1;
        }

        public void setvString1(String vString1) {
            this.vString1 = vString1;
        }

        public String getvString2() {
            return vString2;
        }

        public void setvString2(String vString2) {
            this.vString2 = vString2;
        }

        public String getvString3() {
            return vString3;
        }

        public void setvString3(String vString3) {
            this.vString3 = vString3;
        }

        public String getvString4() {
            return vString4;
        }

        public void setvString4(String vString4) {
            this.vString4 = vString4;
        }

        public String getvString5() {
            return vString5;
        }

        public void setvString5(String vString5) {
            this.vString5 = vString5;
        }

        public String getvString6() {
            return vString6;
        }

        public void setvString6(String vString6) {
            this.vString6 = vString6;
        }

        public String getvString7() {
            return vString7;
        }

        public void setvString7(String vString7) {
            this.vString7 = vString7;
        }

        public String getvString8() {
            return vString8;
        }

        public void setvString8(String vString8) {
            this.vString8 = vString8;
        }

        public Integer getWarnCode() {
            return warnCode;
        }

        public void setWarnCode(Integer warnCode) {
            this.warnCode = warnCode;
        }

        public String getWarningValue1() {
            return warningValue1;
        }

        public void setWarningValue1(String warningValue1) {
            this.warningValue1 = warningValue1;
        }

        public String getWarningValue2() {
            return warningValue2;
        }

        public void setWarningValue2(String warningValue2) {
            this.warningValue2 = warningValue2;
        }

        public String getwPIDFaultValue() {
            return wPIDFaultValue;
        }

        public void setwPIDFaultValue(String wPIDFaultValue) {
            this.wPIDFaultValue = wPIDFaultValue;
        }

        public String getwStringStatusValue() {
            return wStringStatusValue;
        }

        public void setwStringStatusValue(String wStringStatusValue) {
            this.wStringStatusValue = wStringStatusValue;
        }

        public String getTimeCalendar() {
            return timeCalendar;
        }

        public void setTimeCalendar(String timeCalendar) {
            this.timeCalendar = timeCalendar;
        }

        @SerializedName("wStringStatusValue")
        private String wStringStatusValue;

        @SerializedName("timeCalendar")
        private String timeCalendar;

    }

}
