package take.dic.sensorapp

import android.databinding.BaseObservable
import android.databinding.Bindable

class GPSValue(val title: String, var latitude: String, var longitude: String) : BaseObservable(){
    @get:Bindable
    var a: String = "0"
        set(value){
            field = value
            notifyPropertyChanged(BR.a)
        }
}