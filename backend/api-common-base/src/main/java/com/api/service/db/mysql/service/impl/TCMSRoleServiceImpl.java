package com.api.service.db.mysql.service.impl;

import com.api.service.AdminName;
import com.api.service.UUID;
import com.api.service.db.mysql.mapper.TCMSRoleMapper;
import com.api.service.db.mysql.service.ITCMSRoleMenuService;
import com.api.service.db.mysql.service.ITCMSRoleService;
import com.api.service.db.mysql.service.ITCMSUserService;
import com.api.service.db.mysql.util.PageUtils;
import com.api.service.db.mysql.util.Query;
import com.api.service.db.mysql.util.beanToMap.BeanMapUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author Ashy.Cheung
 * @date 2020.07.20
 */
@Service("ITCMSRoleService")
public class TCMSRoleServiceImpl extends ServiceImpl<TCMSRoleMapper, TCmsRoleEntity> implements ITCMSRoleService {
    @Autowired
    private ITCMSRoleMenuService itRoleMenuService;
    @Autowired
    private ITCMSUserService itUserService;


    @Override
    public PageUtils queryPage(TCmsRoleEntity entity) {
        Page<TCmsRoleEntity> page = this.selectPage(
                new Query<TCmsRoleEntity>(BeanMapUtil.toPageQuery(entity)).getPage(),
                new EntityWrapper<TCmsRoleEntity>()
                        .where("1=1")
                        .like(StringUtils.isNotBlank(entity.getRoleName()), "role_name", entity.getRoleName())
                        .eq(entity.getCreateUserId() != null, "create_user_id", entity.getCreateUserId())
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(TCmsRoleEntity entity) {
        entity.setCreateTime(new Date());
        entity.setRoleId(UUID.randomUUID());
        entity.setCreateTime(new Date());
        this.insert(entity);
        //????????????????????????
        checkPrems(entity);
        //???????????????????????????
        itRoleMenuService.saveOrUpdate(entity.getRoleId(), this.getRoleMenuIdList(entity));
    }

    @Override
    public void update(TCmsRoleEntity entity) {
        this.updateById(entity);
        //????????????????????????
        checkPrems(entity);
        //???????????????????????????
        itRoleMenuService.saveOrUpdate(entity.getRoleId(), this.getRoleMenuIdList(entity));
    }

    @Override
    public void delete(String[] roleIds) {
        //????????????
        this.deleteBatchIds(Arrays.asList(roleIds));
        //???????????????????????????
        itRoleMenuService.deleteBatch(roleIds);
    }

    private void checkPrems(TCmsRoleEntity entity) {
        //???????????????????????????????????????????????????????????????????????????????????????
        if (AdminName.getName.equals(entity.getCreateUserId())) {
            return;
        }
        //????????????????????????????????????
        List<String> menuIdList = itUserService.queryAllMenuId(entity.getCreateUserId());
        List<String> roleMenuIdList = this.getRoleMenuIdList(entity);
        //??????????????????
        if ((null == menuIdList && null != roleMenuIdList) || !menuIdList.containsAll(roleMenuIdList)) {
            throw new RuntimeException("???????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????????????????id
     *
     * @param entity
     * @return
     */
    private List<String> getRoleMenuIdList(TCmsRoleEntity entity) {
        if (!CollectionUtils.isEmpty(entity.getMenuIdList())) {
            return entity.getMenuIdList();
        }
        List<String> roleMenuIdList = new ArrayList<>();
        for (TCmsMenuEntity menu : entity.getMenus()) {
            roleMenuIdList.add(menu.getMenuId());
        }
        return roleMenuIdList;
    }

}
