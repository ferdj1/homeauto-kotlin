package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.Scene
import ferdj1.projects.homeauto.repositories.SceneRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface SceneService {
    fun findAll(): List<Scene>

}

@Service("sceneService")
class SceneServiceImpl : SceneService {
    @Autowired
    lateinit var sceneRepository: SceneRepository

    override fun findAll(): List<Scene> = sceneRepository.findAll()

}