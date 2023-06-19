package com.ikalne.meetmap.api.models

import com.google.gson.annotations.SerializedName


data class Area (

  @SerializedName("@id"            ) var id            : String? = null,
  @SerializedName("locality"       ) var locality       : String? = null,
  @SerializedName("postal-code"    ) var postalCode    : String? = null,
  @SerializedName("street-address" ) var streetAddress : String? = null

)