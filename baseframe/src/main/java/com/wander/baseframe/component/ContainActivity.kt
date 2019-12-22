package com.wander.baseframe.component

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.wander.baseframe.R

open class ContainActivity : BaseActivity() {
    protected lateinit var mCurrentFragment: BaseFragment

    //<editor-fold desc="跳转封装">
    companion object {
        private const val EXTRA_FRAGMENT = "EXTRA_FRAGMENT"
        fun start(context: Context?, clazz: Class<*>, params: Bundle?) {
            if (context != null && Fragment::class.java.isAssignableFrom(clazz)) {
                context.startActivity(newIntent(clazz, context, params))
            }
        }

        fun start(activity: Activity, clazz: Class<*>, params: Bundle) {
            start(activity as Context, clazz, params)
        }

        fun start(fragment: Fragment?, clazz: Class<*>, params: Bundle) {
            if (fragment != null) {
                start(fragment.activity as Context, clazz, params)
            }
        }

        fun start(context: Context, clazz: Class<*>) {
            start(context, clazz, null)
        }

        private fun newIntent(clazz: Class<*>, context: Context, bundle: Bundle?): Intent {
            val intent = Intent(context, getMyClass())
            intent.putExtra(EXTRA_FRAGMENT, clazz.name)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            return intent
        }

        private fun getMyClass(): Class<*> {
            return ContainActivity::class.java

        }
    }

    //</editor-fold>

    //<editor-fold desc=方法">

    //@InjectExtra(value = EXTRA_FRAGMENT,remark = "Fragment类名")
    protected var mFragmentClazz: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contain_layout)
        replaceFragment()
    }

    protected fun replaceFragment() {
        mFragmentClazz = intent.getStringExtra(EXTRA_FRAGMENT)
        mFragmentClazz?.let {
            mCurrentFragment = supportFragmentManager.fragmentFactory.instantiate(
                ClassLoader.getSystemClassLoader(),
                it
            ) as BaseFragment
            mCurrentFragment.arguments = intent.extras
            val transaction = supportFragmentManager.beginTransaction()
            replaceAnimation(transaction)
            transaction.replace(R.id.containView, mCurrentFragment)
            transaction.commit()
        }
    }

    open fun replaceAnimation(transaction: FragmentTransaction) {


    }

    //</editor-fold>

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val r = mCurrentFragment.onKeyDown(keyCode, event)
        if (r) {
            return r
        }
        return super.onKeyDown(keyCode, event)
    }


}
