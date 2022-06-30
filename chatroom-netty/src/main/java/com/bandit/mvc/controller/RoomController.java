package com.bandit.mvc.controller;

import com.bandit.entity.Room;
import com.bandit.mvc.service.impl.RoomServiceImpl;
import com.bandit.mvc.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bandit
 * @createTime 2022/6/30 12:08
 */
@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    RoomServiceImpl roomService;

    public ResultVO getRoomListByUser(HttpServletRequest request) {
        Long uid = (Long) request.getAttribute("uid");
        List<Room> roomList = roomService.getRoomByUserId(uid);
        Map<String, Object> params = new HashMap<>();
        params.put("roomList", roomList);
        return ResultVO.success(params);
    }

}
