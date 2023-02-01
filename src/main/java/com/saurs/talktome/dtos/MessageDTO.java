package com.saurs.talktome.dtos;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.saurs.talktome.models.Message;
import com.saurs.talktome.models.enums.Mood;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
public class MessageDTO {
  private Long id;
  private String title;
  private String text;
  private Long fromUserId;
  private Long toUserId;
  private Mood mood;
  private List<ReplyDTO> replies;

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

  public static MessageDTO converter(Message m) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(m, MessageDTO.class);
  }

  public static Message converterToEntity(MessageDTO dto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(dto, Message.class);
  }
}
