package bot.reps;

import bot.entities.TMutedUser;
import bot.entities.TMutedUserID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MutedUsersRep extends CrudRepository<TMutedUser, TMutedUserID> {
}
