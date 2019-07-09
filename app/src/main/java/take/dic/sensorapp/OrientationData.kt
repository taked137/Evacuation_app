package take.dic.sensorapp

data class OrientationData (
    var xAttitude : Float, // x軸の傾き
    var yAttitude : Float, // y軸の傾き
    var zAttitude : Float // z軸の傾き これが実生活でよく使われる"方角"
)