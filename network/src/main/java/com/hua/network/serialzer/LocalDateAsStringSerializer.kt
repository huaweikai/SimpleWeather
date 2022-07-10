package com.hua.network.serialzer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.util.*

class LocalDateAsStringSerializer:KSerializer<Date> {
    override fun deserialize(decoder: Decoder): Date {
        return DateFormat.getDateInstance().parse(decoder.decodeString())?: Date(1L)
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("date",PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.toString())
    }
}