package com.wander.movie.ui.fragment

import android.os.Bundle
import android.view.View
import com.wander.baseframe.component.BaseLayerFragment
import com.wander.baseframe.utils.ImmersionBar
import com.wander.movie.R

class SearchInitFragment : BaseLayerFragment() {
    override fun isUseTitleView() = false
    override fun getLayoutId() = R.layout.fragment_search_init

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        ImmersionBar.initBar(mActivity, true)
    }


}