package com.ysc3237.snapcat

import org.junit.Assert.*
import org.junit.Test

class UnitTest {
    @Test
    fun getDummyLatLng() {
        val latMax = 1.437690
        val latMin = 1.315238
        val lngMax = 104.014576
        val lngMin = 103.638152
        val divs = 100
        val guess = latMin+(((0..divs).random()).toDouble()/divs)*(latMax-latMin)
        assertNotEquals(guess, latMin+(((0..divs).random()).toDouble()/divs)*(latMax-latMin))
    }
}