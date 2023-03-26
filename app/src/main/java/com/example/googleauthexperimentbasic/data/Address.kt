package com.example.googleauthexperimentbasic.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author huba
 */
class Address {
    @JsonProperty("addressType")
    private var addressType: AddressTypeEnum? = null

    @JsonProperty("country")
    private var country: String? = null

    @JsonProperty("county")
    private var county: String? = null

    @JsonProperty("settlement")
    private var settlement: String? = null

    @JsonProperty("street")
    private var street: String? = null

    @JsonProperty("streetNr")
    private var streetNr = 0

    @JsonProperty("staircase")
    private var staircase: String? = null

    @JsonProperty("apNr")
    private var apartment = 0

    constructor() {}

}