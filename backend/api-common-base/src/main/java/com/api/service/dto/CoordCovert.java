package com.api.service.dto;

import java.math.BigDecimal;

/**
 * 坐标系转换
 */
public class CoordCovert {

    /**
     * 提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）
     */

    // 定义一些常量

    private static double x_PI = 3.14159265358979324 * 3000.0 / 180.0;

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * <p>
     * 即 百度 转 谷歌、高德
     *
     * @param bd_lon
     * @param bd_lat
     * @returns {*[]}
     */

    public static double[] bdToGcj02(double bd_lon, double bd_lat) {

        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        double[] coords = new double[2];
        coords[0] = gg_lng;
        coords[1] = gg_lat;
        return coords;
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * <p>
     * 即谷歌、高德 转 百度
     *
     * @param lng
     * @param lat
     * @returns {*[]}
     */

    public static double[] gcjTobd(double lng, double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002
                * Math.sin(lat * x_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        double[] coords = new double[2];
        coords[0] = new BigDecimal(bd_lng).setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        coords[1] = new BigDecimal(bd_lat).setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        return coords;
    }
}
