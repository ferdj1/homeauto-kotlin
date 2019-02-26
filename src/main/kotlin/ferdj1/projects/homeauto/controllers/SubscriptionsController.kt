package ferdj1.projects.homeauto.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ferdj1.projects.homeauto.model.Observer
import ferdj1.projects.homeauto.model.Parameter
import ferdj1.projects.homeauto.model.ParameterComparator
import ferdj1.projects.homeauto.model.Subscription
import ferdj1.projects.homeauto.services.SubscriptionService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class SubscriptionsController {
    @Autowired
    private lateinit var subscriptionService: SubscriptionService

    @GetMapping("/subscriptions")
    fun getSubscriptions() = subscriptionService.findAll()

    @PostMapping("/subscriptions")
    fun addSubscription(@RequestBody subscription: String) {
        val rootNode = jacksonObjectMapper().readTree(subscription)
        val subscriptions = subscriptionService.findAll()
        val subscription = subscriptions.find {
            it.observedDeviceId == rootNode["observedDeviceId"].asText()
                    && it.observedCommandId == rootNode["observedCommandId"].asText()
        }

        val parameterList = rootNode["parameters"].map { jacksonObjectMapper().treeToValue(it, Parameter::class.java) }
        val parameterComparatorList = rootNode["parameterComparators"]
                .map { jacksonObjectMapper().treeToValue(it, ParameterComparator::class.java) }

        if (subscription !== null) {
            subscription.observerList.add(Observer(ObjectId(),
                    rootNode["observerDeviceId"].asText(),
                    rootNode["observerCommandId"].asText(),
                    parameterList,
                    parameterComparatorList))
            subscriptionService.update(subscription)
        } else {
            val newSubscription = Subscription(ObjectId(),
                    rootNode["observedDeviceId"].asText(),
                    rootNode["observedCommandId"].asText(),
                    mutableListOf())
            newSubscription.observerList.add(Observer(ObjectId(),
                    rootNode["observerDeviceId"].asText(),
                    rootNode["observerCommandId"].asText(),
                    parameterList,
                    parameterComparatorList))

            subscriptionService.add(newSubscription)
        }
    }
}