package take.dic.sensorapp.service

enum class RealmStatus (val statusCode: Byte){
    ALL_SENT(0x01),
    REGULAR_SENT(0x10)
}