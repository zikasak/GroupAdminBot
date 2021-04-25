package bot.reps;

import bot.entities.TDeclaredCommand;
import bot.entities.TDeclaredCommandID;
import common.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRep extends CrudRepository<TDeclaredCommand, TDeclaredCommandID> {

    TDeclaredCommand getByChatIDAndCommand(long chat_id, String command);

}
