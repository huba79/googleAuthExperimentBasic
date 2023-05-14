package com.example.googleauthexperimentbasic.data

import java.io.Serializable

/**
 * @author huba
 */
class Role : Serializable {
    private var roleName: String? = null
    constructor(pRoleName:String){
        roleName=pRoleName
    }
    constructor()

    override fun toString(): String {
        return "Role(roleName=$roleName)"
    }

}