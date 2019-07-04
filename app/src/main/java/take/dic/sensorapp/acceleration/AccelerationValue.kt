package take.dic.sensorapp.acceleration

import android.databinding.BaseObservable
import android.databinding.Bindable
import take.dic.sensorapp.BR

class AccelerationValue(val title: String, var x: String, var y: String, var z: String) : BaseObservable(){
    @get:Bindable
    var a: String = "0"
        set(value){
            field = value
            notifyPropertyChanged(BR.a)
        }
}