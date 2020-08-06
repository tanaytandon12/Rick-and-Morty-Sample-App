package com.weather.willy.willyweathersample.model

import com.weather.willy.willyweathersample.model.local.getEpisodeIdFromUrl
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test

class EpisodeTest {

    @Test
    fun episodeIdFromUrl() {
        val url = "https://abc.com/123"
        assertEquals(url.getEpisodeIdFromUrl(), 123)
    }
}