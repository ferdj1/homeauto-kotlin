import Vue from 'vue'
import Router from 'vue-router'

import Devices from './components/Devices'
import Device from './components/Device'
import Subscriptions from './components/Subscriptions'
import Scheduler from './components/Scheduler'
import Rooms from './components/Rooms'

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            redirect: '/devices'
        },
        {
            path: '/devices',
            name: 'Devices',
            component: Devices
        },
        {
            path: '/subscriptions',
            name: 'Subscriptions',
            component: Subscriptions
        },
        {
            path: '/scheduler',
            name: 'Scheduler',
            component: Scheduler
        },
        {
            path: '/rooms',
            name: 'Rooms',
            component: Rooms
        },
        {
            path: '/devices/:id',
            name: 'Device',
            component: Device
        }
    ]
})
