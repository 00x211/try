package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

@SpringBootTest
class TryApplicationTests {
	public static void main(String[] args) {
		String data = "这是一条包含抖音的测试数据";
		String regex = ".*(抖音).*";
		boolean isMatch = Pattern.matches(regex, data);
		System.out.println("是否匹配: " + isMatch);
	}
	@Test
	void contextLoads() {
	}

}
