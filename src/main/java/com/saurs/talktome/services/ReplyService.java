package com.saurs.talktome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saurs.talktome.repositories.ReplyRepository;

@Service
public class ReplyService {
  
  @Autowired
  ReplyRepository repository;


}
