package take.dic.sensorapp

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import take.dic.sensorapp.databinding.FragmentValueBinding

class ValueFragment : Fragment(){
    val gps = GPSValue(title = "GPS", latitude = "緯度", longitude = "経度")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentValueBinding.inflate(inflater, container, false)
        binding.gps = gps

        return binding.root
        /*
        val view = inflater.inflate(R.layout.fragment_value, container, false)
        val data = mutableListOf<String>()
        data.add("1")
        data.add("1")
        for(i in 1..20)
            data.add("1")

        val listView = view.findViewById<ListView>(R.id.listview)
        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, data)
        listView.adapter = arrayAdapter

        data.add("final")
        activity!!.runOnUiThread{
            arrayAdapter.notifyDataSetChanged()
        }
        return view
        */
    }
}