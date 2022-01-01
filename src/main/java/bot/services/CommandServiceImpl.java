package bot.services;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.mappers.CommandMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
@Slf4j
public class CommandServiceImpl implements CommandService {

    private CommandMapper commandMapper;

    @Override
    public Set<TDeclaredCommand> getForGroup(TGroup tGroup) {
        return commandMapper.getForChat(tGroup.getChat_id());
    }
}
