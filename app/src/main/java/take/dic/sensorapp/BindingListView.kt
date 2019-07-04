package take.dic.sensorapp

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.ListView

class BindingListView(context: Context, attrs: AttributeSet) : ListView(context, attrs) {
    private var adapter: ArrayAdapter<String>? = null

    fun setList(list: ArrayList<String>) {
        if(adapter == null){
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
            setAdapter(adapter)
        }
        adapter!!.notifyDataSetChanged()
    }
}