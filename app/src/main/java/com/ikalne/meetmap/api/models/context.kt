package com.ikalne.meetmap.api.models

import com.google.gson.annotations.SerializedName


data class context (

  @SerializedName("c"                 ) var c                 : String? = null,
  @SerializedName("dcterms"           ) var dcterms           : String? = null,
  @SerializedName("geo"               ) var geo               : String? = null,
  @SerializedName("loc"               ) var loc               : String? = null,
  @SerializedName("org"               ) var org               : String? = null,
  @SerializedName("vcard"             ) var vcard             : String? = null,
  @SerializedName("schema"            ) var schema            : String? = null,
  @SerializedName("title"             ) var title             : String? = null,
  @SerializedName("id"                ) var id                : String? = null,
  @SerializedName("relation"          ) var relation          : String? = null,
  @SerializedName("references"        ) var references        : String? = null,
  @SerializedName("address"           ) var address           : String? = null,
  @SerializedName("area"              ) var area              : String? = null,
  @SerializedName("district"          ) var district          : String? = null,
  @SerializedName("locality"          ) var locality          : String? = null,
  @SerializedName("postal-code"       ) var postal       : String? = null,
  @SerializedName("street-address"    ) var street    : String? = null,
  @SerializedName("location"          ) var location          : String? = null,
  @SerializedName("latitude"          ) var latitude          : String? = null,
  @SerializedName("longitude"         ) var longitude         : String? = null,
  @SerializedName("organization"      ) var organization      : String? = null,
  @SerializedName("organization-desc" ) var organizationDesc : String? = null,
  @SerializedName("accesibility"      ) var accesibility      : String? = null,
  @SerializedName("services"          ) var services          : String? = null,
  @SerializedName("schedule"          ) var schedule          : String? = null,
  @SerializedName("organization-name" ) var organizationName : String? = null,
  @SerializedName("description"       ) var description       : String? = null,
  @SerializedName("link"              ) var link              : String? = null,
  @SerializedName("uid"               ) var uid               : String? = null,
  @SerializedName("dtstart"           ) var dtstart           : String? = null,
  @SerializedName("dtend"             ) var dtend             : String? = null,
  @SerializedName("time"              ) var time              : String? = null,
  @SerializedName("excluded-days"     ) var excludedDays     : String? = null,
  @SerializedName("event-location"    ) var eventLocation    : String? = null,
  @SerializedName("free"              ) var free              : String? = null,
  @SerializedName("price"             ) var price             : String? = null,
  @SerializedName("recurrence"        ) var recurrence        : String? = null,
  @SerializedName("days"              ) var days              : String? = null,
  @SerializedName("frequency"         ) var frequency         : String? = null,
  @SerializedName("interval"          ) var interval          : String? = null,
  @SerializedName("audience"          ) var audience          : String? = null

)