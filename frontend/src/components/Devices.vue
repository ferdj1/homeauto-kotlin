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
            <div class="columns" id="devicesInfoCards">
                <div class="column is-half">
                    <div class="card" id="numOfConnectedCard">
                        <div class="card-header">
                            <h1 class="card-header-title is-centered title" v-if="sessions !== null && sessions !== undefined">
                                {{Object.keys(sessions).length}}
                            </h1>
                        </div>
                        <div class="card-content">
                            <div class="content">
                                <h3 class="subtitle has-text-centered"><i class="fas fa-plug"></i> Devices connected</h3>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="column is-half">
                    <div class="card" id="numOfTotalCard">
                        <div class="card-header">
                            <h1 class="card-header-title is-centered title">
                                {{devices.length}}
                            </h1>
                        </div>
                        <div class="card-content">
                            <div class="content">
                                <h3 class="subtitle has-text-centered"><i class="fas fa-desktop"></i> Devices total</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <router-link :to="'/devices/' + device.id" v-for="device in devices">
                <div class="box">
                    <span class="tag is-success is-pulled-right" v-if="sessions !== null && device.id in sessions">Connected</span>
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
            this.$options.sockets.onopen = (event) => {
                console.log('Listening to changes from backend(Devices.vue)...')
            }

            this.$options.sockets.onmessage = (event) => {
                var json = JSON.parse(event.data)
                if (json.status === 'OK'
                    && (json.type === 'description'
                        || json.type === 'sessionConnected'
                        || json.type === 'connectionClosed')) {
                    this.getDevices()
                    this.getSessions()
                }
            }

            this.getDevices()
            this.getSessions()
        },
        methods: {
            getDevices() {
                AXIOS.get('http://localhost:8080/api/devices')
                    .then((response) => {
                        console.log(response)
                        this.devices = response.data
                        if (this.devices.length > 0) {
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
            this.getDevices()
            this.getSessions()

            /*setInterval(() => {
                this.getDevices()
                this.getSessions()
            }, 1000);*/
        }
    }
</script>

<style>
    #devicesRoot {
        background-color: #ededed;
        height: 100%;
    }

    #devicesRoot .box h1 {
        text-transform: uppercase;
        font-weight: bolder;
    }

    #noDevicesAlert {
        margin: 30px;
    }

    #deviceList .box {
        margin-top: 10px;
    }

    #devicesInfoCards {
        padding-left: 100px;
        padding-right: 100px;
    }

    #devicesInfoCards .card h3 {
        text-transform: uppercase;
        font-family: "Roboto Condensed", Arial, sans-serif;
        margin-bottom: 0;
    }

    #devicesInfoCards .card h3 i {
        margin-right: 5px;
    }

    #numOfConnectedCard .card-header {
        background: linear-gradient(to bottom, #003056 30%, #004378 100%);
        padding: 30px;
    }

    #numOfConnectedCard .card-content {
        padding: 15px;
    }

    #devicesInfoCards .card h1 {
        color: #fff;
        font-family: "Roboto Condensed", Arial, sans-serif;
        font-weight: bolder;
    }

    #numOfTotalCard .card-header {
        background: linear-gradient(to bottom, #04518c 30%, #045c9f 100%);
        padding: 30px;
    }

    #numOfTotalCard .card-content {
        padding: 15px;
    }

    #numOfSomethingCard .card-header {
        background: linear-gradient(to bottom, #00a1d9 30%, #00b7f7 100%);
        padding: 30px;
    }
</style>