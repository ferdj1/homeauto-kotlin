package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.Room
import ferdj1.projects.homeauto.repositories.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface RoomService {
    fun findAll(): List<Room>
    fun add(room: Room): Room
}

@Service("roomService")
class RoomServiceImpl : RoomService {
    @Autowired
    lateinit var roomRepository: RoomRepository

    override fun findAll(): List<Room> = roomRepository.findAll()
    override fun add(room: Room) = roomRepository.insert(room)
}