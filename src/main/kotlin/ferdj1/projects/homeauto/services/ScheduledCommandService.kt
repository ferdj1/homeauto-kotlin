package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.repositories.ScheduledCommandRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ScheduledCommandService {
    // TODO: Define needed methods
}

@Service("scheduledCommandService")
class ScheduledCommandServiceImpl : ScheduledCommandService {
    @Autowired
    lateinit var scheduledCommandRepository: ScheduledCommandRepository
}