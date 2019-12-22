package com.wander.baseframe.view.recycler

/**
 * author wander
 * date 2019/7/13
 *
 */
interface IRoomHeader {
    fun onMove(distance: Float)
    fun reset() {}
    fun getOrgHeight(): Int
    fun getCurHeight(): Int
    fun recoverHeight(newHeight: Int)
    fun hasRest(): Boolean
}