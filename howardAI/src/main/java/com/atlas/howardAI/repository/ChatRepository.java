package com.atlas.howardAI.repository;

import com.atlas.howardAI.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat,Long> {


}
