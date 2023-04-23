package com.fouan.actors

data class ActorId(val value: String = getNextId()) {
    companion object {
        private var nextId = 0

        @JvmStatic
        fun getNextId(): String {
            return (nextId++).toString()
        }
    }
}
