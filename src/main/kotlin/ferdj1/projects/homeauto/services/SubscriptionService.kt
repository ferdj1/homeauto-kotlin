package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.repositories.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface SubscriptionService {
    // TODO: Define needed methods
}

@Service("subscriptionService")
class SubscriptionServiceImpl : SubscriptionService {
    @Autowired
    lateinit var subscriptionRepository: SubscriptionRepository
}