package bot.services;

import bot.BotUtils;
import bot.commands.inChat.InChatBotCommand;
import bot.entities.TBlockedPhrase;
import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.mappers.ChatMapper;
import bot.mappers.CommandMapper;
import bot.mappers.BlockedPhraseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
@Slf4j
public class InChatBotCommandService {

    private final ChatMapper chatRep;
    private final CommandMapper commandMapper;
    private final BlockedPhraseMapper blockedPhraseMapper;
    private final BotUtils botUtils;

    @Autowired
    public InChatBotCommandService(ChatMapper chatRep, CommandMapper commandMapper, BlockedPhraseMapper blockedPhraseMapper, BotUtils botUtils) {
        this.chatRep = chatRep;
        this.commandMapper = commandMapper;
        this.blockedPhraseMapper = blockedPhraseMapper;
        this.botUtils = botUtils;
    }

    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] strings, InChatBotCommand command) throws TelegramApiException {
        String text = message.getText();
        String replace = text.replace("/" + command.getCommandIdentifier() + " ", "");
        message.setText(replace);
        Optional<TGroup> chat = this.chatRep.findById(message.getChatId());
        boolean rightsNeeded = command.isRightsNeeded();
        boolean alligableToRun = !rightsNeeded || botUtils.isUserAdmin(absSender, message);
        if (alligableToRun && chat.isPresent() == command.isChatCheckParam())
            command.execute(absSender, chat.orElse(null), message, strings);
        if (command.isDeleteAfterUse()){
            botUtils.deleteMessage(absSender, message, absSender.getMe());
        }
    }

    @Transactional
    public void save(TGroup tGroup){
        this.chatRep.save(tGroup);
    }

    @Transactional
    public void save(TDeclaredCommand command){
        this.commandMapper.save(command);
    }

    @Transactional
    public void save(TBlockedPhrase blockedPhrase){
        this.blockedPhraseMapper.save(blockedPhrase.getChat_id(), blockedPhrase.getBlocked_phrase());
    }

    @Transactional
    public void delete(TDeclaredCommand command){
        this.commandMapper.delete(command.getChatId(), command.getCommand());
    }

    @Transactional
    public void delete(TBlockedPhrase phrase){
        this.blockedPhraseMapper.delete(phrase.getChat_id(), phrase.getBlocked_phrase());
    }

}
