package ferdj1.projects.homeauto.repositories

import ferdj1.projects.homeauto.model.Scene
import org.springframework.data.mongodb.repository.MongoRepository

interface SceneRepository : MongoRepository<Scene, Long>