package bot.services;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;

import java.util.Set;

public interface CommandService {

    Set<TDeclaredCommand> getForGroup(TGroup tGroup);
}
