package take.dic.sensorapp.gps

import android.databinding.BaseObservable
import android.databinding.Bindable
import take.dic.sensorapp.BR

class GPSValue(val title: String, var latitude: String, var longitude: String) : BaseObservable(){
    @get:Bindable
    var a: String = "0"
        set(value){
            field = value
            notifyPropertyChanged(BR.a)
        }
}