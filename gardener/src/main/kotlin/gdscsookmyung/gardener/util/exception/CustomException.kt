package gdscsookmyung.gardener.util.exception

import java.lang.RuntimeException

class CustomException(
    val errorCode: ErrorCode
): RuntimeException() {

}