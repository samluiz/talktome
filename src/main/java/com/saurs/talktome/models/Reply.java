package com.saurs.talktome.models;

import com.saurs.talktome.models.enums.Reaction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_REPLY")
public class Reply extends AbstractPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Reaction reaction;

  @ManyToOne
  @JoinColumn(name = "reply_to")
  private Post replyTo;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Reply reply = (Reply) o;
    return id != null && Objects.equals(id, reply.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

