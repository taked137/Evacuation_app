package take.dic.sensorapp.service

import android.graphics.Point
import android.os.Build

object DeviceInformationManager {
    // 端末情報
    var id = ""
    var size = Point()
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
