package com.fouan.actors

import org.jheaps.annotations.VisibleForTesting

data class ActorId(val value: String = getNextId()) {
    companion object {
        private var nextId = 0

        @JvmStatic
        fun getNextId(): String {
            return (nextId++).toString()
        }

        @VisibleForTesting
        fun resetId() {
            nextId = 0;
        }
    }
}
