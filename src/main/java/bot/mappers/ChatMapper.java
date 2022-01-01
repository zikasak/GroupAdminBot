package bot.mappers;

import bot.entities.TGroup;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ChatMapper {

    @Select("select " +
            "chat_id  chat_id," +
            "new_users_blocked new_users_blocked," +
            "read_only read_only," +
            "time_to_mute time_to_mute," +
            "wel_message wel_message " +
            "from t_groups " +
            "where chat_id = #{id}")
    Optional<TGroup> findById(@Param("id") Long id);

    @Insert("INSERT INTO t_groups " +
            "(chat_id, chat_name, new_users_blocked, read_only, time_to_mute, wel_message) " +
            "VALUES(#{chat.chat_id}, #{chat.chat_name}, #{chat.new_users_blocked}, " +
            "#{chat.read_only}, #{chat.time_to_mute}, #{chat.wel_message}) ON CONFLICT (chat_id) DO UPDATE " +
            "SET chat_name = #{chat.chat_name}, new_users_blocked = #{chat.new_users_blocked}, " +
            "read_only = #{chat.read_only}, time_to_mute = #{chat.time_to_mute}, wel_message = #{chat.wel_message};")
    void save(@Param("chat") TGroup chat);


}
