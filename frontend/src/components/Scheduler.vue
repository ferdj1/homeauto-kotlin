<template>
    <div id="schedulerRoot">
        <!-- Heading of the scheduler -->
        <section class="hero is-primary is-marginless">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">
                        Scheduler
                    </h1>
                    <h2 class="subtitle">
                        Here you can let your devices execute operations at certain intervals.
                    </h2>
                </div>
            </div>
        </section>

        <section id="content-holder">
            <form @submit.prevent="scheduleCommand" id="schedule-form">
                <div class="section">
                    <div class="columns" id="schedulerColumns">
                        <div class="column">
                            <div id="deviceSelectLeftBorder"></div>
                            <h1 class="title col-title">Device</h1>
                            <div class="control">
                                <div class="select">
                                    <select name="scheduledDeviceId" id="scheduledDeviceId" v-model="scheduledDeviceId">
                                        <option v-for="device in devices">
                                            {{device.id}}
                                        </option>
                                    </select>
                                </div>
                                <div id="scheduledDeviceWrapper" v-if="scheduledDeviceId !== null">
                                    <h3 class="title is-5 is-marginless">
                                        Selected device:
                                    </h3>
                                    <div class="box">
                                        <h1 class="title is-5 is-marginless">{{scheduledDevice.name}}</h1>
                                        <h2 class="subtitle is-6 is-marginless">Manufacturer:
                                            {{scheduledDevice.manufacturer}}</h2>
                                        <p class="subtitle is-6 is-marginless">Type: {{scheduledDevice.type}}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="column">
                            <div id="commandSelectLeftBorder"></div>
                            <h1 class="title col-title">Command</h1>
                            <div class="control">
                                <div class="select" v-if="scheduledDeviceId !== null">
                                    <select name="scheduledCommandId" id="scheduledCommandId"
                                            v-model="scheduledCommandId" @change="generateSchemasAndModels">
                                        <option v-for="command in scheduledDevice.commands">
                                            {{command.id}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div id="scheduledCommandWrapper" v-if="scheduledCommandId !== null">
                                <h3 class="title is-5 is-marginless">
                                    Selected command:
                                </h3>
                                <div class="box">
                                    <h1 class="title is-5 is-marginless">{{scheduledCommand.id}}</h1>
                                    <h2 class="subtitle is-6 is-marginless">Name: {{scheduledCommand.name}}</h2>
                                    <p class="subtitle is-6 is-marginless">Description:
                                        {{scheduledCommand.description}}</p>
                                </div>
                            </div>
                        </div>
                        <div class="column">
                            <div id="parameterSelectLeftBorder"></div>
                            <h1 class="title col-title">Parameters</h1>
                            <vue-form-generator id="vfg-scheduler" :schema="commandParamSchema"
                                                :model="commandParamModel"
                                                :options="formOptions"
                                                @validated="onValidated" v-if="scheduledCommandId"></vue-form-generator>
                        </div>
                        <div class="column">
                            <div id="intervalSelectLeftBorder"></div>
                            <h1 class="title col-title">Interval</h1>
                            <div class="columns interval-cols" v-if="scheduledCommandId !== null">
                                <div class="control column">
                                    <input class="input" type="number" placeholder="60" v-model="interval">
                                </div>
                                <div class="control column">
                                    <div class="select">
                                        <select name="intervalMetric" id="intervalMetric" v-model="intervalMetric">
                                            <option>seconds</option>
                                            <option>minutes</option>
                                            <option>hours</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="control">
                        <button class="button is-success" id="scheduleButton">
                            Schedule
                        </button>
                    </div>
                </div>
            </form>
            <div class="section" id="scheduled-commands">
                <div id="scheduledCommandsLeftBorder"></div>
                <h1 class="title">Scheduled commands</h1>

                <div class="box" v-for="scheduledCommand in scheduledCommands">
                    <div class="control">
                        <button class="button is-danger is-small is-pulled-right"
                                v-on:click="deleteScheduledCommand(scheduledCommand)">
                            <i class="fas fa-times"></i> <span class="delete-button-text">Remove</span>
                        </button>
                    </div>
                    <div class="tag is-medium">{{getDeviceById(scheduledCommand.deviceId).name}}</div>
                    <h1 class="title is-3">{{getCommandByIds(scheduledCommand.deviceId,
                        scheduledCommand.commandId).name}}</h1>
                    <h1 class="subtitle is-6">{{getCommandByIds(scheduledCommand.deviceId,
                        scheduledCommand.commandId).description}}</h1>
                    <hr>
                    <div class="columns">
                        <div class="column">
                            <h3 class="title is-4 is-marginless">Parameters</h3>
                            <div class="pi-content">
                                <h4 class="is-size-5" v-for="(parameter, index) in scheduledCommand.parameters">
                                    {{getCommandByIds(scheduledCommand.deviceId,
                                    scheduledCommand.commandId).parameterDescriptions[index].name}}: {{parameter}}
                                </h4>
                            </div>
                        </div>
                        <div class="column">
                            <h3 class="title is-4 is-marginless">Interval</h3>
                            <h4 class="is-size-5 pi-content">{{scheduledCommand.interval}}
                                [{{scheduledCommand.intervalMetric}}]</h4>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import {AXIOS} from '../http-common'
    import validators from "vue-form-generator/src/utils/validators";


    export default {
        name: 'Scheduler',
        data() {
            return {
                devices: [],
                scheduledDeviceId: null,
                scheduledCommandId: null,
                commandParamSchema: null,
                commandParamModel: null,
                formOptions: null,
                interval: 60,
                intervalMetric: 'seconds',
                scheduledCommands: []
            }
        },
        created: function () {
            this.$options.sockets.onopen = (event) => {
                console.log('Listening to changes from backend(Scheduler.vue)...')
            }

            this.$options.sockets.onmessage = (event) => {
                var json = JSON.parse(event.data)
                if (json.status === 'OK' && json.type === 'schedulerChange') {
                    this.getScheduledCommands()
                }
            }

            this.getDevices()
            this.getScheduledCommands()


        },
        computed: {
            scheduledDevice() {
                this.scheduledCommandId = null
                return this.devices.find(device => device.id === this.scheduledDeviceId)
            },
            scheduledCommand() {
                if(this.scheduledDeviceId !== null && this.scheduledDeviceId !== undefined) {
                    return this.scheduledDevice.commands.find(command => command.id === this.scheduledCommandId)
                } else {
                    return undefined
                }
            },


        },
        methods: {
            deleteScheduledCommand(scheduledCommand) {
                console.log('THIS')
                console.log(JSON.stringify(scheduledCommand))
                AXIOS.delete('http://localhost:8080/api/scheduledCommands/', {data: scheduledCommand}).then((response) => {
                    this.getScheduledCommands()
                    console.log(response)
                }).catch((error) => {
                    console.log(error)
                })
            },
            getCommandByIds(deviceId, commandId) {
                let device = this.getDeviceById(deviceId)
                return device.commands.find(command => command.id === commandId)
            },
            getDeviceById(deviceId) {
                return this.devices.find(device => device.id === deviceId)
            },
            getDevices() {
                AXIOS.get('http://localhost:8080/api/devices')
                    .then((response) => {
                        console.log(response)
                        this.devices = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            },
            getScheduledCommands() {
                AXIOS.get('http://localhost:8080/api/scheduledCommands')
                    .then((response) => {
                        console.log(response)
                        this.scheduledCommands = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            },
            generateSchemasAndModels() {
                let deviceId = this.$data.scheduledDeviceId

                let command = this.scheduledCommand

                //Generate options
                this.$data.formOptions = {
                    validateAfterLoad: true,
                    validateAfterChanged: true,
                    validationErrorClass: "is-danger",
                    validationSuccessClass: "is-success"
                }
                //


                //Generate model
                let modelObject = {
                    deviceId: deviceId,
                    commandId: command.id
                }
                let parameterDescriptions = command.parameterDescriptions

                parameterDescriptions.forEach(function (parameterDescription) {
                    if (parameterDescription === null) return

                    let parameterName = parameterDescription.name
                    let defaultParameterValue = parameterDescription.defaultValue
                    modelObject[parameterName] = defaultParameterValue
                })

                this.$data.commandParamModel = modelObject

                //Generate schema
                let schemaObject = {
                    fields: []
                }

                parameterDescriptions.forEach((parameterDescription) => {
                    if (parameterDescription === null) return

                    switch (parameterDescription.limitType) {
                        case 'LIMITED_SET':
                            let fieldObject = {
                                type: "select",
                                label: parameterDescription.name,
                                model: parameterDescription.name,
                                values: parameterDescription.values,
                                selectOptions: {
                                    hideNoneSelectedText: true
                                },
                                styleClasses: 'control field',
                                required: true
                            }

                            schemaObject.fields.push(fieldObject)
                            break

                        case 'MIN_MAX':
                            if (parameterDescription.type === 'string') {
                                return
                            } else if (parameterDescription.type === 'number') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: parameterDescription.name,
                                    min: parameterDescription.min,
                                    max: parameterDescription.max,
                                    validator: validators.number,
                                    styleClasses: '',
                                    required: true
                                    //hint: 'Min: ' + parameterDescription.min + '  Max: ' + parameterDescription.max
                                }

                                schemaObject.fields.push(fieldObject)
                            } else if (parameterDescription.type === 'integer') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: parameterDescription.name,
                                    min: parameterDescription.min,
                                    max: parameterDescription.max,
                                    validator: [validators.number, validators.integer],
                                    styleClasses: '',
                                    required: true
                                    //hint: 'Min: ' + parameterDescription.min + '  Max: ' + parameterDescription.max
                                }

                                schemaObject.fields.push(fieldObject)
                            }
                            break

                        case 'NO_LIMIT':
                            if (parameterDescription.type === 'string') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "text",
                                    label: parameterDescription.name,
                                    model: parameterDescription.name,
                                    validator: validators.string,
                                    required: true
                                }

                                schemaObject.fields.push(fieldObject)
                            } else if (parameterDescription.type === 'number') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: parameterDescription.name,
                                    validator: validators.number,
                                    required: true

                                }

                                schemaObject.fields.push(fieldObject)
                            } else if (parameterDescription.type === 'integer') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: parameterDescription.name,
                                    validator: [validators.number, validators.integer],
                                    required: true
                                }

                                schemaObject.fields.push(fieldObject)
                            }
                            break

                        case 'UNDEFINED':
                            if (parameterDescription.type === 'boolean') {
                                let fieldObject = {
                                    type: "checkbox",
                                    label: parameterDescription.name,
                                    model: parameterDescription.name,
                                    default: false
                                }

                                schemaObject.fields.push(fieldObject)
                            }
                            break

                        default:
                            return
                    }
                })
                this.$data.commandParamSchema = schemaObject


                setTimeout(() => {
                    this.fixFormCSS();
                }, 50);
            },
            onValidated(isValid, errors) {
                console.log('VALIDITY')
                console.log(isValid)
                console.log('ERRORS:')
                console.log(errors)
            },
            fixFormCSS() {
                //Hack
                let inputElements = document.getElementsByClassName("form-control")
                for (let i = 0; i < inputElements.length; i++) {
                    inputElements.item(i).classList.add('input')
                }
            },
            scheduleCommand() {
                let params = []

                Object.keys(this.commandParamModel).forEach((key) => {
                    if (key !== 'deviceId' && key !== 'commandId') {
                        params.push(this.commandParamModel[key])
                    }
                })

                let json = {
                    "id": {},
                    "deviceId": this.scheduledDeviceId,
                    "commandId": this.scheduledCommandId,
                    "parameters": params,
                    "interval": this.interval,
                    "intervalMetric": this.intervalMetric
                }

                AXIOS.post('http://localhost:8080/api/scheduledCommands', json).then((response) => {
                    console.log(response)
                    this.getScheduledCommands()
                }).catch((error) => {
                    console.log(error)
                })
            }
        }
    }
</script>

<style>
    #schedulerRoot {
        padding-bottom: 20px;
        height: 100%;
        background-color: #ededed;
    }


    #content-holder {
        background-color: #ededed;
        padding-top: 20px;
    }


    #deviceSelectLeftBorder {
        float: left;
        background-color: #4272db;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #commandSelectLeftBorder {
        float: left;
        background-color: #3fd2b4;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #parameterSelectLeftBorder {
        float: left;
        background-color: #a855dd;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #intervalSelectLeftBorder {
        float: left;
        background-color: #dd7b82;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #scheduledCommandsLeftBorder {
        float: left;
        background-color: #ddbc1b;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #schedulerColumns {
        padding-left: 50px;
        padding-right: 50px;
    }

    #scheduleButton {
        margin-left: 50px;
    }

    .col-title {

    }

    .interval-cols .column {
        padding-left: 6px;
        padding-right: 6px;
    }

    #scheduledDeviceName {
        font-weight: bold;
    }

    #scheduledDeviceWrapper {
        margin-top: 20px;
    }

    #scheduledDeviceWrapper .box {
        margin-top: 5px;
    }

    #scheduledCommandWrapper {
        margin-top: 20px;
    }

    #scheduledCommandWrapper .box {
        margin-top: 5px;
    }

    #scheduled-commands {
        margin-left: 45px;
    }

    #schedule-form {
        border-bottom: 2px solid #b1b3c7;
    }

    .pi-content {
        margin-top: 7px;
    }

    .delete-button-text {
        margin-left: 5px;
        margin-bottom: 0;
        font-weight: bold;
    }
</style>