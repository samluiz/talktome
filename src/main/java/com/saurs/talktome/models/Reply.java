package com.saurs.talktome.models;

import com.saurs.talktome.models.enums.Reaction;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("REPLY")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Reply extends AbstractPost {
  
  private Reaction reaction;

  @ManyToOne
  @JoinColumn(name = "reply_to")
  private Message replyTo;
}
