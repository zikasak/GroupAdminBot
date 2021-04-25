package bot.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TGroupAdmin")
public class TGroupAdmin implements Serializable {

    @EmbeddedId
    private TGroupAdminID id;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<TGroup> groups;

    public TGroupAdminID getId() {
        return id;
    }

    public void setId(TGroupAdminID id) {
        this.id = id;
    }

    public Set<TGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<TGroup> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TGroupAdmin that = (TGroupAdmin) o;
        return id.equals(that.id) && groups.equals(that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groups);
    }
}
