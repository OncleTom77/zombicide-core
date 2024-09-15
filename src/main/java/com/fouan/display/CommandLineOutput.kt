package com.fouan.display

import javax.inject.Named

@Named
class CommandLineOutput : Output {
    override fun display(value: String) {
        println(value)
    }
}
