package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.repositories.DeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface DeviceService {
    fun findAll(): List<DeviceDescription>
    fun existsById(id: String): Boolean
    fun add(device: DeviceDescription): DeviceDescription
}

@Service("deviceService")
class DeviceServiceImpl : DeviceService {
    @Autowired
    lateinit var deviceRepository: DeviceRepository

    override fun findAll(): List<DeviceDescription> = deviceRepository.findAll()

    override fun existsById(id: String) = deviceRepository.existsById(id)

    override fun add(device: DeviceDescription) = deviceRepository.save(device)

}