package take.dic.sensorapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import take.dic.sensorapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* 上から
        * 加速度取得用フラグメント
        * 角速度取得用フラグメント
        * GPSを用いた位置情報(経度・緯度)取得用フラグメント
           を追加している
        */

        /*
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.AccelerationContainer, AccelerationSensorFragment.createInstance("渡したい文字列"))
            transaction.add(R.id.GyroContainer, GyroSensorFragment.createInstance("渡したい文字列"))
            transaction.add(R.id.GPSContainer, GPSFragment.createInstance("渡したい文字列"))
            transaction.add(R.id.OrientationContainer, OrientationFragment.createInstance("渡したい文字列"))
            transaction.commit()
        }
        */


        supportActionBar!!.hide()

        // 画像を表示するfragmentを追加します(雑)
        //supportFragmentManager.beginTransaction().add(R.id.container, ImageFragment()).commit()
    }
}