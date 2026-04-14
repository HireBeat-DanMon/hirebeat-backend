package com.ktor.api.hirebeat.modules.profile.domain.model

import com.ktor.api.hirebeat.modules.catalogs.domain.model.Instrument
import com.ktor.api.hirebeat.modules.catalogs.domain.model.Level

data class ProfileInstrument(
    val instrument: Instrument,
    val level: Level,
    val isPrincipal: Boolean
)
