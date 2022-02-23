package bot.services;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.entities.TGroupAdmin;
import bot.entities.TMutedUser;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;

public interface ChatService {

    Set<TGroup> getGroupList(long userId);

    List<TMutedUser> getMutedUsers(TGroup group);

    Optional<TGroup> get(Long id);

    void save(TGroup chat);

    Set<TDeclaredCommand> getCommands(TGroup tGroup);

    void addAdministrator(TGroup tGroup, User user);

    void addAdministrator(Long tGroup, Long user, boolean additional);

    Set<TGroupAdmin> getAdministratorList(Long chatId);

    void setAdministratorList(Long chatId, Collection<Long> admins);

    void deleteAdministrator(Long chatId, Long userId);
}
