package com.apuzanov.sobakabot.utils

import com.apuzanov.sobakabot.enums.UserGender

fun UserGender.getFormByGender(male: String, female: String, it: String) =
    when (this) {
        UserGender.MALE -> male
        UserGender.FEMALE -> female
        UserGender.IT -> it
    }
