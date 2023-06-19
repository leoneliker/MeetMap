package com.ikalne.meetmap.api.services

import DOMAIN_URL
import com.ikalne.meetmap.api.models.MadridResponse
import java.net.URL

public class MadridApiRequestManager {
    fun getAll(): MadridResponse {
        return getUrlObject(URL(DOMAIN_URL),MadridResponse::class.java)
    }
}