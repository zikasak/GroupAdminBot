package bot.mappers;

import bot.entities.TMutedUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MutedUserMapper {

    @Select("SELECT chat_id, user_id, group_chat_id, mute_date, welcome_msg_id " +
            "FROM t_muted_users " +
            "WHERE chat_id = #{chatId} AND user_id = #{userId};")
    Optional<TMutedUser> findByUsedIdAndChatId(@Param("userId") Long user_id, @Param("chatId") Long chat_id);

    @Select("SELECT chat_id, user_id, group_chat_id, mute_date, welcome_msg_id " +
            "            \"FROM t_muted_users \" +\n" +
            "            \"WHERE chat_id = #{chatId}")
    List<TMutedUser> getMutedInChat(@Param("chatId") Long chat_id);

    @Select("DELETE " +
            "FROM t_muted_users " +
            "WHERE chat_id = #{chatId} AND user_id = #{userId};")
    void delete(@Param("userId") Long user_id, @Param("chatId") Long chat_id);
}
