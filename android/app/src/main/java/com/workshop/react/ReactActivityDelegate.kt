package com.workshop.react

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import com.facebook.infer.annotation.Assertions
import com.facebook.react.*
import com.facebook.react.bridge.Callback
import com.facebook.react.devsupport.DoubleTapReloadRecognizer
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.core.PermissionListener

open class ReactActivityDelegate {

    private val mActivity: Activity?
    private val mMainComponentName: String?

    private var mReactRootView: ReactRootView? = null
    private var mDoubleTapReloadRecognizer: DoubleTapReloadRecognizer? = null
    private var mPermissionListener: PermissionListener? = null
    private var mPermissionsCallback: Callback? = null

    protected val launchOptions: Bundle?
        get() = null

    /**
     * Get the [ReactNativeHost] used by this app. By default, assumes
     * [Activity.getApplication] is an instance of [ReactApplication] and calls
     * [ReactApplication.getReactNativeHost]. Override this method if your application class
     * does not implement `ReactApplication` or you simply have a different mechanism for
     * storing a `ReactNativeHost`, e.g. as a static field somewhere.
     */
    val reactNativeHost: ReactNativeHost
        get() = (plainActivity.application as ReactApplication).reactNativeHost

    val reactInstanceManager: ReactInstanceManager
        get() = reactNativeHost.reactInstanceManager

    protected val context: Context
        get() = Assertions.assertNotNull(mActivity)

    protected val plainActivity: Activity
        get() = context as Activity

    constructor(activity: Activity, mainComponentName: String?) {
        mActivity = activity
        mMainComponentName = mainComponentName
    }

    constructor(activity: ReactActivity, mainComponentName: String?) {
        mActivity = activity
        mMainComponentName = mainComponentName
    }

    protected fun createRootView(): ReactRootView {
        return ReactRootView(context)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        if (mMainComponentName != null) {
            loadApp(mMainComponentName)
        }
        mDoubleTapReloadRecognizer = DoubleTapReloadRecognizer()
    }

    fun loadApp(appKey: String) {
        if (mReactRootView != null) {
            throw IllegalStateException("Cannot loadApp while app is already running.")
        }
        mReactRootView = createRootView()
        mReactRootView!!.startReactApplication(
                reactNativeHost.reactInstanceManager,
                appKey,
                launchOptions)
        plainActivity.setContentView(mReactRootView)
    }

    fun onPause() {
        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager.onHostPause(plainActivity)
        }
    }

    fun onResume() {
        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager.onHostResume(
                    plainActivity,
                    plainActivity as DefaultHardwareBackBtnHandler)
        }

        if (mPermissionsCallback != null) {
            mPermissionsCallback!!.invoke()
            mPermissionsCallback = null
        }
    }

    fun onDestroy() {
        if (mReactRootView != null) {
            mReactRootView!!.unmountReactApplication()
            mReactRootView = null
        }
        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager.onHostDestroy(plainActivity)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager
                    .onActivityResult(plainActivity, requestCode, resultCode, data)
        }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (reactNativeHost.hasInstance()
                && reactNativeHost.useDeveloperSupport
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            event.startTracking()
            return true
        }
        return false
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (reactNativeHost.hasInstance() && reactNativeHost.useDeveloperSupport) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                reactNativeHost.reactInstanceManager.showDevOptionsDialog()
                return true
            }
            val didDoubleTapR = Assertions.assertNotNull(mDoubleTapReloadRecognizer)
                    .didDoubleTapR(keyCode, plainActivity.currentFocus)
            if (didDoubleTapR) {
                reactNativeHost.reactInstanceManager.devSupportManager.handleReloadJS()
                return true
            }
        }
        return false
    }

    fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (reactNativeHost.hasInstance()
                && reactNativeHost.useDeveloperSupport
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            reactNativeHost.reactInstanceManager.showDevOptionsDialog()
            return true
        }
        return false
    }

    fun onBackPressed(): Boolean {
        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager.onBackPressed()
            return true
        }
        return false
    }

    fun onNewIntent(intent: Intent): Boolean {
        if (reactNativeHost.hasInstance()) {
            reactNativeHost.reactInstanceManager.onNewIntent(intent)
            return true
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissions(
            permissions: Array<String>,
            requestCode: Int,
            listener: PermissionListener) {
        mPermissionListener = listener
        plainActivity.requestPermissions(permissions, requestCode)
    }

    fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        mPermissionsCallback = Callback {
            if (mPermissionListener != null && mPermissionListener!!.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                mPermissionListener = null
            }
        }
    }
}
