package com.melardev.android.crud.datasource.remote.dtos.responses

import com.google.gson.annotations.SerializedName

open class BaseDataResponse {
    @SerializedName("success")
    var isSuccess: Boolean = false
    @SerializedName("full_messages")
    lateinit var fullMessages: Array<String>

    constructor(success: Boolean) {
        this.isSuccess = success
    }

    constructor() {}
}
