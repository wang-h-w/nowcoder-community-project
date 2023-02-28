package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

// 采用注解的方式指定SQL语句
@Mapper
public interface LoginTicketMapper {

    /**
     * 插入一个登录凭证
     * @param loginTicket Ticket
     * @return 1-success; 0-fail
     */
    @Insert("insert into login_ticket (user_id, ticket, status, expired) values (#{userId}, #{ticket}, #{status}, #{expired})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 根据凭证号查询相关信息
     * @param ticket Ticket string
     * @return Ticket
     */
    @Select("select * from login_ticket where ticket = #{ticket}")
    LoginTicket selectByTicket(String ticket);

    /**
     * 修改登录凭证状态
     * @param ticket Ticket string
     * @param status Status
     * @return 1-success; 0-fail
     */
    @Update("update login_ticket set status = #{status} where ticket = #{ticket}")
    int updateStatus(String ticket, int status);
}
