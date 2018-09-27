package src.redtalent.domain;

import org.springframework.data.annotation.Id;
import javax.persistence.*;
import java.util.Objects;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DomainEntity {

    // Constructors -----------------------------------------------------------

    public DomainEntity() {
        super();
    }


    // Identification ---------------------------------------------------------

    private String	id;
    private int	version;


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Version
    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    // Object interface -------------------------------------------------------


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEntity)) return false;
        DomainEntity that = (DomainEntity) o;
        return getVersion() == that.getVersion() &&
                Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVersion());
    }

    @Override
    public String toString() {
        return "DomainEntity{" +
                "id='" + id + '\'' +
                ", version=" + version +
                '}';
    }
}

