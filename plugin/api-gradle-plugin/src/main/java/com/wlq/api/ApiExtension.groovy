package com.wlq.api

class ApiExtension {

    boolean abortOnError = true
    String apiUtilsClass = "com.wlq.willymodule.base.util.ApiUtils";
    String onlyScanLibRegex = ""
    String jumpScanLibRegex = ""

    @Override
    String toString() {
        return "ApiExtension { " +
                "abortOnError: " + abortOnError +
                ", apiUtilsClass: " + apiUtilsClass +
                (onlyScanLibRegex == "" ? "" : ", onlyScanLibRegex: " + onlyScanLibRegex) +
                (jumpScanLibRegex == "" ? "" : ", jumpScanLibRegex: " + jumpScanLibRegex) +
                " }";
    }
}
