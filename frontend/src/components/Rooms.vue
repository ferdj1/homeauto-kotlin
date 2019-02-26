<template>
    <div id="roomsRoot">
        <section class="hero is-primary is-marginless">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">
                        Rooms
                    </h1>
                    <h2 class="subtitle">
                        Group your devices into separate rooms.
                    </h2>
                </div>
            </div>
        </section>

        <!-- Room form -->
        <section id="contentHolder">
            <div class="container" id="formContainer">
                <button class="button is-primary" v-on:click="toggleAddForm" v-html="toggleAddFormText"></button>
                <div class="box" v-if="addFormShown" id="addForm">
                    <form method="post" @submit.prevent="addRoom">
                        <div class="field">
                            <label class="label">Room name</label>
                            <div class="control">
                                <input type="text" placeholder="Room name" class="input"
                                       name="roomName" v-model="roomName" v-bind:class="{'is-danger': invalidName}">
                                <p class="help is-danger" v-show="invalidName">This room name is invalid</p>
                            </div>
                            <br/>
                            <label class="label">Room type</label>
                            <div class="control">
                                <div class="select">
                                    <select name="roomType" id="roomType" v-model="roomType"
                                            v-bind:class="{'is-danger': invalidType}">
                                        <option>Living room</option>
                                        <option>Bedroom</option>
                                        <option>Kitchen</option>
                                        <option>Bathroom</option>
                                        <option>Hallway</option>
                                        <option>Toilet</option>
                                        <option>Garage</option>
                                        <option>Dining room</option>
                                        <option>Balcony</option>
                                        <option>Garden</option>
                                        <option>Storage room</option>
                                    </select>
                                    <p class="help is-danger" v-show="invalidType">This room type is invalid</p>
                                </div>
                            </div>
                            <div class="control">
                                <button class="button is-link" id="newRoomButton">Create new room</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Room list -->
            <div class="container">
                <div class="container" id="titleContainer">
                    <button class="button is-link is-pulled-right is-clearfix" id="generalSettingsButton"
                            v-on:click="settingsShown = !settingsShown">
                        <i class="fas fa-cog" id="genSettingsIcon"></i>Settings
                    </button>
                    <div id="roomsLeftBorder"></div>
                    <h1 class="title is-marginless">Rooms</h1>
                    <div class="modal" id="settingsModal" v-bind:class="{'is-active': settingsShown}">
                        <div class="modal-background" v-on:click="settingsShown = !settingsShown"></div>
                        <div class="modal-card">
                            <header class="modal-card-head">
                                <p class="modal-card-title">Room settings</p>
                                <button class="delete" aria-label="close"
                                        v-on:click="settingsShown = !settingsShown"></button>
                            </header>
                            <section class="modal-card-body">
                                <!-- Content ... -->

                                <!-- Delete room picker -->
                                <form @submit.prevent="deleteRoom">
                                    <label class="label">Delete room:</label>
                                    <div class="control">
                                        <div class="select">
                                            <select name="deleteRoomName" id="deleteRoomName"
                                                    v-model="deleteSelectedRoom">
                                                <option v-for="room in rooms">
                                                    {{room.name}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="control">
                                        <button class="button is-danger" id="deleteButton"
                                                v-on:click="settingsShown = !settingsShown">Delete
                                        </button>
                                    </div>
                                </form>

                            </section>

                            <footer class="modal-card-foot">
                                <button class="button is-primary" v-on:click="settingsShown = !settingsShown">Done
                                </button>
                            </footer>
                        </div>
                    </div>
                </div>
                <br>
                <div class="box" v-for="room in rooms">
                    <div class="control">
                        <button class="button is-warning is-small is-pulled-right roomSettingsButton"
                                v-on:click="settings(room)">
                            <i class="fas fa-cog"></i>
                        </button>
                    </div>
                    <div class="tags has-addons tagInfoGroup is-marginless">
                        <span class="tag is-dark">Type</span>
                        <span class="tag is-info" id="tagType">{{room.type}}</span>
                    </div>
                    <h2 class="title is-3">{{room.name}}</h2>
                    <div class="columns">
                        <div class="column is-2" v-for="(device, index) in room.devices" v-if="index <= 6">
                            <router-link :to="'/devices/' + device.id">
                                <div class="card">
                                    <div class="card-header device-card-header">
                                        <h1 class="card-header-title is-centered subtitle device-card-header-title">
                                            <i class="fas fa-tv" v-if="device.type === 'TV'"></i>
                                            <i class="fas fa-lightbulb" v-if="device.type === 'Lights'"></i>
                                        </h1>
                                    </div>
                                    <div class="card-content">
                                        <div class="content">
                                            <h3 class="subtitle is-6 has-text-centered device-card-name">
                                                {{device.name}}</h3>
                                            <div id="open-bar">
                                                <h5 class="has-text-centered is-uppercase">
                                                    Open
                                                    <i class="fas fa-arrow-circle-right"></i>
                                                </h5>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </router-link>
                        </div>
                    </div>

                    <!-- Modal for single room settings -->
                    <div class="modal" id="roomSettingsModal" v-bind:class="{'is-active': roomSettingsShown}">
                        <div class="modal-background" v-on:click="roomSettingsShown = !roomSettingsShown"></div>
                        <div class="modal-card">
                            <header class="modal-card-head">
                                <p class="modal-card-title">Room settings</p>
                                <button class="delete" aria-label="close"
                                        v-on:click="roomSettingsShown = !roomSettingsShown"></button>
                            </header>
                            <section class="modal-card-body">
                                <!-- Content ... -->

                                <!-- Add device to room picker -->
                                <form @submit.prevent="addDeviceToRoom">
                                    <label class="label">Add device:</label>
                                    <div class="control">
                                        <div class="select">
                                            <select name="addDeviceId" id="addDeviceId" v-model="addDeviceId">
                                                <option v-for="device in freeDevices">
                                                    {{device.id}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="control">
                                        <button class="button is-success" id="addDevice"
                                                v-on:click="roomSettingsShown = !roomSettingsShown">
                                            Add device
                                        </button>
                                    </div>
                                </form>

                                <!-- Delete device from room picker -->
                                <form @submit.prevent="deleteDeviceFromRoom">
                                    <label class="label">Remove device:</label>
                                    <div class="control">
                                        <div class="select">
                                            <select name="deleteDeviceId" id="deleteDeviceId" v-model="deleteDeviceId"
                                                    v-if="currentRoomSettings != null">
                                                <option v-for="device in currentRoomSettings.devices">
                                                    {{device.id}}
                                                </option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="control">
                                        <button class="button is-danger" id="deleteDevice"
                                                v-on:click="roomSettingsShown = !roomSettingsShown">
                                            Remove device
                                        </button>
                                    </div>
                                </form>

                            </section>

                            <footer class="modal-card-foot">
                                <button class="button is-primary" v-on:click="roomSettingsShown = !roomSettingsShown">
                                    Done
                                </button>
                            </footer>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import {AXIOS} from '../http-common'

    export default {
        name: 'Rooms',
        data() {
            return {
                rooms: null,
                roomName: '',
                roomType: 'Living Room',
                addFormShown: false,
                toggleAddFormText: '<i class="fas fa-plus" style="margin-right: 5px"></i> New room',
                settingsShown: false,
                deleteSelectedRoom: '',
                invalidName: false,
                invalidType: false,
                devices: null,
                roomSettingsShown: false,
                currentRoomSettings: null,
                addDeviceId: null,
                deleteDeviceId: null
            }
        },
        computed: {
            freeDevices: function () {
                let takenDevicesList = []
                this.rooms.forEach((room) => {
                    takenDevicesList.push(...room.devices)
                })
                if (this.devices !== null) {
                    return this.devices.filter(device => !takenDevicesList.find(dev => device.id === dev.id))
                }
            }
        },
        created: function () {
            this.$options.sockets.onopen = (event) => {
                console.log('Listening to changes from backend(Rooms.vue)...')
            }

            this.$options.sockets.onmessage = (event) => {
                var json = JSON.parse(event.data)
                if (json.status === 'OK' && json.type === 'roomsChange') {
                    this.getRooms()
                }
            }

            this.getRooms()
            this.getDevices()
        },
        methods: {
            getRooms() {
                console.log('Loading rooms...')
                AXIOS.get('http://localhost:8080/api/rooms')
                    .then((response) => {
                        console.log(response)
                        this.rooms = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            },
            getDevices() {
                console.log('Loading devices...')
                AXIOS.get('http://localhost:8080/api/devices')
                    .then((response) => {
                        console.log(response)
                        this.devices = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            },
            addRoom() {
                //Validate
                let isInvalidName = false
                let isInvalidType = false

                this.rooms.forEach((item) => {
                    if (item.name === this.roomName) {
                        isInvalidName = true
                        this.invalidName = true
                    }
                })

                if (this.roomName === '') {
                    isInvalidName = true
                    this.invalidName = true
                }

                if (this.roomType === '') {
                    isInvalidType = true
                    this.invalidType = true
                }

                if (isInvalidName === true || isInvalidType === true) {
                    return
                }

                AXIOS.post('http://localhost:8080/api/rooms',
                    {
                        "name": this.roomName,
                        "type": this.roomType
                    }).then((response) => {
                    this.rooms.push({
                        "name": this.roomName,
                        "type": this.roomType,
                        "devices": []
                    })
                    this.roomName = ''
                    this.roomType = 'Living room'
                    console.log(response)
                }).catch((error) => {
                    console.log(error)
                })

            },
            deleteRoom() {
                AXIOS.delete('http://localhost:8080/api/rooms/' + this.deleteSelectedRoom).then((response) => {
                    this.getRooms()
                    console.log(response)
                }).catch((error) => {
                    console.log(error)
                })
            },
            toggleAddForm() {
                if (this.addFormShown) {
                    this.addFormShown = false
                    this.toggleAddFormText = '<i class="fas fa-plus" style="margin-right: 5px"></i> New room'
                } else {
                    this.addFormShown = true
                    this.toggleAddFormText = '<i class="fas fa-angle-up" id="upArrow"></i> Hide form'
                }
            },
            settings(room) {
                this.currentRoomSettings = room
                this.roomSettingsShown = true
            },
            addDeviceToRoom() {
                AXIOS.post('http://localhost:8080/api/rooms/' + this.currentRoomSettings.name + '/' + this.addDeviceId).then((response) => {
                    console.log('Adding device ' + this.addDeviceId + ' to room ' + this.currentRoomSettings.name)
                }).catch((error) => {
                    console.log(error)
                })
            },
            deleteDeviceFromRoom() {
                AXIOS.delete('http://localhost:8080/api/rooms/' + this.currentRoomSettings.name + '/' + this.deleteDeviceId).then((response) => {
                    this.getRooms()
                    console.log(response)
                }).catch((error) => {
                    console.log(error)
                })
            }
        },
        mounted: function () {
            this.getRooms()

            /*setInterval(() => {
                this.getRooms()
            }, 1000)*/
        }
    }
</script>

<style>
    span {
        margin-bottom: 10px;
    }

    #roomsRoot {
        padding-bottom: 20px;
        background-color: #ededed;
    }

    .hero {
        margin-bottom: 20px;
    }

    #upArrow {
        margin-right: 5px;
    }

    #addForm {
        margin-top: 10px;
        padding: 30px;
    }

    #formContainer {
        margin-bottom: 30px;
        margin-top: 10px;
    }

    #newRoomButton {
        margin-top: 15px;
    }

    #deleteButton {
        margin-top: 10px;
    }

    #genSettingsIcon {
        margin-right: 5px;
    }

    #generalSettingsButton {
        font-weight: bold;
    }

    #tagType {
        font-weight: bold;
    }

    .card-content {
        padding: 10px 0 0;
    }

    .device-card-header {
        background: linear-gradient(to bottom, #2b344b 20%, #512837 100%);
        padding: 30px;
    }

    .device-card-header:hover {
        background: linear-gradient(to bottom, #414e71 20%, #783a51 100%);
        padding: 30px;
    }

    .device-card-header-title {
        color: #fff;
    }

    #open-bar {
        width: 100%;
        height: 40px;
        background-color: #4d71da;
        margin: 0;
        padding: 10px 0 10px;
    }

    #open-bar:hover {
        background-color: #5781fb;
    }

    #open-bar h5 {
        color: #fff;
    }

    #open-bar h5 i {
        margin-left: 5px;
    }

    #contentHolder {
        background-color: #ededed;
        padding-top: 20px;
    }

    #roomsLeftBorder {
        float: left;
        background-color: #4272db;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }


</style>