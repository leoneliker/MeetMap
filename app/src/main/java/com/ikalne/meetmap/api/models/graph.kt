package com.ikalne.meetmap.api.models

import com.google.gson.annotations.SerializedName


data class graph(

  @SerializedName("@id"            ) var id2            : String?       = null,
  @SerializedName("@type"          ) var type          : String?       = null,
  @SerializedName("id"             ) var id             : String?       = null,
  @SerializedName("title"          ) var title          : String?       = null,
  @SerializedName("description"    ) var description    : String?       = null,
  @SerializedName("free"           ) var free           : Int?          = null,
  @SerializedName("price"          ) var price          : String?       = null,
  @SerializedName("dtstart"        ) var dtstart        : String?       = null,
  @SerializedName("dtend"          ) var dtend          : String?       = null,
  @SerializedName("time"           ) var time           : String?       = null,
  @SerializedName("excluded-days"  ) var excludedDays  : String?       = null,
  @SerializedName("audience"       ) var audience       : String?       = null,
  @SerializedName("uid"            ) var uid            : String?       = null,
  @SerializedName("link"           ) var link           : String?       = null,
  @SerializedName("event-location" ) var eventLocation : String?       = null,
  @SerializedName("references"     ) var references     : References?   = References(),
  @SerializedName("relation"       ) var relation       : Relation?     = Relation(),
  @SerializedName("address"        ) var address        : Address?      = Address(),
  @SerializedName("location"       ) var location       : Location?     = Location(),
  @SerializedName("organization"   ) var organization   : Organization? = Organization()

)