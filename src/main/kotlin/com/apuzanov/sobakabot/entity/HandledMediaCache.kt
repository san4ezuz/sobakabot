package com.apuzanov.sobakabot.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "handled_media_cache")
data class HandledMediaCache(

    @Column
    val chatId: String,

    @Column
    val userId: String,

    @Column
    val fileId: String,

    @Column(name="added_date")
    val date: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0
)