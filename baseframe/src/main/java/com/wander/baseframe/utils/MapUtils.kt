package com.wander.baseframe.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.wander.baseframe.R


object MapUtils {
    val defaultToName = "终点"
    /**
     * 跳转到百度地图
     * @param context 使用Application
     * @param latitude
     * @param longitude
     */
    fun goToBDMap(context: Context, lngLat: LngLat, toName: String = defaultToName) {
        val uri = ("baidumap://map/direction"
                + "?origin=我的位置"
                + "&destination=name:$toName|latlng:" + lngLat.lat + "," + lngLat.lng
                + "&coord_type=bd09ll"
                + "&mode=driving"
                + "&src=andr.companyName.appName")//src为统计来源必填，companyName、appName是公司名和应用名
        val intent = Intent("android.intent.action.VIEW", Uri.parse(uri))
        intent.addCategory("android.intent.category.DEFAULT")
        context.startActivity(intent)
    }


    /**
     * 跳转到高德地图
     * @param context 使用Application
     * @param latitude
     * @param longitude
     */
    fun goToGDMap(context: Context, lngLat: LngLat, toName: String = defaultToName) {
        //默认驾车
        val uri =
            "amapuri://route/plan?sourceApplication=${ResourceUtils.getString(R.string.app_name)}&dname=${toName}&dlat=" + lngLat.lat + "&dlon=" + lngLat.lng + "&dev=0"
        val intent = Intent("android.intent.action.VIEW", Uri.parse(uri))
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setPackage("com.autonavi.minimap")
        context.startActivity(intent)
    }

    /**
     * 跳转腾讯地图
     */
    fun goToTencentMap(context: Context, lngLat: LngLat, toName: String = defaultToName) {
        val stringBuffer = StringBuffer("qqmap://map/routeplan?type=drive")
            .append("&tocoord=").append(lngLat.lat).append(",").append(lngLat.lng)
            .append("&to=$toName")
        val intent = Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()))
        context.startActivity(intent)
    }


    /**
     * 跳转谷歌
     */
    fun goToGoogleMap(context: Context, lngLat: LngLat, toName: String = defaultToName) {
        val gmmIntentUri = Uri.parse("google.navigation:q=" + lngLat.lat + "," + lngLat.lng)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    /**
     * GCJ-02 坐标转换成 BD-09 坐标
     */
    fun GCJ2BD(bd: LngLat): LngLat {
        val x = bd.lng
        val y = bd.lat
        val z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI)
        val theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI)
        val tempLon = z * Math.cos(theta) + 0.0065
        val tempLat = z * Math.sin(theta) + 0.006
        return LngLat(tempLon, tempLat)
    }


    /**
     * BD-09 坐标转换成 GCJ-02 坐标
     */
    fun BD2GCJ(bd: LngLat): LngLat {
        val x = bd.lng - 0.0065
        val y = bd.lat - 0.006
        val z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI)
        val theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI)

        val lng = z * Math.cos(theta)//lng
        val lat = z * Math.sin(theta)//lat
        return LngLat(lng, lat)
    }
}


data class LngLat(val lng: Double, val lat: Double) {
    constructor(lng: String, lat: String) : this(lng.toDouble(), lat.toDouble())
}