package ferdj1.projects.homeauto.controllers

import ferdj1.projects.homeauto.services.SubscriptionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class SubscriptionsController {
    @Autowired
    private lateinit var subscriptionService: SubscriptionService

    @GetMapping("/subscriptions")
    fun getSubscriptions() = subscriptionService.findAll()
}