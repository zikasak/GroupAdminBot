package bot.mappers;

import bot.entities.TDeclaredCommand;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface CommandMapper {

    @Select("SELECT chat_id chat_id, " +
            "command_text command_text, " +
            "command command " +
            "FROM t_declared_command " +
            "WHERE chat_id = #{chatId} and command = #{command};")
    TDeclaredCommand getByIdChatIdAndIdCommand(@Param("chatId") long chat_id, @Param("command") String command);

    @Select("SELECT chat_id chatId, " +
            "command_text commandText, " +
            "command command " +
            "FROM t_declared_command " +
            "WHERE chat_id = #{chatId};")
    Set<TDeclaredCommand> getForChat(@Param("chatId") Long chatId);

    @Delete("DELETE FROM t_declared_command " +
            "WHERE chat_id = #{chatId} and command = #{command};")
    void delete(@Param("chatId") long chat_id, @Param("command") String command);

    @Insert("INSERT INTO t_declared_command " +
            "(chat_id, command_text, command) " +
            "VALUES(#{command.chatId}, #{command.command_text}, #{command.command}) ON CONFLICT (chat_id, command) DO UPDATE " +
            "SET command_text = #{command.command_text}, command = #{command.command};")
    void save(@Param("command") TDeclaredCommand command);
}

