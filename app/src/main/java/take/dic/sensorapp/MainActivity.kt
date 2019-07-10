package take.dic.sensorapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        // 画像を表示するfragmentを追加します(雑)
        //supportFragmentManager.beginTransaction().add(R.id.container, ImageFragment()).commit()
    }
}