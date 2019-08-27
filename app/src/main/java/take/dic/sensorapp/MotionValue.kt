package take.dic.sensorapp

import take.dic.sensorapp.acceleration.AccelerationValue
import take.dic.sensorapp.direction.DirectionValue
import take.dic.sensorapp.gyro.GyroValue


class MotionValue {

    //motions[0]は加速度、[1]は角速度、[2]は方位
    var motions : MutableList<Motion?> = mutableListOf(Motion("加速度"), Motion("角速度"), Motion("方位"))

    //motionsの代わりにこっちを使うかも

    /*var acceleration = AccelerationValue("")
    var gyro = GyroValue("")
    var direction = DirectionValue("")*/


    //センサーが存在する加速度・角速度・方位情報について、すべてが新しい値を取得していたら更新
    fun updateData() {
        var judge = true
        for (motion in motions) {
            motion.let {
                if (it?.isUpdate == false) {
                    judge = false
                }
            }
        }

        if (judge) {
            for (motion in motions) {
                motion.let {
                    it?.update()
                }
            }
        }
    }
}

