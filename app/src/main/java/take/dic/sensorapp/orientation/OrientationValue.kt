package take.dic.sensorapp.orientation

import android.databinding.BaseObservable
import android.databinding.Bindable
import take.dic.sensorapp.BR

class OrientationValue(val title: String, var x: String, var y: String, var z: String) : BaseObservable(){
    @get:Bindable
    var a: String = "0"
        set(value){
            field = value
            notifyPropertyChanged(BR.a)
        }
}