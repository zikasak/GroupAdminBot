package bot.services;

import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;
import bot.mappers.BlockedPhraseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
@Slf4j
public class BlockedPhraseServiceImpl implements BlockedPhraseService {
    private BlockedPhraseMapper mapper;

    @Override
    public Set<TBlockedPhrase> getForGroup(TGroup tGroup) {
        return mapper.getForChat(tGroup.getChat_id());
    }
}
