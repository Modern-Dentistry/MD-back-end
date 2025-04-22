package com.rustam.modern_dentistry.util.constants;

public interface Directory {
    String domain = "localhost:80";
    String pathPatInsuranceBalance = "media/patient/insurance/balance";
    String pathXray = "media/xray";

    static String getUrl(String path, String fileName) {
        return domain + "/" + path + "/" + fileName;
    }
}
