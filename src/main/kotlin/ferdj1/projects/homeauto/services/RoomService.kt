package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.Room
import ferdj1.projects.homeauto.repositories.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface RoomService {
    fun findAll(): List<Room>
    fun findByName(name: String): Room?
    fun add(room: Room): Room
    fun delete(room: Room)
    fun deleteByName(name: String)
    fun update(room: Room): Room
}

@Service("roomService")
class RoomServiceImpl : RoomService {

    @Autowired
    lateinit var roomRepository: RoomRepository

    override fun findAll(): List<Room> = roomRepository.findAll()
    override fun add(room: Room) = roomRepository.insert(room)
    override fun delete(room: Room) = roomRepository.delete(room)
    override fun deleteByName(name: String) = roomRepository.deleteByName(name)
    override fun findByName(name: String): Room? = roomRepository.findByName(name)
    override fun update(room: Room): Room = roomRepository.save(room)
}