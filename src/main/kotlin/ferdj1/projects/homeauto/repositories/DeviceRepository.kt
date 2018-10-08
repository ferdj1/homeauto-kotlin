package ferdj1.projects.homeauto.repositories

import ferdj1.projects.homeauto.model.DeviceDescription
import org.springframework.data.mongodb.repository.MongoRepository

interface DeviceRepository : MongoRepository<DeviceDescription, String>