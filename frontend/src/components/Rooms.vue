<template>
    <div id="roomsRoot">
        <section class="hero is-primary">
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
                                <select name="roomType" id="roomType" v-model="roomType" v-bind:class="{'is-danger': invalidType}">
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
            <div class="container">
                <button class="button is-warning is-pulled-right is-clearfix"
                        v-on:click="settingsShown = !settingsShown">
                    <i class="fas fa-cog"></i>
                </button>
                <h1 class="title">Rooms</h1>
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
                                        <select name="deleteRoomName" id="deleteRoomName" v-model="deleteSelectedRoom">
                                            <option v-for="room in rooms">
                                                {{room.name}}
                                            </option>
                                        </select>
                                    </div>
                                </div>

                                <div class="control">
                                    <button class="button is-danger" id="deleteButton" v-on:click="settingsShown = !settingsShown">Delete</button>
                                </div>
                            </form>

                        </section>

                        <footer class="modal-card-foot">
                            <button class="button is-primary" v-on:click="settingsShown = !settingsShown">Done</button>
                        </footer>
                    </div>
                </div>
            </div>
            <br>
            <div class="box" v-for="room in rooms">
                <span class="tag is-warning">{{room.type}}</span>
                <h2 class="title is-3">{{room.name}}</h2>
            </div>
        </div>
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
                toggleAddFormText: 'Create new room',
                settingsShown: false,
                deleteSelectedRoom: '',
                invalidName: false,
                invalidType: false
            }
        },
        created: function () {
            AXIOS.get('http://localhost:8080/api/rooms')
                .then((response) => {
                    console.log(response)
                    this.rooms = response.data
                }).catch((error) => {
                console.log(error)
            })
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
            addRoom() {
                //Validate
                let isInvalidName = false
                let isInvalidType = false

                this.rooms.forEach((item) => {
                    if(item.name === this.roomName) {
                        isInvalidName = true
                        this.invalidName = true
                    }
                })

                if(this.roomName === '') {
                    isInvalidName = true
                    this.invalidName = true
                }

                if(this.roomType === '') {
                    isInvalidType = true
                    this.invalidType = true
                }

                if(isInvalidName === true || isInvalidType === true) {
                    return
                }

                AXIOS.post('http://localhost:8080/api/rooms',
                    {
                        "name": this.roomName,
                        "type": this.roomType
                    }).then((response) => {
                    this.rooms.push({
                        "name": this.roomName,
                        "type": this.roomType
                    })
                    this.roomName = '';
                    this.roomType = 'Living room';
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
                    this.toggleAddFormText = 'Create new room'
                } else {
                    this.addFormShown = true
                    this.toggleAddFormText = '<i class="fas fa-angle-up" id="upArrow"></i> Hide form'
                }
            }
        }
    }
</script>

<style>
    span {
        margin-bottom: 10px;
    }

    #roomsRoot {
        margin-bottom: 20px;
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
        margin-bottom: 20px;
    }

    #newRoomButton {
        margin-top: 15px;
    }

    #deleteButton {
        margin-top: 10px;
    }
</style>