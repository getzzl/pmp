package com.zk.service.system.impl;

import com.zk.common.constant.*;
import com.zk.common.domain.system.Menu;
import com.zk.common.vo.MenuListBaseVo;
import com.zk.common.vo.MenuListVo;
import com.zk.common.vo.MenuVo;
import com.zk.db.system.MenuRepository;
import com.zk.service.system.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zzl
 * @Date: 2022/11/10 0010
 */
@Service
public class MenuServiceImpl implements MenuService {


    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void editMenu(MenuVo menuVo) {
        Integer menuId = menuVo.getMenuId();

        Integer parentId = menuVo.getParentId();
        //如果父级为空或者为0的话
        if (parentId == null || SysConstants.MENU_PARENT_ID.equals(parentId)) {
            parentId = SysConstants.MENU_PARENT_ID;
        }
        checkSameMenuName(menuVo, parentId);


        //get ids
        String ids = parentId.equals(SysConstants.MENU_PARENT_ID)
                ? SysConstants.MENU_PARENT_ID + SysConstants.SPLIT_POINT
                : menuRepository.findByMenuIdAndDeletedStatus(parentId, SysConstants.DELETE_STATUS_ZERO)
                .orElseThrow(() -> new RuntimeException("未找到对应的父级菜单节点"))
                .getIds() + SysConstants.SPLIT_POINT;

        // builder
        Menu menuBuildr = Menu.builder()
                .menuId(menuId)
                .menuName(menuVo.getMenuName())
                .parentId(parentId)
                .path(menuVo.getPath())
                .menuType(MenuTypeEnum.checkMenuTypeValue(menuVo.getMenuType()))
                .icon(menuVo.getIcon())
                .remark(StringUtils.isEmpty(menuVo.getRemark()) ? "" : menuVo.getRemark())
                .outChainStatus(MenuOutChainStatusEnum.checkMenuOutChainStatusValue(menuVo.getOutChainStatus()))
                .outChainUrl(MenuOutChainStatusEnum.ENABLE_CHAIN.getValue().equals(menuVo.getMenuStatus()) ? menuVo.getOutChainUrl() : null)
                .hideStatus(MenuHideStatusEnum.checkMenuHideStatusValue(menuVo.getHideStatus()))
                .menuStatus(MenuStatusEnum.checkMenuStatusValue(menuVo.getMenuStatus()))
                .deletedStatus(SysConstants.DELETE_STATUS_ZERO)
                .order(menuVo.getOrder())
                .updateTime(new Date())
                .createTime(new Date())
                .isManagerMenu(MenuIsManagerTypeEnum.checkIsMenuTypeValue(menuVo.getIsManagerMenu()))
                .ids(ids)
                .menuCode(menuVo.getMenuCode())
                .build();

//        if (menuId != null) {
//            //update
//            menuBuildr.setMenuId(menuId);
//            this.menuRepository.findByMenuIdAndDeletedStatus(menuId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> new RuntimeException("未找到对应操作的菜单或者菜单已删除"));
//            checkHideStatus(MenuHideStatusEnum.checkMenuHideStatusValue(menuVo.getHideStatus()), menuExist.getIds());
//            checkMenuStatus(MenuStatusEnum.checkMenuStatusValue(menuVo.getMenuStatus()), menuExist.getIds());
//            checkDeletedStatus(menuVo.getDeletedStatus(), menuExist.getIds());

//        }

        Menu menuSave = this.menuRepository.save(menuBuildr);

    }

//    private void checkDeletedStatus(Integer deletedStatus, String ids) {
//        if (SysConstants.DELETE_STATUS_ONE.equals(deletedStatus)) {
//            //删除状态
//            List<Menu> allChildMenu = findByIdsList(ids);
//            if (!CollectionUtils.isEmpty(allChildMenu)) {
//                List<Menu> updateMenu = allChildMenu.stream().map(e -> {
//                    e.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
//                    return e;
//                }).collect(Collectors.toList());
//                this.menuRepository.saveAll(updateMenu);
//            }
//        }
//    }
//
//    private void checkMenuStatus(Integer checkMenuStatusValue, String ids) {
//        if (MenuStatusEnum.MENU_DIS_ENABLE.getValue().equals(checkMenuStatusValue)) {
//            //禁用状态
//            List<Menu> allChildMenu = findByIdsList(ids);
//            if (!CollectionUtils.isEmpty(allChildMenu)) {
//                List<Menu> updateMenu = allChildMenu.stream().map(e -> {
//                    e.setMenuStatus(MenuStatusEnum.MENU_DIS_ENABLE.getValue());
//                    return e;
//                }).collect(Collectors.toList());
//                this.menuRepository.saveAll(updateMenu);
//            }
//        } else {
//            //启用状态
//        }
//    }
//
//    private void checkHideStatus(Integer checkMenuHideStatusValue, String ids) {
//        if (MenuHideStatusEnum.HIDE_STATUS.getValue().equals(checkMenuHideStatusValue)) {
//            //隐藏状态
//            List<Menu> allChildMenu = findByIdsList(ids);
//            if (!CollectionUtils.isEmpty(allChildMenu)) {
//                List<Menu> updateMenu = allChildMenu.stream().map(e -> {
//                    e.setHideStatus(MenuHideStatusEnum.HIDE_STATUS.getValue());
//                    return e;
//                }).collect(Collectors.toList());
//                this.menuRepository.saveAll(updateMenu);
//            }
//        }
//    }

    private List<Menu> findByIdsList(String ids) {
        return this.menuRepository.findByIdsLike(ids + "%");
    }


    private void checkSameMenuName(MenuVo menuVo, Integer parentId) {

        List<Menu> menuList;
        if (menuVo.getMenuId() == null) {
            //新增时候的校验
            menuList = this.menuRepository.findByParentIdAndDeletedStatusAndMenuStatusAndHideStatusAndMenuName
                    (parentId, SysConstants.DELETE_STATUS_ZERO, SysConstants.MENU_STATUS_ENABLE, SysConstants.MENU_HIDE_STATUS_ZERO, menuVo.getMenuName());
        } else {
            //修改时候的校验
            menuList = this.menuRepository.findByParentIdAndDeletedStatusAndMenuStatusAndHideStatusAndMenuNameAndMenuIdNot
                    (parentId, SysConstants.DELETE_STATUS_ZERO, SysConstants.MENU_STATUS_ENABLE, SysConstants.MENU_HIDE_STATUS_ZERO, menuVo.getMenuName(), menuVo.getMenuId());

        }
        if (!CollectionUtils.isEmpty(menuList)) {
            //不为空的话 说明名称重复
            throw new RuntimeException("同一部门下存在相同的名称，请核对后在添加");
        }

    }

    @Override
    public void deleteMenu(Integer menuId) {
        Menu menuParent = this.menuRepository.findByMenuIdAndDeletedStatus(menuId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> {
            throw new RuntimeException("删除的菜单节点不存在或者已被删除");
        });
        menuParent.setDeletedStatus(SysConstants.DELETE_STATUS_ONE);
        menuRepository.save(menuParent);
    }

    @Override
    public List<MenuListVo> getAllMenuList(Integer menuId,Integer isManagerMenu) {


        Integer isMenuTypeValue = MenuIsManagerTypeEnum.checkIsMenuTypeValue(isManagerMenu);

        if (menuId < 0) {
            throw new RuntimeException("菜单id不合法");
        }
        if (SysConstants.MENU_PARENT_ID.equals(menuId)) {
            //查询所有的菜单
            List<Menu> menuParent = this.menuRepository.findByParentIdAndIsManagerMenuAndDeletedStatus(menuId, isMenuTypeValue, SysConstants.DELETE_STATUS_ZERO);

            return menuParent.stream().map(e -> {
                MenuListVo menuListVo = new MenuListVo();

                BeanUtils.copyProperties(e, menuListVo);
                MenuListVo child = getChildNodesByParentNode(menuListVo,isMenuTypeValue);
                menuListVo.setChild(child.getChild());
                return menuListVo;
            }).collect(Collectors.toList());

        } else {
            //非根节点 for:菜单管理
            MenuListVo menuListVo = new MenuListVo();
            Menu menuParent = this.menuRepository.findByMenuIdAndDeletedStatus(menuId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> {
                throw new RuntimeException("查询的菜单节点不存在或者已被删除");
            });
            BeanUtils.copyProperties(menuParent, menuListVo);
            MenuListVo child = getChildNodesByParentNode(menuListVo,isMenuTypeValue);
            menuListVo.setChild(child.getChild());
            return Collections.singletonList(menuListVo);
        }

    }

    @Override
    public void updateMenuStatus(Integer menuId, Integer menuStatus) {
        Menu menuParent = this.menuRepository.findByMenuIdAndDeletedStatus(menuId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> {
            throw new RuntimeException("修改的菜单节点不存在或者已被删除");
        });

        menuParent.setMenuStatus(MenuStatusEnum.checkMenuStatusValue(menuStatus));

        menuRepository.save(menuParent);

    }

    @Override
    public void updateMenuHideStatus(Integer menuId, Integer hideStatus) {
        Menu menuParent = this.menuRepository.findByMenuIdAndDeletedStatus(menuId, SysConstants.DELETE_STATUS_ZERO).orElseThrow(() -> {
            throw new RuntimeException("修改的菜单节点不存在或者已被删除");
        });

        menuParent.setHideStatus(MenuHideStatusEnum.checkMenuHideStatusValue(hideStatus));

        menuRepository.save(menuParent);
    }

    @Override
    public Page<MenuListBaseVo> getMenuToSearch(Integer menuStatus, Integer page, Integer size, String keyword,Integer isManagerMenu) {
        final String finalKeyword = keyword;
        final Integer finalMenuStatus = MenuStatusEnum.checkMenuStatusValue(menuStatus);
        Integer isMenuTypeValue = MenuIsManagerTypeEnum.checkIsMenuTypeValue(isManagerMenu);
        Page<Menu> menus = this.menuRepository.findAll((root, query, build) -> {
            ArrayList<Predicate> arrayList = new ArrayList<>();

            if (!StringUtils.isEmpty(finalKeyword)) {
                //构建条件 like --or
                arrayList.add(build.like(root.get("menuName"), "%" + finalKeyword + "%"));
            }
            if (finalMenuStatus != null) {
                arrayList.add(build.equal(root.get("menuStatus"), menuStatus));
            }
            if (isMenuTypeValue != null) {
                arrayList.add(build.equal(root.get("isManagerMenu"), isMenuTypeValue));
            }
            Predicate[] predicates = new Predicate[arrayList.size()];
            return build.and(arrayList.toArray(predicates));
        }, PageRequest.of(page == null || page - 1 < 0 ? 0 : page - 1, size == null ? 20 : size, Sort.by(Sort.Direction.DESC, "createTime")));
        List<MenuListBaseVo> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(menus.getContent())) {
            result = menus.getContent().stream().map(menu -> {
                MenuListBaseVo menuListBaseVo = new MenuListBaseVo();
                BeanUtils.copyProperties(menu, menuListBaseVo);
                if (!SysConstants.MENU_PARENT_ID.equals(menu.getParentId())) {
                    this.menuRepository.findByMenuIdAndDeletedStatus(menu.getParentId(), SysConstants.DELETE_STATUS_ZERO).ifPresent(menuParent -> {
                        menuListBaseVo.setParentName(menuParent.getMenuName());
                    });
                }
                return menuListBaseVo;
            }).collect(Collectors.toList());
        }
        return new PageImpl<>(result, menus.getPageable(), menus.getTotalElements());
    }


    private MenuListVo getChildNodesByParentNode(MenuListVo menuListVo,Integer isManagerType) {

        List<Menu> menuChildList = this.menuRepository.findByParentIdAndIsManagerMenuAndDeletedStatus(menuListVo.getMenuId(),isManagerType, SysConstants.DELETE_STATUS_ZERO);
        if (!CollectionUtils.isEmpty(menuChildList)) {
            menuListVo.setChild(menuChildList.stream().map(e -> {
                MenuListVo menuListVoInner = new MenuListVo();
                BeanUtils.copyProperties(e, menuListVoInner);
                MenuListVo childNodesByParentNode = getChildNodesByParentNode(menuListVoInner,isManagerType);
                menuListVoInner.setChild(childNodesByParentNode.getChild());
                return menuListVoInner;
            }).collect(Collectors.toList()));
        } else {
            menuListVo.setChild(Collections.emptyList());
        }
        return menuListVo;
    }
}
