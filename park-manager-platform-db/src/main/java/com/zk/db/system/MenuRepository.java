package com.zk.db.system;

import com.zk.common.domain.system.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author: zzl
 * @Date: 2022/11/8 0008
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {


    List<Menu> findByMenuIdInAndDeletedStatus(List<Integer> menuIds, Integer deletedStatus);

    List<Menu> findByIdsLike(String ids);

    Optional<Menu> findByMenuIdAndDeletedStatus(Integer menuId, Integer deletedStatus);


    List<Menu> findByParentIdAndDeletedStatus(Integer menuId, Integer deletedStatus);

    List<Menu> findByParentIdAndIsManagerMenuAndDeletedStatus(Integer menuId,Integer isManagerMenu,Integer deletedStatus);

    List<Menu> findByParentIdAndDeletedStatusAndMenuStatusAndHideStatusAndMenuName(Integer parentId, Integer deletedStatus, Integer menuStatus, Integer hideStatus, String menuName);


    List<Menu> findByParentIdAndDeletedStatusAndMenuStatusAndHideStatusAndMenuNameAndMenuIdNot(Integer parentId, Integer deleteStatusZero, Integer menuStatusEnable, Integer menuHideStatusZero, String menuName, Integer menuId);
}
