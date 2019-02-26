package ferdj1.projects.homeauto.services

import ferdj1.projects.homeauto.model.DeviceDescription
import ferdj1.projects.homeauto.repositories.DeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

interface DeviceService {
    fun findAll(): MutableList<DeviceDescription>
    fun findById(id: String): Optional<DeviceDescription>
    fun existsById(id: String): Boolean
    fun add(device: DeviceDescription): DeviceDescription
    fun update(device: DeviceDescription): DeviceDescription
}

@Service("deviceService")
class DeviceServiceImpl : DeviceService {
    @Autowired
    lateinit var deviceRepository: DeviceRepository

    override fun findAll(): MutableList<DeviceDescription> = deviceRepository.findAll()

    override fun existsById(id: String) = deviceRepository.existsById(id)

    override fun add(device: DeviceDescription) = deviceRepository.save(device)

    override fun findById(id: String): Optional<DeviceDescription> = deviceRepository.findById(id)

    override fun update(device: DeviceDescription): DeviceDescription {
        val oldDeviceDescription = findById(device.id).get()
        return deviceRepository.save(device)
    }
}