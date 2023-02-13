package com.ikalne.meetmap.api.models

import com.google.gson.annotations.SerializedName


data class Organization (

  @SerializedName("organization-name" ) var organizationName : String? = null,
  @SerializedName("accesibility"      ) var accesibility      : String? = null

)