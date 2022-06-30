package com.bandit.mvc.service.impl;

import com.bandit.entity.RoomUser;
import com.bandit.mvc.mapper.RoomUserMapper;
import com.bandit.mvc.service.RoomUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Bandit
 * @createTime 2022/6/29 23:41
 */
@Service
public class RoomUserServiceImpl extends ServiceImpl<RoomUserMapper, RoomUser> implements RoomUserService {
}
