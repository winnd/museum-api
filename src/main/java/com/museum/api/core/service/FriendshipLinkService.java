package com.museum.api.core.service;

import com.github.pagehelper.PageHelper;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.orm.dao.FriendshipLinkDao;
import com.museum.api.common.orm.mapper.FriendshipLinkMapper;
import com.museum.api.common.orm.model.FriendshipLink;
import com.museum.api.core.vo.FriendshipLinkSearchVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FriendshipLinkService {

    @Resource
    FriendshipLinkMapper friendshipLinkMapper;

    @Resource
    FriendshipLinkDao friendshipLinkDao;

    @Transactional
    public int addFriendshipLink(String name, String url, String image){

        Long currentTime = System.currentTimeMillis();

        FriendshipLink friendshipLink = new FriendshipLink();

        friendshipLink.setName(name);
        friendshipLink.setUrl(url);
        friendshipLink.setImage(image);
        friendshipLink.setCreateTime(currentTime);
        friendshipLink.setCreateBy(1);
        friendshipLink.setUpdateTime(currentTime);
        friendshipLink.setUpdateBy(1);

        return friendshipLinkMapper.insert(friendshipLink);

    }

    @Transactional
    public int removeFriendshipLink(Integer friendshipLink) {

        return friendshipLinkMapper.deleteByPrimaryKey(friendshipLink);

    }

    @Transactional
    public int updateFriendshipLink(FriendshipLink friendshipLink) {

        return friendshipLinkMapper.updateByPrimaryKeySelective(friendshipLink);

    }

    public List<FriendshipLink> getFriendshipLink(FriendshipLinkSearchVO searchVO) {

        PageHelper.startPage(searchVO.getPageNum() == null ? 1 : searchVO.getPageNum(), searchVO.getPageSize() == null ? Constants.PAGE_SIZE : searchVO.getPageSize());

        return friendshipLinkDao.getFriendshipLink(searchVO);

    }

}
