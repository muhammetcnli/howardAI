package com.atlas.howardAI.repository;

import com.atlas.howardAI.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
