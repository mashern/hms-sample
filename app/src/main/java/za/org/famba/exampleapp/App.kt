package za.org.famba.exampleapp

import android.app.Application
import com.huawei.hms.maps.MapsInitializer
import com.huawei.agconnect.AGConnectOptionsBuilder

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val apiKey = AGConnectOptionsBuilder().build(this).getString("client/api_key")
        MapsInitializer.setApiKey(apiKey)
    }
}