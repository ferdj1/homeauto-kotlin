package ferdj1.projects.homeauto.repositories

import ferdj1.projects.homeauto.model.Room
import org.springframework.data.mongodb.repository.MongoRepository

interface RoomRepository : MongoRepository<Room, Long> {
    fun deleteByName(name: String)
    fun findByName(name: String): Room?
}