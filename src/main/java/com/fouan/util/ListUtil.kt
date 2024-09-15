package com.fouan.util

fun <T> mergeLists(vararg lists: List<T>): List<T> {
    return listOf(*lists).flatten()
}