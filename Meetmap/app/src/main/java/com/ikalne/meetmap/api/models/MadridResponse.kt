package com.ikalne.meetmap.api.models

import com.google.gson.annotations.SerializedName


data class MadridResponse (

  @SerializedName("@context" ) var context : context = context(),
  @SerializedName("@graph"   ) var graph   : ArrayList<graph> = arrayListOf()

)