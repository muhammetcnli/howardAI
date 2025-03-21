package com.atlas.howardAI.repository;

import com.atlas.howardAI.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
}
