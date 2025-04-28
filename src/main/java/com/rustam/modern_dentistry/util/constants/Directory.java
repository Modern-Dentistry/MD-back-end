package com.rustam.modern_dentistry.util.constants;

public interface Directory {
    String domain = "http://159.89.3.81:80";
    String pathPatInsuranceBalance = "media/patient/insurance/balance";
    String pathPatPhoto = "media/patient/photo";
    String pathPatXray = "media/patient/xray";
    String pathPatVideo = "media/patient/video";

    static String getUrl(String path, String fileName) {
        return domain + "/" + path + "/" + fileName;
    }
}
