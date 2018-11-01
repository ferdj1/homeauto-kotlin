package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.services.SceneService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ScenesController {
    @Autowired
    private lateinit var sceneService: SceneService

    @GetMapping("/scenes")
    fun getScenes() = sceneService.findAll()
}