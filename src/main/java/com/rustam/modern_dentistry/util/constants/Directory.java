package com.rustam.modern_dentistry.util.constants;

public interface Directory {
    String domain = "http://localhost:80";
    String pathPatInsuranceBalance = "media/patient/insurance/balance";
    String pathPatPhoto = "/root/modern-dentistry/media/patient/photo";
    String pathPatXray = "root/modern-dentistry/media/patient/xray";

    static String getUrl(String path, String fileName) {
        return domain + "/" + path + "/" + fileName;
    }
}
