package com.example.googleauthexperimentbasic.data

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * @author huba
 */

class FinancialData : Serializable {
    @JsonProperty("bankAccountNr")
    private var bankAccountNr: String? = null

    constructor(createUserId: Long?, bankAccountNr: String?) {
        this.bankAccountNr = bankAccountNr
    }

    constructor() {}
}