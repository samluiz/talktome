package com.saurs.talktome.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.saurs.talktome.models.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name="TB_USER")
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String email;
  private String phone;
  private String firstName;
  private String lastName;
  private Gender gender;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", updatable=false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
  @ToString.Exclude
  private List<Post> posts = new ArrayList<>();

  public void clearPosts() {
      for (Post p : this.posts) {
        p.setAuthor(null);
        p.clearReplies();
      }
    this.posts.clear();
  }

  public boolean repeatedEmail(String email) {
    return this.email.equals(email);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
