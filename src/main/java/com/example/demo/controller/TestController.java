package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;

@RestController
@RequestMapping("test")
public class TestController {
	@GetMapping
	public String testController() {
		return "Hello World!(안녕하세요!)";
	}

	@GetMapping("/testRequestBody")
	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
		return "Hello World! ID" + testRequestBodyDTO.getId() + " Message:  " + testRequestBodyDTO.getMessage();
	}
	
	@GetMapping("testResponseBody")
	public ResponseDTO<String> testControllerResponseBody(){
		List<String> list = new ArrayList<String>();
		list.add("Hello World! I'm ResponseDTO");
		list.add("See you!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}
}
