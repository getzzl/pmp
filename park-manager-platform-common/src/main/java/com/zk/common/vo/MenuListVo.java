package com.zk.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuListVo extends MenuListBaseVo {

    private List<MenuListVo> child;

}
