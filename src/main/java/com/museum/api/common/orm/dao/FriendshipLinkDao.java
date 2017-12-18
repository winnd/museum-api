package com.museum.api.common.orm.dao;

import com.museum.api.common.orm.model.FriendshipLink;
import com.museum.api.core.vo.FriendshipLinkSearchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendshipLinkDao {

    List<FriendshipLink> getFriendshipLink(@Param("searchVO") FriendshipLinkSearchVO friendshipLinkSearchVO);

}
