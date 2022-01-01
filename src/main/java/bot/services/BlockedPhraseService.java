package bot.services;

import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;

import java.util.Set;

public interface BlockedPhraseService {

    Set<TBlockedPhrase> getForGroup(TGroup tGroup);
}
