package com.example.equalizeme.services.profiles

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.equalizeme.model.UserProfileList

import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserProfileListSerializer : Serializer<UserProfileList> {
    override val defaultValue: UserProfileList
        get() = UserProfileList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProfileList {
        try {
            return UserProfileList.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: UserProfileList, output: OutputStream) {
        t.writeTo(output)
    }
}