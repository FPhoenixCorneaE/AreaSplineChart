package com.wkz.area_spline_chart

class TempBean {
    /**
     * 时间戳
     */
    var timestamp: Long = 0

    /**
     * 温度
     */
    var temp = 0

    constructor() {}
    constructor(temp: Int, timestamp: Long) {
        this.temp = temp
        this.timestamp = timestamp
    }

}