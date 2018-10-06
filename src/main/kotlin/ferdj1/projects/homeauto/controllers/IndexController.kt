package ferdj1.projects.homeauto.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller("/")
class IndexController {
    @GetMapping("/")
    fun getIndex(model: Model): String {
        return "index"
    }
}