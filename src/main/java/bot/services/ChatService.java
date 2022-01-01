package bot.services;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.entities.TMutedUser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatService {
    List<TMutedUser> getMutedUsers(TGroup group);

    Optional<TGroup> get(Long id);

    void save(TGroup chat);

    Set<TDeclaredCommand> getCommands(TGroup tGroup);

}
