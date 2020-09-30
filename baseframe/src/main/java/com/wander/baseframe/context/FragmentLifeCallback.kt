package com.wander.baseframe.context

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wander.baseframe.utils.DebugLog

/**
 * author wander
 * date 2020/9/28
 *
 */
class FragmentLifeCallback : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentPreAttached")
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentAttached")
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentActivityCreated")
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentPreCreated")
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentViewCreated")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentCreated")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentStarted")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentResumed")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentPaused")
    }


    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentSaveInstanceState")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentStopped")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentDetached")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentViewDestroyed")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        DebugLog.d("FragmentLifecycle", "${f.javaClass.simpleName}-onFragmentDestroyed")
    }

}