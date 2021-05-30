package bot.services;

import bot.BotUtils;
import bot.commands.inChat.InChatBotCommand;
import bot.entities.TBlockedPhrase;
import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.reps.ChatRep;
import bot.reps.CommandRep;
import bot.reps.PhrasesRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
public class InChatBotCommandService {
    @Autowired
    protected ChatRep chatRep;
    @Autowired
    protected CommandRep commandRep;
    @Autowired
    protected PhrasesRep phrasesRep;

    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] strings, InChatBotCommand command) throws TelegramApiException {
        String text = message.getText();
        String replace = text.replace("/" + command.getCommandIdentifier() + " ", "");
        message.setText(replace);
        Optional<TGroup> chat = this.chatRep.findById(message.getChatId());
        boolean rightsNeeded = command.isRightsNeeded();
        boolean alligableToRun = !rightsNeeded || BotUtils.isUserAdmin(absSender, message.getChat(), message.getFrom());
        if (alligableToRun && chat.isPresent() == command.isChatCheckParam())
            command.execute(absSender, chat.orElse(null), message, strings);
        if (command.isDeleteAfterUse()){
            BotUtils.deleteMessage(absSender, message, absSender.getMe());
        }
    }

    @Transactional
    public void save(TGroup tGroup){
        this.chatRep.save(tGroup);
    }

    @Transactional
    public void save(TDeclaredCommand command){
        this.commandRep.save(command);
    }

    @Transactional
    public void save(TBlockedPhrase blockedPhrase){
        this.phrasesRep.save(blockedPhrase);
    }

    @Transactional
    public void delete(TDeclaredCommand command){
        this.commandRep.delete(command);
    }

    @Transactional
    public void delete(TBlockedPhrase phrase){
        this.phrasesRep.delete(phrase);
    }

}
