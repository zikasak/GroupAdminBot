package bot.reps;

import bot.entities.TGroup;
import common.HibernateUtil;
import org.hibernate.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRep extends CrudRepository<TGroup, Long> {

}
