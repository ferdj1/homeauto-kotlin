package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.model.Subscription
import ferdj1.projects.homeauto.repositories.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface SubscriptionService {
    fun findAll(): List<Subscription>

}

@Service("subscriptionService")
class SubscriptionServiceImpl : SubscriptionService {
    @Autowired
    lateinit var subscriptionRepository: SubscriptionRepository

    override fun findAll(): List<Subscription> = subscriptionRepository.findAll()
}