package ferdj1.projects.homeauto.repositories

import ferdj1.projects.homeauto.model.ScheduledCommand
import org.springframework.data.mongodb.repository.MongoRepository

interface ScheduledCommandRepository : MongoRepository<ScheduledCommand, Long>