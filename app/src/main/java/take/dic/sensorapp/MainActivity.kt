package take.dic.sensorapp

import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import io.realm.Realm
import org.altbeacon.beacon.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), BeaconConsumer {


    private lateinit var listView: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        //listView = findViewById(R.id.listview)
        //arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        //listView.adapter = arrayAdapter

        Realm.init(this)
    }

    /*
    override fun onResume() {
        super.onResume()
        beaconManager.bind(this)
    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)
    }
    */

    override fun onBeaconServiceConnect() {

    }

    // 試験用(削除予定)
    override fun onStop() {

        realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(BeaconModel::class.java)
        realm.commitTransaction()
        realm.close()

        super.onStop()
    }
}