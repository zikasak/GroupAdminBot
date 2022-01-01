package bot.mappers;

import bot.entities.TBlockedPhrase;
import bot.entities.TDeclaredCommand;
import org.apache.ibatis.annotations.*;

import java.util.Set;

@Mapper
public interface BlockedPhraseMapper {

    @Delete("DELETE FROM t_blocked_phrase " +
            "WHERE chat_id=#{chatId} AND blocked_phrase=#{phrase};")
    void delete(@Param("chatId") Long chatId, @Param("phrase") String phrase);

    @Insert("INSERT INTO t_blocked_phrase\n" +
            "(chat_id, blocked_phrase)\n" +
            "VALUES(#{chatId}, #{phrase});\n")
    void save(@Param("chatId") Long chatId, @Param("phrase") String phrase);

    @Select("SELECT chat_id, blocked_phrase " +
            "FROM t_blocked_phrase " +
            "WHERE chat_id = #{chatId};")
    Set<TBlockedPhrase> getForChat(@Param("chatId") Long chatId);
}

