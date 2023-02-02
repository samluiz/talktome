package com.saurs.talktome.models;

import com.saurs.talktome.models.enums.Mood;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_POST")
public class Post extends AbstractPost {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Mood mood;

  @OneToMany(mappedBy = "replyTo", cascade = CascadeType.REMOVE)
  @ToString.Exclude
  private List<Reply> replies = new ArrayList<>();

  public void clearReplies() {
    this.replies.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Post post = (Post) o;
    return id != null && Objects.equals(id, post.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
