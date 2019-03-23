package com.workshop.react

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactNativeHost

abstract class ReactFragment() : Fragment() {

    private var mDelegate: ReactActivityDelegate
    private var mActivity = activity as AppCompatActivity
    
    init {
        mDelegate = createReactActivityDelegate()
    }

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     * e.g. "MoviesApp"
     */
    abstract fun getMainComponentName(): String?


    /**
     * Called at construction time, override if you have a custom delegate implementation.
     */
    private fun createReactActivityDelegate(): ReactActivityDelegate {
        return ReactActivityDelegate(mActivity, getMainComponentName())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        mDelegate.onPause()
    }

    override fun onResume() {
        super.onResume()
        mDelegate.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelegate.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mDelegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun getReactNativeHost(): ReactNativeHost {
        return mDelegate.reactNativeHost
    }

    protected fun getReactInstanceManager(): ReactInstanceManager {
        return mDelegate.reactInstanceManager
    }

    protected fun loadApp(appKey: String) {
        mDelegate.loadApp(appKey)
    }
}