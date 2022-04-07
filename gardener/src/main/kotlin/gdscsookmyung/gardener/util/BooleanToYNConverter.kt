package gdscsookmyung.gardener.util

import javax.persistence.AttributeConverter

class BooleanToYNConverter: AttributeConverter<Boolean, String> {
    override fun convertToDatabaseColumn(bool: Boolean?): String {
        return if(bool != null && bool) "Y" else "N"
    }

    override fun convertToEntityAttribute(yn: String?): Boolean {
        return "Y".contentEquals(yn)
    }
}