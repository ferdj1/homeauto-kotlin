package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.model.ScheduledCommand
import ferdj1.projects.homeauto.repositories.ScheduledCommandRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface ScheduledCommandService {
    fun findAll(): List<ScheduledCommand>

}

@Service("scheduledCommandService")
class ScheduledCommandServiceImpl : ScheduledCommandService {
    @Autowired
    lateinit var scheduledCommandRepository: ScheduledCommandRepository

    override fun findAll(): List<ScheduledCommand> = scheduledCommandRepository.findAll()
}