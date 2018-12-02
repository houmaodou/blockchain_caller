package com.taikang.pension.blockchain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.taikang.pension.blockchain.admin.dao")
public class BlockchainCallerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockchainCallerApplication.class, args);
	}
}
