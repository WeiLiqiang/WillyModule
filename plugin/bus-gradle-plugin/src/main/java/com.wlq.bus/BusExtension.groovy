package com.wlq.bus

class BusExtension {

    boolean abortOnError = true;
    String busUtilsClass = "com.wlq.willymodule.base.util.BusUtils";
    String onlyScanLibRegex = ""
    String jumpScanLibRegex = ""

    @Override
    String toString() {
        return "BusExtension { " +
                "abortOnError: " + abortOnError +
                ", busUtilsClass: " + busUtilsClass +
                (onlyScanLibRegex == "" ? "" : ", onlyScanLibRegex: " + onlyScanLibRegex) +
                (jumpScanLibRegex == "" ? "" : ", jumpScanLibRegex: " + jumpScanLibRegex) +
                " }";
    }
}