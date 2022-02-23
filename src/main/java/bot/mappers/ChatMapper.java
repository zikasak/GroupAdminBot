package bot.mappers;

import bot.entities.TGroup;
import bot.entities.TGroupAdmin;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface ChatMapper {

    @Select("select " +
            "chat_id  chat_id," +
            "chat_name chat_name," +
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
            tg.chat_name chat_name,
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
            (chat_id, user_id, additional)
            VALUES (#{chat_id}, #{user_id}, #{additional}) ON CONFLICT (chat_id, user_id) DO UPDATE
            SET additional = #{additional};""")
    void addAdministrator(@Param("chat_id") Long chatId, @Param("user_id") Long userId, @Param("additional") boolean additional );

    @Select("""
            SELECT chat_id chat_id,
            user_id user_id,
            additional additional
            FROM t_groups_admins
            WHERE chat_id = #{chat_id};""")
    Set<TGroupAdmin> getAdministrators(@Param("chat_id") Long chatId);

    @Insert({"<script>",
            """
            DELETE FROM t_groups_admins
            WHERE chat_id = #{chatId} and additional = false;
            """,
            """
            insert into t_groups_admins(chat_id, user_id) values
            """,
            "<foreach collection='groupAdmin' item='user' index='index' open='(' separator = '),(' close=')' >#{chatId},#{user}</foreach>",
            "</script>"})
    void setAdministratorList(@Param("chatId") Long chatId, @Param("groupAdmin") Collection<Long> groupAdmin);

    @Delete("""
            DELETE FROM t_groups_admins
            WHERE chat_id = #{chatId} and user_id = #{userId};""")
    void deleteAdministrator(@Param("chatId") Long chatId, @Param("userId") Long userId);
}
