package com.task.task

import org.modelmapper.Conditions
import org.modelmapper.ModelMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@SpringBootApplication
@EnableScheduling
class TaskApplication

	fun main(args: Array<String>) {
		runApplication<TaskApplication>(*args)
	}

@Configuration
@EnableWebSecurity
class SecurityConfig() : WebSecurityConfigurerAdapter() {
	 @Throws(Exception::class)
	 override fun configure(security: HttpSecurity) {
		 security.httpBasic().disable()
		 http .csrf().disable() .authorizeRequests() .anyRequest().permitAll()
	 }

	 @Bean
	fun modelMapper():ModelMapper{
		val modelMapper = ModelMapper()
		modelMapper.configuration.isAmbiguityIgnored = true
		modelMapper.configuration.propertyCondition = Conditions.isNotNull()
		return modelMapper
	}
}

