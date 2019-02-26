package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.model.Room
import ferdj1.projects.homeauto.model.Subscription
import ferdj1.projects.homeauto.repositories.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface SubscriptionService {
    fun findAll(): List<Subscription>
    fun add(subscription: Subscription): Subscription
    fun delete(subscription: Subscription)
    fun update(subscription: Subscription): Subscription
}

@Service("subscriptionService")
class SubscriptionServiceImpl : SubscriptionService {
    @Autowired
    lateinit var subscriptionRepository: SubscriptionRepository

    override fun findAll(): List<Subscription> = subscriptionRepository.findAll()
    override fun add(subscription: Subscription): Subscription = subscriptionRepository.insert(subscription)
    override fun delete(subscription: Subscription) = subscriptionRepository.delete(subscription)
    override fun update(subscription: Subscription): Subscription = subscriptionRepository.save(subscription)

}