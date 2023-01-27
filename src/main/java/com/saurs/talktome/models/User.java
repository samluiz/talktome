package com.saurs.talktome.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.saurs.talktome.models.enums.Gender;
import com.saurs.talktome.models.enums.Mood;
import com.saurs.talktome.models.enums.Reaction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name="TB_USER")
public class User {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String email;
  private String phone;
  private String password;
  private String firstName;
  private String lastName;
  private Gender gender;

  @OneToOne(cascade = CascadeType.DETACH)
  private User partner;

  private Mood lastMood;
  private Reaction lastReaction;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", updatable=false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
  
  @OneToMany(mappedBy = "fromUser")
  private List<AbstractPost> messages;

  
}
