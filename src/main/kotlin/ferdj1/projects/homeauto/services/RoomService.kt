package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.repositories.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface RoomService {
    // TODO: Define needed methods
}

@Service("roomService")
class RoomServiceImpl : RoomService {
    @Autowired
    lateinit var roomRepository: RoomRepository
}