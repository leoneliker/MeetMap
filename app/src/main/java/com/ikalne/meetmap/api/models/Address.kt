package com.ikalne.meetmap.api.models

import com.google.gson.annotations.SerializedName


data class Address (

  @SerializedName("district" ) var district : District? = District(),
  @SerializedName("area"     ) var area     : Area?     = Area()

)