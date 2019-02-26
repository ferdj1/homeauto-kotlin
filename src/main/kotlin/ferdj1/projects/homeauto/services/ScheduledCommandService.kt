package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.model.ScheduledCommand
import ferdj1.projects.homeauto.repositories.ScheduledCommandRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

interface ScheduledCommandService {
    fun findAll(): List<ScheduledCommand>
    fun scheduleCommand(command: ScheduledCommand): ScheduledCommand
    fun delete(scheduledCommand: ScheduledCommand)
}

@Service("scheduledCommandService")
class ScheduledCommandServiceImpl : ScheduledCommandService {
    @Autowired
    lateinit var scheduledCommandRepository: ScheduledCommandRepository

    override fun findAll(): List<ScheduledCommand> = scheduledCommandRepository.findAll()
    override fun scheduleCommand(command: ScheduledCommand) = scheduledCommandRepository.insert(command)
    override fun delete(scheduledCommand: ScheduledCommand) = scheduledCommandRepository.delete(scheduledCommand)
}