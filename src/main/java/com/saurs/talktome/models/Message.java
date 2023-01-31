package com.saurs.talktome.models;

import java.util.ArrayList;
import java.util.List;

import com.saurs.talktome.models.enums.Mood;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_MESSAGE")
public class Message extends AbstractPost {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Mood mood;

  @OneToMany(mappedBy = "replyTo")
  private List<Reply> replies = new ArrayList<>();
}
