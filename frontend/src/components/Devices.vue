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
            <router-link :to="'/devices/' + device.id" v-for="device in devices">
                <div class="box">
                    <span class="tag is-success is-pulled-right" v-if="device.id in sessions">Connected</span>
                    <span class="tag is-danger is-pulled-right" v-else>Disconnected</span>
                    <h1 class="title is-3">{{device.name}}</h1>
                    <h2 class="subtitle is-5">{{device.manufacturer}}</h2>
                    <hr>
                    <p class="is-5">{{device.type}}</p>
                </div>
            </router-link>
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
                sessions: null,
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

            AXIOS.get('http://localhost:8080/api/websocketsessions')
                .then((response) => {
                    console.log(response)
                    this.sessions = response.data
                }).catch((error) => {
                console.log(error)
            })
        },
        methods: {
            getDevices() {
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
            getSessions() {
                AXIOS.get('http://localhost:8080/api/websocketsessions')
                    .then((response) => {
                        console.log(response)
                        this.sessions = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            }
        },
        mounted: function () {
            this.getSessions();

            setInterval(() => {
                this.getSessions();
            }, 500);
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