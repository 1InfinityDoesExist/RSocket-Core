package com.rsocket.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}


/*
   wget https://github.com/making/rsc/releases/download/0.5.0/rsc-linux-x86_64
   mv rsc-linux-x86_64 rsc
   chmod +x rsc
   ./rsc --version

*/