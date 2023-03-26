package com.example.googleauthexperimentbasic

import com.example.googleauthexperimentbasic.data.Address
import com.example.googleauthexperimentbasic.data.ContactData
import com.example.googleauthexperimentbasic.data.FinancialData
import com.example.googleauthexperimentbasic.data.Role
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

class UserResponse {
    @JsonProperty("userId")
    private var id: Long? = null

    @JsonProperty("firstName")
    private var firstName: String? = null

    @JsonProperty("lastName")
    private var lastName: String? = null

    @JsonProperty("displayName")
    private var displayName: String? = null

    @JsonProperty("loginName")
    private var loginName: String? = null

    @JsonProperty("active")
    private var active: Boolean? = null

    @JsonProperty("address")
    @JsonSerialize(`as` = Address::class)
    private var address: Address? = null

    @JsonProperty("contactData")
    @JsonSerialize(`as` = ContactData::class)
    private var contactData: ContactData? = null

    @JsonProperty("roles")
    private var roles: List<Role>? = null

    @JsonProperty("financialData")
    @JsonSerialize(`as` = FinancialData::class)
    private var financialData: FinancialData? = null

    constructor() {}
    constructor(
        id: Long?,
        firstName: String?,
        lastName: String?,
        displayName: String?,
        loginName: String?,
        active: Boolean?,
        address: Address?,
        contactData: ContactData?,
        roles: List<Role>?,
        financialData: FinancialData?
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.displayName = displayName
        this.loginName = loginName
        this.active = active
        this.address = address
        this.contactData = contactData
        this.roles = roles
        this.financialData = financialData
    }

}
