package com.workshop

import com.facebook.react.ReactPackage
import com.reactnativenavigation.NavigationApplication
import com.reactnativenavigation.react.NavigationReactNativeHost
import com.reactnativenavigation.react.ReactGateway

class MainApplication : NavigationApplication() {

    override fun isDebug() = BuildConfig.DEBUG

    override fun createReactGateway(): ReactGateway {
        val host = object : NavigationReactNativeHost(this, isDebug, createAdditionalReactPackages()) {
            override fun getJSBundleFile(): String? {
                return "index"
            }

            override fun getJSMainModuleName(): String {
                return "index"
            }
            
        }
        return ReactGateway(this, isDebug, host)
    }


    private fun getPackages(): List<ReactPackage> {
        return emptyList()
    }

    override fun createAdditionalReactPackages() = getPackages()
}