package bot.mappers;

import bot.entities.TGroup;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;
import java.util.Set;

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


    @Select("""
            select
            tg.chat_id chat_id,
            tg.new_users_blocked new_users_blocked,
            tg.read_only read_only,
            tg.time_to_mute time_to_mute,
            tg.wel_message wel_message
            from
            t_groups_admins tga
            join t_groups tg on
            tg.chat_id = tga.chat_id
            where
            tga.user_id = #{userId}""")
    Set<TGroup> getGroupForUser(@Param("userId") long userId);

    @Insert("""
            INSERT INTO t_groups_admins
            VALUES (chat_id, user_id)
            (#{tGroup.id}, #{user.id}) ON CONFLICT DO NOTHING""")
    void addAdministrator(@Param("tGroup") TGroup tGroup, @Param("user") User user);
}
