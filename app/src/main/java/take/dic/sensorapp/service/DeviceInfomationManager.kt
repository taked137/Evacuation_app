package take.dic.sensorapp.service

import android.os.Build
import android.provider.Settings

object DeviceInfomationManager {
    // 端末情報
    var id = ""
    val model = Build.MODEL
    val version = Build.VERSION.RELEASE
    val system = "Android"

    // 搭載センサー情報
    var hasGps = false
    var hasAcceleration = false
    var hasDirection = false
    var hasGyro = false
    var hasBle = false
}
