package bot.services;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.entities.TMutedUser;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatService {

    Set<TGroup> getGroupList(long userId);

    List<TMutedUser> getMutedUsers(TGroup group);

    Optional<TGroup> get(Long id);

    void save(TGroup chat);

    Set<TDeclaredCommand> getCommands(TGroup tGroup);

    void addAdministrator(TGroup tGroup, User user);
}
