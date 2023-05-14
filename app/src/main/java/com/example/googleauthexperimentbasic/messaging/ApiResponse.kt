package com.example.googleauthexperimentbasic.messaging

import com.fasterxml.jackson.annotation.JsonProperty

class ApiResponse<T> {
    @JsonProperty("status")
    private var status : String? = null
    @JsonProperty("errors")
    private var errors: String? = null
    @JsonProperty("data")
    private var data: T? = null

    constructor(pStatus:String, pErrors:String, pData:T) {
        status = pStatus
        errors=pErrors
        data = pData
    }
    constructor()

    override fun toString(): String {
        return "ApiResponse(status=$status, errors=$errors, data=$data)"
    }

}