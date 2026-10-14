package com.mycompany.MoneyMatters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mycompany.Repository.SQLQueriesDDL;

@SpringBootApplication
@ComponentScan(basePackages = "com.mycompany.*")
public class MoneyMattersApplication {
	public static void main(String[] args) {
		SQLQueriesDDL.createTables();
		SpringApplication.run(MoneyMattersApplication.class, args);
	}
}