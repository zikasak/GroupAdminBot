package bot.reps;

import bot.entities.TBlockedPhrase;
import bot.entities.TBlockedPhraseID;
import bot.entities.TDeclaredCommand;
import bot.entities.TDeclaredCommandID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhrasesRep extends CrudRepository<TBlockedPhrase, TBlockedPhraseID> {

}

