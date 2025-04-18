package com.example.data.todoapp.rest.model;

import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CheckListItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CheckListTask task;

    private String text;

    @Builder.Default
    private boolean checked = false;

    public boolean toggle() {
        return checked = !checked;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        CheckListItem checkListItem = (CheckListItem) o;
        return getId() != null && Objects.equals(getId(), checkListItem.getId());
    }
}
