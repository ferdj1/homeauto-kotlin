package ferdj1.projects.homeauto.repositories

import ferdj1.projects.homeauto.model.Subscription
import org.springframework.data.mongodb.repository.MongoRepository

interface SubscriptionRepository : MongoRepository<Subscription, Long>