package com.rocky.springmvc;

import com.fasterxml.jackson.jr.ob.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@RequestMapping("/tests")
	@ResponseBody
	@SystemControllerLog(description = "测试方法") // 记录日志
	public String test(String name,String age ) {
		return name+","+age;
	}

	@RequestMapping("/addUser")
	@ResponseBody
	@SystemControllerLog(description = "添加用户") // 记录日志
	public Map<String,Object> addUser() {
		Map<String,Object>map=new HashMap<>();
		map.put("name","tom");
		map.put("age",10);
		return map;
	}

}
