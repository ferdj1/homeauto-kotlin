<template>
    <div id="deviceRoot">
        <!-- Heading of the devices -->
        <section class="hero is-primary" id="titleHero">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">
                        {{device.name}}
                    </h1>
                    <h2 class="subtitle">
                        {{device.manufacturer}}
                    </h2>
                </div>
            </div>
        </section>

        <section class="hero is-dark" id="infoHero">
            <div class="hero-body" id="infoHeroBody">
                <div class="container">
                    <div class="tags has-addons">
                        <span class="tag is-medium is-marginless">Connection</span>
                        <span class="tag is-success is-medium has-text-weight-bold is-marginless"
                              v-if="device.id in sessions">ON</span>
                        <span class="tag is-danger is-medium has-text-weight-bold" v-else>OFF</span>
                    </div>

                    <div class="tags has-addons">
                        <span class="tag is-medium is-marginless">Type</span>
                        <span class="tag is-link is-medium has-text-weight-bold is-marginless">{{device.type}}</span>
                    </div>
                </div>
            </div>
        </section>

        <section id="commandSection">
            <div class="container columns">
                <div class="column is-half" v-if="device.id in sessions">
                    <div id="commandBox">
                        <transition name="fade">
                            <div id="commandBoxLeftBorder"></div>
                        </transition>
                        <transition name="fade">
                            <h1 class="title" id="commandBoxTitle">Commands</h1>
                        </transition>
                        <transition name="fade" tag="li">
                            <ul>
                                <!-- Controls -->
                                <li class="box commandBox" v-for="command in device.commands"
                                    :key="device.commands.indexOf(command)" v-if="command.getSetType === 'SET'">
                                    <h1 class="title is-5">{{command.name}}</h1>
                                    <h3 class="subtitle is-7">{{command.description}}</h3>
                                    <vue-form-generator id="vfg" :schema="schemas[device.commands.indexOf(command)]"
                                                        :model="models[device.commands.indexOf(command)]"
                                                        :options="formOptions"
                                                        @validated="onValidated"></vue-form-generator>
                                </li>
                            </ul>
                        </transition>
                    </div>
                </div>
                <div class="column is-half" v-else>
                    <transition name="fadeAlert">
                        <!-- No device connected alert -->
                        <article class="message" id="noDevicesAlert">
                            <div class="message-header">
                                <p>Connect your device to control it</p>
                            </div>
                            <div class="message-body">Device is not connected at the moment. Once connected, device
                                controls
                                will show
                                up here.
                            </div>
                        </article>
                    </transition>
                </div>
                <div class="column is-half" v-if="device.id in sessions">
                    <div id="infoBox">
                        <!-- Information from get functions -->
                        <transition name="fade">
                            <div id="infoBoxLeftBorder"></div>
                        </transition>
                        <transition name="fade">
                            <div id="refreshInfoBox">
                                <button class="button is-primary is-pulled-right is-clearfix"
                                        v-on:click="getValueObjects">
                                    <i class="fas fa-sync-alt"></i>
                                </button>
                            </div>
                        </transition>
                        <transition name="fade">
                            <h1 class="title" id="infoBoxTitle">Device Information</h1>
                        </transition>
                        <transition name="fade" tag="li">
                            <ul>
                                <li class="box commandBox" v-for="command in device.commands"
                                    :key="device.commands.indexOf(command)" v-if="command.getSetType === 'GET'">
                                    <h1 class="title is-5">{{command.displayText}}:
                                        {{getResultByCommandId(command.id)}}</h1>
                                </li>
                            </ul>
                        </transition>
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
        name: "Device",
        data() {
            return {
                device: null,
                sessions: null,
                executionObject: null,
                schemas: [],
                models: [],
                formOptions: null,
                valuesObjects: []
            }
        },
        created: function () {
            this.$options.sockets.onopen = (event) => {
                console.log('Listening to changes from backend(Device.vue)...')
            }

            this.$options.sockets.onmessage = (event) => {
                var json = JSON.parse(event.data)
                if (json.status === 'OK' && (json.type === 'sessionConnected' || json.type === 'connectionClosed')) {
                    this.getSessions()
                }

                if (json.status === 'OK' && json.type === 'clientToServerExecutedCommand') {
                    console.log(json.type)
                    this.getValueObjects()
                    this.getSessions()
                }
            }

            this.getDevice()
            this.getSessions()

            setTimeout(() => {
                this.generateSchemasAndModels()
                this.getValueObjects()
            }, 100)

            setTimeout(() => {
                this.getValueObjects()
            }, 100)

            setTimeout(() => {
                this.fixFormCSS()
            }, 200)

            /*setInterval(() => {
                this.getValueObjects()
            }, 10000)*/

        },
        methods: {
            getDevice() {
                let url = 'http://localhost:8080/api/devices/' + this.$route.params.id

                AXIOS.get(url)
                    .then((response) => {
                        console.log('DEVICE: ')
                        console.log(response)
                        this.device = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            },
            getSessions() {
                AXIOS.get('http://localhost:8080/api/websocketsessions')
                    .then((response) => {
                        //console.log(response)
                        this.sessions = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            },
            getValueObjects() {
                console.log('GET VALUE OBJECTS')
                this.valuesObjects = []
                this.device.commands.forEach((command) => {
                    if (command.getSetType === 'GET') {
                        let json = {
                            "messageType": "executeCommand",
                            "deviceId": this.device.id,
                            "commandId": command.id,
                            "parameters": []
                        }

                        AXIOS.post('http://localhost:8080/api/executeCommand', json)
                            .then((response) => {
                                // Empty
                            }).catch((error) => {
                            console.log(error)
                        })

                        AXIOS.get('http://localhost:8080/api/executedCommands')
                            .then((response) => {
                                console.log('THIS')
                                console.log(response.data)
                                response.data.forEach((executedCommand) => {
                                    if (executedCommand.commandId === command.id) {
                                        this.valuesObjects.push({
                                            commandId: command.id,
                                            result: executedCommand.result
                                        })
                                    }
                                })
                            }).catch((error) => {
                            console.log(error)
                        })

                    }
                })


            },
            getResultByCommandId(commandId) {
                var result;
                this.valuesObjects.forEach((valuesObject) => {
                    if (valuesObject.commandId === commandId) {
                        result = valuesObject.result
                    }
                })
                return result
            },
            executeCommand(executionJSON) {
                let json = {
                    "messageType": "executeCommand",
                    "deviceId": executionJSON.deviceId,
                    "commandId": executionJSON.commandId,
                    "parameters": []
                }

                for (let key in executionJSON) {
                    if (executionJSON.hasOwnProperty(key)) {
                        if (key !== 'deviceId' && key !== 'commandId') {
                            json["parameters"].push(executionJSON[key])
                        }
                    }
                }

                AXIOS.post('http://localhost:8080/api/executeCommand', json).then((response) => {
                    console.log(response)
                }).catch((error) => {
                    console.log(error)
                })
            },
            fixFormCSS() {
                //Hack
                let inputElements = document.getElementsByClassName("form-control")
                for (let i = 0; i < inputElements.length; i++) {
                    inputElements.item(i).classList.add('input')
                }
            },

            generateSchemasAndModels() {
                let deviceId = this.$data.device.id

                let commands = this.$data.device.commands

                //Generate options
                this.$data.formOptions = {
                    validateAfterLoad: true,
                    validateAfterChanged: true,
                    validationErrorClass: "is-danger",
                    validationSuccessClass: "is-success"
                }
                //


                commands.forEach((command) => {
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

                    this.$data.models.push(modelObject)

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
                    let submitButtonFieldObject = {
                        buttons: [
                            {
                                classes: "button is-link",
                                label: command.name,
                                onclick: (model) => {
                                    this.executeCommand(JSON.parse(JSON.stringify(model)))
                                    setTimeout(() => {
                                        this.getValueObjects()
                                    }, 10)
                                    setTimeout(() => {
                                        this.getValueObjects()
                                    }, 20)
                                },
                                disabled() {
                                    return true
                                }
                            }
                        ]
                    }

                    schemaObject.fields.push(submitButtonFieldObject)

                    this.$data.schemas.push(schemaObject)

                })

                console.log(this.$data.models)
            },

            onValidated(isValid, errors) {
                console.log('VALIDITY')
                console.log(isValid)
                console.log('ERRORS:')
                console.log(errors)
            }
        },
        mounted: function () {
            setTimeout(() => {
                this.getSessions();
            }, 10);

            setTimeout(() => {
                this.generateSchemasAndModels();
            }, 20);

            setTimeout(() => {
                this.fixFormCSS();
            }, 50);

            /*setInterval(() => {
                this.getSessions();
            }, 3000);*/
        }
    }
</script>

<style>

    #titleHero {
        margin-bottom: 0;
    }

    #titleHero .hero-body {
        padding-bottom: 24px;
    }

    #infoHero {
        margin-top: 0;
        margin-bottom: 0;
    }

    #infoHeroBody {
        padding-bottom: 12px;
        padding-top: 12px;
    }

    #infoHeroBody .container div {
        display: inline-block;
        margin: 10px 10px 10px 0;
    }

    .commandBox {
        margin-left: 30px;
        margin-bottom: 10px;
    }

    #commandSection {
        padding-top: 20px;
        background-color: #ededed;
        height: 100%;
    }

    fieldset {
        border: 0;
    }

    fieldset * {
        margin-bottom: 5px;
    }

    .fade-enter-active, .fade-leave-active {
        transition: all .5s ease;
    }

    .fade-enter, .fade-leave-to {
        transform: translateY(-50px);
        opacity: 0;
    }

    .fadeAlert-enter-active, .fadeAlert-leave-active {
        transition: all 1s ease;
    }

    .fadeAlert-enter, .fadeAlert-leave-to {
        transform: translateY(50px);
        opacity: 0;
    }

    #infoBox {
    }

    #infoBoxLeftBorder {
        float: left;
        background-color: #3fd2b4;
        width: 12px;
        height: 36px;
        margin-left: 30px;
    }

    #commandBoxLeftBorder {
        float: left;
        background-color: #4272db;
        width: 12px;
        height: 36px;
        margin-left: 30px;
    }

    #infoBoxTitle {
        margin-left: 52px;
    }

    #commandBoxTitle {
        margin-left: 52px;
    }

</style>