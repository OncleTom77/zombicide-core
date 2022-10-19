package com.fouan

import com.fouan.game.Game
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.util.*

@SpringBootApplication(scanBasePackages = ["com.fouan"])
class ZombicideApplication

fun main(args: Array<String>) {
    SpringApplication.run(ZombicideApplication::class.java, *args)
}

@Component
internal class GameStartup(
    private val game: Game,
) : CommandLineRunner {

    override fun run(args: Array<String>) = game.run()
}

@Configuration
internal class ZombicideConfig {

    @Bean
    fun random() = Random()
}
