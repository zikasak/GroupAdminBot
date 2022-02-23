package bot.services;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.entities.TGroupAdmin;
import bot.entities.TMutedUser;
import bot.mappers.ChatMapper;
import bot.mappers.CommandMapper;
import bot.mappers.MutedUserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;
    private final MutedUserMapper mutedUserMapper;
    private final CommandMapper commandMapper;

    @Override
    public Set<TGroup> getGroupList(long userId) {
        return chatMapper.getGroupForUser(userId);
    }

    @Override
    public List<TMutedUser> getMutedUsers(TGroup group){
        return mutedUserMapper.getMutedInChat(group.getChat_id());
    }

    @Override
    public Optional<TGroup> get(Long id) {
        return chatMapper.findById(id);
    }

    @Override
    public void save(TGroup chat) {
        chatMapper.save(chat);
    }

    @Override
    public Set<TDeclaredCommand> getCommands(TGroup tGroup) {
        return commandMapper.getForChat(tGroup.getChat_id());
    }

    @Override
    public void addAdministrator(TGroup tGroup, User user) {
       addAdministrator(tGroup.getChat_id(), user.getId(), false);
    }

    @Override
    public void addAdministrator(Long tGroup, Long user, boolean additional) {
        chatMapper.addAdministrator(tGroup, user, additional);
    }

    @Override
    public Set<TGroupAdmin> getAdministratorList(Long chatId) {
        return chatMapper.getAdministrators(chatId);
    }

    @Override
    public void setAdministratorList(Long chatId, Collection<Long> groupAdmin) {
        chatMapper.setAdministratorList(chatId, groupAdmin);
    }

    @Override
    public void deleteAdministrator(Long chatId, Long userId) {
        chatMapper.deleteAdministrator(chatId, userId);
    }
}
