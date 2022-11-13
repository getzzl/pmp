package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageVo {
	
	private Integer page;
	
	private Integer size;
	
	private String keyword;
	
}
