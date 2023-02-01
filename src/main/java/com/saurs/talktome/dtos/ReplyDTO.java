package com.saurs.talktome.dtos;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.saurs.talktome.models.Reply;
import com.saurs.talktome.models.enums.Reaction;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
public class ReplyDTO {
  private Long id;
  private String title;
  private String text;
  private Long fromUserId;
  private Long toUserId;
  private Long replyToId;
  private Reaction reaction;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
  @Column(name = "created_at", updatable=false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public static ReplyDTO converter(Reply m) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(m, ReplyDTO.class);
  }
}
