package com.dev_forum

import com.dev_forum.resources.mail.EmailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*


@SpringBootApplication
class DevForumApplication

fun main(args: Array<String>) {
	runApplication<DevForumApplication>(*args)
	println("TIRA A SENHA SLK")
}
