package com.apuzanov.sobakabot.repository

import com.apuzanov.sobakabot.entity.MediaCache
import org.springframework.data.jpa.repository.JpaRepository

interface MediaCacheRepository : JpaRepository<MediaCache, Int> {
    fun findByDigest(digest: String): MediaCache?
}
