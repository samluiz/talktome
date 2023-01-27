package com.saurs.talktome.models;

import java.util.ArrayList;
import java.util.List;

import com.saurs.talktome.models.enums.Mood;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("MESSAGE")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Message extends AbstractPost {

  private Mood mood;

  @OneToMany(mappedBy = "replyTo")
  private List<Reply> replies = new ArrayList<>();
}
