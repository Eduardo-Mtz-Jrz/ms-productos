package com.ms_products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // <--- Agrega esta anotación
public class MsProductsApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsProductsApplication.class, args);
	}
}
