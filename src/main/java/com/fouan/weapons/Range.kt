package com.fouan.weapons

class Range(val min: Int, val max: Int) {

    operator fun contains(value: Int): Boolean {
        return value in min..max
    }
}
