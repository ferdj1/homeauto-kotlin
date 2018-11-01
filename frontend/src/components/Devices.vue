<template>
    <div id="devicesRoot">
        <!-- Heading of the devices -->
        <section class="hero is-primary">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">
                        Devices in use
                    </h1>
                    <h2 class="subtitle">
                        Here you can access control panels of every device connected to the system
                    </h2>
                </div>
            </div>
        </section>

        <!-- No devices connected alert -->
        <article class="message" id="noDevicesAlert" v-if="noDevices">
            <div class="message-header">
                <p>Connect devices</p>
            </div>
            <div class="message-body">There is no devices connected at the moment. Once connected, devices will show
                up here.
            </div>
        </article>

        <!-- Device list -->
        <div class="container" id="deviceList" v-else>
            <!-- TODO: Fix href if necessary -->
            <a href="" v-for="device in devices">
                <div class="box">
                    <!-- TODO: Insert code that displays whether device is connected or not -->
                    <span class="tag is-success is-pulled-right">Connected</span>
                    <h1 class="title is-3">{{device.name}}</h1>
                    <h2 class="subtitle is-5">{{device.manufacturer}}</h2>
                    <hr>
                    <p class="is-5">{{device.type}}</p>
                </div>
            </a>
        </div>
    </div>
</template>

<script>
    import {AXIOS} from '../http-common'

    export default {
        name: 'Devices',
        data() {
            return {
                devices: null,
                noDevices: true
            }
        },
        created: function () {
            AXIOS.get('http://localhost:8080/api/devices')
                .then((response) => {
                    console.log(response)
                    this.devices = response.data
                    if(this.devices.length > 0) {
                        console.log(this.devices.length)
                        this.noDevices = false
                    }
                }).catch((error) => {
                console.log(error)
            })
        },
        methods: {
            getRooms() {
                AXIOS.get('http://localhost:8080/api/devices')
                    .then((response) => {
                        console.log(response);
                        this.devices = response.data
                        if(this.devices.length > 0) {
                            console.log(this.devices.length)
                            this.noDevices = false
                        }
                    }).catch((error) => {
                    console.log(error)
                })
            }
        }
    }
</script>

<style>
    #noDevicesAlert {
        margin: 30px;
    }


    #deviceList .box {
        margin-top: 10px;
    }
</style>