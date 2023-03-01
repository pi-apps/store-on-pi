package com.api.service.location;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel(value = "GeoPoint", description = "坐标对象")
public class GeoPoint implements Serializable {

    public GeoPoint(double longitude, double latitude) {

        this.longitude = longitude;
        this.latitude = latitude;
    }

    public GeoPoint() {

    }

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度坐标", dataType = "double")
    private double longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度坐标", dataType = "double")
    private double latitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
