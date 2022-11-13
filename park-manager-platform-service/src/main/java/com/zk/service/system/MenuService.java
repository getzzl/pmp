package com.zk.service.system;

import com.zk.common.vo.MenuListBaseVo;
import com.zk.common.vo.MenuListVo;
import com.zk.common.vo.MenuVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
public interface MenuService {

    void editMenu(MenuVo menuVo);

    void deleteMenu(Integer menuId);

    List<MenuListVo> getAllMenuList(Integer menuId,Integer isManagerMenu);

    void updateMenuStatus(Integer menuId, Integer menuStatus);

    void updateMenuHideStatus(Integer menuId, Integer hideStatus);

    Page<MenuListBaseVo> getMenuToSearch(Integer menuStatus, Integer page, Integer size, String keyword,Integer isManagerMenu);

}
