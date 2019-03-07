<template>
    <div id="subscriptionsRoot">
        <!-- Heading of the subscriptions -->
        <section class="hero is-primary" id="subscriptionsHeader">
            <div class="hero-body">
                <div class="container">
                    <h1 class="title">
                        Subscriptions
                    </h1>
                    <h2 class="subtitle">
                        Here you can let each device know if some other device executed an operation and let it act
                        accordingly.
                    </h2>
                </div>
            </div>
        </section>

        <div id="content-holder">
            <div class="container">
                <div id="observed-section">
                    <div id="observedLeftBorder"></div>
                    <h1 class="title col-title is-marginless">Observed command</h1>

                    <div class="columns" id="observedColumns">
                        <div class="column">
                            <div id="observedDeviceSelectLeftBorder"></div>
                            <h1 class="title col-title is-4 is-marginless">Device</h1>
                            <div class="control selectors">
                                <div class="select">
                                    <select name="observedDeviceId" id="observedDeviceId"
                                            v-model="observedDeviceId">
                                        <option v-for="device in devices">
                                            {{device.id}}
                                        </option>
                                    </select>
                                </div>
                                <div id="observedDeviceWrapper" v-if="observedDeviceId !== null">
                                    <h3 class="title is-5 is-marginless">
                                        Selected device:
                                    </h3>
                                    <div class="box descriptionBox">
                                        <h1 class="title is-5 is-marginless">{{observedDevice.name}}</h1>
                                        <h2 class="subtitle is-6 is-marginless">Manufacturer:
                                            {{observedDevice.manufacturer}}</h2>
                                        <p class="subtitle is-6 is-marginless">Type: {{observedDevice.type}}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="column">
                            <div id="observedCommandSelectLeftBorder"></div>
                            <h1 class="title col-title is-4 is-marginless">Command</h1>
                            <div class="control selectors">
                                <div class="select" v-if="observedDeviceId !== null">
                                    <select name="observedCommandId" id="observedCommandId"
                                            v-model="observedCommandId" @change="generateSchemasAndModels">
                                        <option v-for="command in observedDevice.commands">
                                            {{command.id}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div id="observedCommandWrapper" v-if="observedCommandId !== null">
                                <h3 class="title is-5 is-marginless">
                                    Selected command:
                                </h3>
                                <div class="box descriptionBox">
                                    <h1 class="title is-5 is-marginless">{{observedCommand.id}}</h1>
                                    <h2 class="subtitle is-6 is-marginless">Name: {{observedCommand.name}}</h2>
                                    <p class="subtitle is-6 is-marginless">Description:
                                        {{observedCommand.description}}</p>
                                </div>
                            </div>
                        </div>
                        <div class="column">
                            <div id="observedParameterSelectLeftBorder"></div>
                            <h1 class="title col-title is-4 is-marginless">Parameters information</h1>
                            <div v-if="observedCommandId !== null">
                                <div class="box descriptionBox"
                                     v-for="parameterDescription in observedCommand.parameterDescriptions">
                                    <h1 class="title is-5 is-marginless">Name: {{parameterDescription.name}}</h1>
                                    <h2 class="subtitle is-6 is-marginless">Type: {{parameterDescription.type}}</h2>
                                </div>
                            </div>
                        </div>

                        <div class="column">
                            <div id="observedReturnsSelectLeftBorder"></div>
                            <h1 class="title col-title is-4 is-marginless">Return information</h1>
                            <div v-if="observedCommandId !== null && observedCommand.getSetType === 'GET'">
                                <div class="box descriptionBox">
                                    <h1 class="title is-5 is-marginless">Type:
                                        {{observedCommand.returnType.charAt(0).toUpperCase()
                                        +observedCommand.returnType.slice(1)}}</h1>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="observer-section">
                    <div id="observerLeftBorder"></div>
                    <h1 class="title col-title is-marginless">Observer</h1>

                    <div class="columns" id="observerColumns">
                        <div class="column">
                            <div id="observerDeviceSelectLeftBorder"></div>
                            <h1 class="title col-title is-4 is-marginless">Device</h1>
                            <div class="control selectors">
                                <div class="select">
                                    <select name="observerDeviceId" id="observerDeviceId"
                                            v-model="observerDeviceId">
                                        <option v-for="device in devices">
                                            {{device.id}}
                                        </option>
                                    </select>
                                </div>
                                <div id="observerDeviceWrapper" v-if="observerDeviceId !== null">
                                    <h3 class="title is-5 is-marginless">
                                        Selected device:
                                    </h3>
                                    <div class="box descriptionBox">
                                        <h1 class="title is-5 is-marginless">{{observerDevice.name}}</h1>
                                        <h2 class="subtitle is-6 is-marginless">Manufacturer:
                                            {{observerDevice.manufacturer}}</h2>
                                        <p class="subtitle is-6 is-marginless">Type: {{observerDevice.type}}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="column">
                            <div id="observerCommandSelectLeftBorder"></div>
                            <h1 class="title col-title is-4 is-marginless">Command</h1>
                            <div class="control selectors">
                                <div class="select" v-if="observerDeviceId !== null">
                                    <select name="observerCommandId" id="observerCommandId"
                                            v-model="observerCommandId" @change="generateSchemasAndModels">
                                        <option v-for="command in observerDevice.commands">
                                            {{command.id}}
                                        </option>
                                    </select>
                                </div>
                            </div>
                            <div id="observerCommandWrapper" v-if="observerCommandId !== null">
                                <h3 class="title is-5 is-marginless">
                                    Selected command:
                                </h3>
                                <div class="box descriptionBox">
                                    <h1 class="title is-5 is-marginless">{{observerCommand.id}}</h1>
                                    <h2 class="subtitle is-6 is-marginless">Name: {{observerCommand.name}}</h2>
                                    <p class="subtitle is-6 is-marginless">Description:
                                        {{observerCommand.description}}</p>
                                </div>
                            </div>
                        </div>

                        <div class="column">
                            <div id="observerParameterSelectLeftBorder"></div>
                            <h1 class="title col-title is-4 is-marginless">Parameters</h1>
                            <div v-if="observerCommandId !== null">
                                <div class="columns">
                                    <!-- Command Input Schema -->
                                    <div class="column">
                                        <vue-form-generator id="vfg-subscriptions-params"
                                                            :schema="commandParamSchema"
                                                            :model="commandParamModel"
                                                            :options="formOptions"
                                                            @validated="onValidated"
                                                            v-if="observerCommandId"></vue-form-generator>
                                    </div>

                                    <!-- Checkboxes for existing values schema -->
                                    <div class="column">
                                        <vue-form-generator id="vfg-subscriptions-params-check"
                                                            :schema="commandParamCheckSchema"
                                                            :model="commandParamCheckModel"
                                                            :options="formOptions"
                                                            @validated="onValidated"
                                                            v-if="observerCommandId && observedCommandId"></vue-form-generator>
                                    </div>

                                    <!-- CommandId Input Schema -->
                                    <div class="column">
                                        <vue-form-generator id="vfg-subscriptions-params-use-observed"
                                                            :schema="commandParamObservedValueSchema"
                                                            :model="commandParamObservedValueModel"
                                                            :options="formOptions"
                                                            @validated="onValidated"
                                                            v-if="observerCommandId && observedCommandId"></vue-form-generator>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div id="observer-optional-section">
                    <div id="observerOptionalLeftBorder"></div>
                    <h1 class="title col-title is-marginless">Execute when...</h1>
                    <div v-if="observedCommandId !== null">
                        <div class="columns">
                            <!-- Checkboxes to pick which parameter will be compared to a certain value -->
                            <div class="column">
                                <vue-form-generator id="vfg-subscriptions-params-conditional-check"
                                                    :schema="conditionalCheckSchema"
                                                    :model="conditionalCheckModel"
                                                    :options="formOptions"
                                                    @validated="onValidated"
                                                    v-if="observedCommandId"></vue-form-generator>
                            </div>

                            <!-- Comparison sign picker -->
                            <div class="column">
                                <vue-form-generator id="vfg-subscriptions-params-conditional-sign"
                                                    :schema="conditionalSignSchema"
                                                    :model="conditionalSignModel"
                                                    :options="formOptions"
                                                    @validated="onValidated"
                                                    v-if="observedCommandId"></vue-form-generator>
                            </div>

                            <!-- Value picker -->
                            <div class="column">
                                <vue-form-generator id="vfg-subscriptions-params-conditional-value"
                                                    :schema="conditionalValueSchema"
                                                    :model="conditionalValueModel"
                                                    :options="formOptions"
                                                    @validated="onValidated"
                                                    v-if="observedCommandId"></vue-form-generator>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="control">
                    <button class="button is-success" id="subscribeButton" v-on:click="subscribe">
                        Subscribe
                    </button>
                </div>
            </div>
            <hr>
            <div class="section" id="subscriptions">
                <div id="subscriptionsLeftBorder"></div>
                <h1 class="title">Subscriptions</h1>

                <div class="box" v-for="subscription in subscriptions">
                    <div class="control">
                        <button class="button is-danger is-small is-pulled-right"
                                v-on:click="deleteSubscription(subscription)">
                            <i class="fas fa-times"></i> <span class="delete-button-text">Remove subscription</span>
                        </button>
                    </div>
                    <div id="observedCommandLeftBorder"></div>
                    <h1 class="title is-5">Observed command</h1>
                    <div class="tag is-medium">{{getDeviceById(subscription.observedDeviceId).name}}</div>
                    <h1 class="title is-3">{{getCommandByIds(subscription.observedDeviceId,
                        subscription.observedCommandId).name}}</h1>
                    <h1 class="subtitle is-6">{{getCommandByIds(subscription.observedDeviceId,
                        subscription.observedCommandId).description}}</h1>
                    <hr>
                    <div id="observersLeftBorder"></div>
                    <h1 class="title is-5">Observers</h1>
                    <div class="notification" v-for="observer in subscription.observerList">
                        <div class="control">
                            <button class="button is-warning is-small is-pulled-right"
                                    v-on:click="deleteObserver(subscription, observer)">
                                <i class="fas fa-times"></i> <span class="delete-button-text">Remove</span>
                            </button>
                        </div>
                        <div class="tag is-medium is-dark">{{getDeviceById(observer.observerDeviceId).name}}</div>
                        <h1 class="title is-3">{{getCommandByIds(observer.observerDeviceId,
                            observer.observerCommandId).name}}</h1>
                        <h1 class="subtitle is-6">{{getCommandByIds(observer.observerDeviceId,
                            observer.observerCommandId).description}}</h1>


                        <div class="columns">
                            <div class="column">
                                <div id="descParametersLeftBorder"></div>
                                <h1 class="title is-5 observer-parameters-title">Parameters <span
                                        v-if="(observer.parameters.length === 0)">: None</span></h1>
                                <h1 class="subtitle is-6" v-for="(parameter, index) in observer.parameters">
                                    {{getCommandByIds(observer.observerDeviceId,
                                    observer.observerCommandId).parameterDescriptions[index].name}}:
                                    {{!parameter.usesObservedValue ? parameter.value :
                                    getCommandByIds(subscription.observedDeviceId,
                                    subscription.observedCommandId).parameterDescriptions[parameter.observedParameterIndex].name}}
                                </h1>
                            </div>
                            <div class="column">
                                <div id="descConditionsLeftBorder"></div>
                                <h1 class="title is-5 observer-parameters-title">Conditions <span
                                        v-if="(Object.values(observer.parameterComparators).every(item => item.useOption === false))">: None</span>
                                </h1>
                                <div v-for="(parameterComparator, index) in observer.parameterComparators">
                                    <h1 class="subtitle is-6" v-if="parameterComparator.useOption === true">
                                        {{getCommandByIds(subscription.observedDeviceId,
                                        subscription.observedCommandId).parameterDescriptions[index].name}} is
                                        {{convertComparisonCodeToText(parameterComparator.sign)}}
                                        {{parameterComparator.comparedValue}}
                                    </h1>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


</template>

<script>
    import {AXIOS} from '../http-common'
    import validators from "vue-form-generator/src/utils/validators"

    export default {
        name: 'Subscriptions',
        data() {
            return {
                devices: [],
                subscriptions: [],
                observedDeviceId: null,
                observedCommandId: null,
                observerDeviceId: null,
                observerCommandId: null,
                parameters: [],
                commandParamSchema: null,
                commandParamModel: null,
                commandParamCheckSchema: null,
                commandParamCheckModel: null,
                commandParamObservedValueSchema: null,
                commandParamObservedValueModel: null,
                conditionalCheckSchema: null,
                conditionalCheckModel: null,
                conditionalSignSchema: null,
                conditionalSignModel: null,
                conditionalValueSchema: null,
                conditionalValueModel: null,
                formOptions: null,
            }
        },
        created: function () {
            this.$options.sockets.onopen = (event) => {
                console.log('Listening to changes from backend(Subscriptions.vue)...')
            }

            this.$options.sockets.onmessage = (event) => {
                var json = JSON.parse(event.data)
                if (json.status === 'OK' && json.type === 'subscriptionsChange') {
                    this.getSubscriptions()
                }
            }

            this.getDevices()
            this.getSubscriptions()
        },
        computed: {
            observedDevice() {
                this.observedCommandId = null
                return this.devices.find(device => device.id === this.observedDeviceId)
            },
            observedCommand() {
                if (this.observedDeviceId !== null && this.observedDeviceId !== undefined) {
                    return this.observedDevice.commands.find(command => command.id === this.observedCommandId)
                } else {
                    return undefined
                }
            },
            observerDevice() {
                this.observerCommandId = null
                return this.devices.find(device => device.id === this.observerDeviceId)
            },
            observerCommand() {
                if (this.observerDeviceId !== null && this.observerDeviceId !== undefined) {
                    return this.observerDevice.commands.find(command => command.id === this.observerCommandId)
                } else {
                    return undefined
                }
            }
        },
        methods: {
            getDevices() {
                AXIOS.get('http://localhost:8080/api/devices')
                    .then((response) => {
                        console.log(response)
                        this.devices = response.data
                    }).catch((error) => {
                    console.log(error)
                })
            },

            getSubscriptions() {
                AXIOS.get('http://localhost:8080/api/subscriptions')
                    .then((response) => {
                        console.log(response)
                        this.subscriptions = response.data
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

            deleteSubscription(subscription) {
                AXIOS.delete('http://localhost:8080/api/subscriptions/', {data: subscription}).then((response) => {
                    this.getSubscriptions()
                    console.log(response)
                }).catch((error) => {
                    console.log(error)
                })
            },

            deleteObserver(subscription, observer) {
                let json = {
                    subscription: subscription,
                    observer: observer
                }

                AXIOS.delete('http://localhost:8080/api/subscriptions/observers', {data: json}).then((response) => {
                    this.getSubscriptions()
                    console.log(response)
                }).catch((error) => {
                    console.log(error)
                })
            },

            convertComparisonCodeToText(code) {
                switch (code) {
                    case 'EQ':
                        return 'equal to'
                    case 'NE':
                        return 'not equal to'
                    case 'LT':
                        return 'less than'
                    case 'LE':
                        return 'less than or equal to'
                    case 'GT':
                        return 'greater than'
                    case 'GE':
                        return 'greater than or equal to'
                }
            },

            subscribe() {
                // Observer parameters
                let params = []
                let numberOfObserverParameters = this.observerCommand.parameterDescriptions.length

                for (let i = 0; i < numberOfObserverParameters; i++) {
                    let parameterObject = {
                        usesObservedValue: this.commandParamCheckModel[i.toString()],
                        value: this.commandParamModel[i.toString()],
                        observedParameterIndex: this.commandParamObservedValueModel[i.toString()]
                    }
                    params.push(parameterObject)
                }

                // Conditionals
                let paramComparators = []
                let numberOfObservedParameters = this.observedCommand.parameterDescriptions.length

                for (let i = 0; i < numberOfObservedParameters; i++) {
                    let conditionalObject = {
                        useOption: this.conditionalCheckModel[i.toString()],
                        optionType: this.observedCommand.parameterDescriptions[i].type,
                        sign: this.conditionalSignModel[i.toString()],
                        comparedValue: this.conditionalValueModel[i.toString()]
                    }
                    paramComparators.push(conditionalObject)
                }

                let json = {
                    observedDeviceId: this.observedDeviceId,
                    observedCommandId: this.observedCommandId,
                    observerDeviceId: this.observerDeviceId,
                    observerCommandId: this.observerCommandId,
                    parameters: params,
                    parameterComparators: paramComparators
                }

                AXIOS.post('http://localhost:8080/api/subscriptions', json).then((response) => {
                    console.log(response)
                    this.getSubscriptions()
                }).catch((error) => {
                    console.log(error)
                })
            },

            generateSchemasAndModels() {
                if (this.$data.observerDeviceId === null || this.$data.observerDeviceId === undefined
                    || this.$data.observerCommandId === null || this.$data.observerCommandId === undefined) {
                    return
                }


                //Generate options
                this.$data.formOptions = {
                    validateAfterLoad: false,
                    validateAfterChanged: true,
                    validationErrorClass: "is-danger",
                    validationSuccessClass: "is-success"
                }
                //

                ///////////////////
                //Generate models//
                ///////////////////

                // Generate models for observer
                let modelObject = {}
                let modelCheckObject = {}
                let modelObservedValue = {}

                let parameterDescriptions = this.observerCommand.parameterDescriptions

                parameterDescriptions.forEach(function (parameterDescription, index) {
                    if (parameterDescription === null) return
                    let parameterName = parameterDescription.name
                    let defaultParameterValue = parameterDescription.defaultValue
                    modelObject[index.toString()] = defaultParameterValue
                    modelCheckObject[index.toString()] = false
                    modelObservedValue[index.toString()] = null
                })

                this.$data.commandParamModel = modelObject
                this.$data.commandParamCheckModel = modelCheckObject
                this.$data.commandParamObservedValueModel = modelObservedValue


                // Generate models for conditional checking
                let conditionalCheckModel = {}
                let conditionalSignModel = {}
                let conditionalValueModel = {}

                let observedCommandParameterDescriptions = this.observedCommand.parameterDescriptions

                observedCommandParameterDescriptions.forEach(function (parameterDescription, index) {
                    if (parameterDescription === null) return
                    conditionalCheckModel[index.toString()] = false
                    conditionalSignModel[index.toString()] = null
                    conditionalValueModel[index.toString()] = parameterDescription.defaultValue
                })

                this.$data.conditionalCheckModel = conditionalCheckModel
                this.$data.conditionalSignModel = conditionalSignModel
                this.$data.conditionalValueModel = conditionalValueModel

                /////////////////////
                //Generate schemas//
                ///////////////////

                // Generate schemas for observer
                let schemaObject = {
                    fields: []
                }

                let schemaCheckObject = {
                    fields: []
                }

                let schemaObservedValueObject = {
                    fields: []
                }

                parameterDescriptions.forEach((parameterDescription, index) => {
                    if (parameterDescription === null) return

                    let fieldCheckObject = {
                        type: "checkbox",
                        label: "Use observed value",
                        model: index.toString(),
                        default: false
                    }
                    schemaCheckObject.fields.push(fieldCheckObject)

                    switch (parameterDescription.limitType) {
                        case 'LIMITED_SET':
                            let fieldObject = {
                                type: "select",
                                label: parameterDescription.name,
                                model: index.toString(),
                                values: parameterDescription.values,
                                selectOptions: {
                                    hideNoneSelectedText: true
                                },
                                disabled: () => {
                                    this.fixFormCSS()
                                    return this.$data.commandParamCheckModel[index.toString()]
                                },
                                styleClasses: 'control field',
                                required: true
                            }

                            if (this.$data.observedCommandId !== null) {
                                let vals = []

                                /*
                                if(this.observedCommand.returnType && this.observedCommand.returnType) {
                                    vals.push({"name": "Return Value", "index": -1})
                                }*/

                                this.observedCommand.parameterDescriptions.forEach((paramDesc, index) => {
                                    if (paramDesc.limitType === parameterDescription.limitType
                                        && paramDesc.type === parameterDescription.type
                                        && paramDesc.values.every(val => parameterDescription.values.includes(val))) {
                                        vals.push({"name": paramDesc.name, "id": index})
                                    }
                                })

                                if (vals.length > 0) {
                                    let observedValueFieldObject = {
                                        type: "select",
                                        label: "Select observed parameter",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.commandParamCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: this.$data.commandParamCheckModel[index.toString()]
                                    }

                                    schemaObservedValueObject.fields.push(observedValueFieldObject)
                                }
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
                                    model: index.toString(),
                                    min: parameterDescription.min,
                                    max: parameterDescription.max,
                                    validator: validators.number,
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return this.$data.commandParamCheckModel[index.toString()]
                                    },
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
                                    model: index.toString(),
                                    min: parameterDescription.min,
                                    max: parameterDescription.max,
                                    validator: [validators.number, validators.integer],
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return this.$data.commandParamCheckModel[index.toString()]
                                    },
                                    styleClasses: '',
                                    required: true
                                    //hint: 'Min: ' + parameterDescription.min + '  Max: ' + parameterDescription.max
                                }

                                schemaObject.fields.push(fieldObject)
                            }

                            if (this.$data.observedCommandId !== null) {
                                let vals = []
                                // Add server-side check to see that it is in defined bounds
                                if (this.observedCommand.returnType && this.observedCommand.returnType) {
                                    vals.push({"name": "Return Value", "id": -1})
                                }

                                this.observedCommand.parameterDescriptions.forEach((paramDesc, index) => {
                                    if (paramDesc.limitType === parameterDescription.limitType
                                        && paramDesc.type === parameterDescription.type
                                        && (paramDesc.min >= parameterDescription.min && paramDesc.max <= parameterDescription.max)) {
                                        vals.push({"name": paramDesc.name, "id": index})
                                    }
                                })

                                if (vals.length > 0) {
                                    let observedValueFieldObject = {
                                        type: "select",
                                        label: "Select observed parameter",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.commandParamCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: this.$data.commandParamCheckModel[index.toString()]
                                    }

                                    schemaObservedValueObject.fields.push(observedValueFieldObject)
                                }
                            }

                            break

                        case 'NO_LIMIT':
                            if (parameterDescription.type === 'string') {
                                let inputType = parameterDescription.specialType
                                if (inputType === null) {
                                    inputType = "text"
                                }

                                let fieldObject = {
                                    type: "input",
                                    inputType: inputType,
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    validator: validators.string,
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return this.$data.commandParamCheckModel[index.toString()]
                                    },
                                    required: true
                                }

                                schemaObject.fields.push(fieldObject)
                            } else if (parameterDescription.type === 'number') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    validator: validators.number,
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return this.$data.commandParamCheckModel[index.toString()]
                                    },
                                    required: true

                                }

                                schemaObject.fields.push(fieldObject)
                            } else if (parameterDescription.type === 'integer') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    validator: [validators.number, validators.integer],
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return this.$data.commandParamCheckModel[index.toString()]
                                    },
                                    required: true
                                }

                                schemaObject.fields.push(fieldObject)
                            }

                            if (this.$data.observedCommandId !== null) {
                                let vals = []
                                // Add server-side check to see that it is in defined bounds
                                if (this.observedCommand.returnType && this.observedCommand.returnType) {
                                    vals.push({"name": "Return Value", "id": -1})
                                }

                                this.observedCommand.parameterDescriptions.forEach((paramDesc, index) => {
                                    if (paramDesc.type === parameterDescription.type) {
                                        vals.push({"name": paramDesc.name, "id": index})
                                    }
                                })

                                if (vals.length > 0) {
                                    let observedValueFieldObject = {
                                        type: "select",
                                        label: "Select observed parameter",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.commandParamCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: this.$data.commandParamCheckModel[index.toString()]
                                    }

                                    schemaObservedValueObject.fields.push(observedValueFieldObject)
                                }
                            }

                            break

                        case 'UNDEFINED':
                            if (parameterDescription.type === 'boolean') {
                                let fieldObject = {
                                    type: "checkbox",
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return this.$data.commandParamCheckModel[index.toString()]
                                    },
                                    default: false
                                }

                                schemaObject.fields.push(fieldObject)
                            }

                            if (this.$data.observedCommandId !== null) {
                                let vals = []
                                // Add server-side check to see that it is in defined bounds
                                if (this.observedCommand.returnType && this.observedCommand.returnType) {
                                    vals.push({"name": "Return Value", "id": -1})
                                }

                                this.observedCommand.parameterDescriptions.forEach((paramDesc, index) => {
                                    if (paramDesc.limitType === parameterDescription.limitType
                                        && paramDesc.type === parameterDescription.type) {
                                        vals.push({"name": paramDesc.name, "id": index})
                                    }
                                })

                                if (vals.length > 0) {
                                    let observedValueFieldObject = {
                                        type: "select",
                                        label: "Select observed parameter",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.commandParamCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: this.$data.commandParamCheckModel[index.toString()]
                                    }

                                    schemaObservedValueObject.fields.push(observedValueFieldObject)
                                }
                            }

                            break

                        default:
                            return
                    }
                })
                this.$data.commandParamSchema = schemaObject
                this.$data.commandParamCheckSchema = schemaCheckObject
                this.$data.commandParamObservedValueSchema = schemaObservedValueObject


                // Generate schemas for conditional checking
                let conditionalCheckSchema = {
                    fields: []
                }

                let conditionalSignSchema = {
                    fields: []
                }

                let conditionalValueSchema = {
                    fields: []
                }

                observedCommandParameterDescriptions.forEach((parameterDescription, index) => {
                    if (parameterDescription === null) return

                    let conditionalCheckFieldObject = {
                        type: "checkbox",
                        label: 'Check the box to compare: ' + parameterDescription.name,
                        model: index.toString(),
                        default: false
                    }
                    conditionalCheckSchema.fields.push(conditionalCheckFieldObject)

                    switch (parameterDescription.limitType) {
                        case 'LIMITED_SET':
                            let fieldObject = {
                                type: "select",
                                label: parameterDescription.name,
                                model: index.toString(),
                                values: parameterDescription.values,
                                selectOptions: {
                                    hideNoneSelectedText: true
                                },
                                disabled: () => {
                                    this.fixFormCSS()
                                    return !this.$data.conditionalCheckModel[index.toString()]
                                },
                                styleClasses: 'control field',
                                required: true
                            }

                            if (this.$data.observedCommandId !== null) {
                                let vals = [{name: 'equal to', id: 'EQ'},
                                    {name: 'not equal to', id: 'NE'}]

                                let conditionalSignSchemaFieldObject = {
                                    type: "select",
                                    label: "Select comparison sign",
                                    model: index.toString(),
                                    values: vals,
                                    selectOptions: {
                                        hideNoneSelectedText: true,
                                        value: "id",
                                        name: "name"
                                    },
                                    disabled: () => {
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    styleClasses: 'control field',
                                    required: true
                                }

                                conditionalSignSchema.fields.push(conditionalSignSchemaFieldObject)
                            }

                            conditionalValueSchema.fields.push(fieldObject)
                            break

                        case 'MIN_MAX':
                            if (parameterDescription.type === 'string') {
                                return
                            } else if (parameterDescription.type === 'number') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    min: parameterDescription.min,
                                    max: parameterDescription.max,
                                    validator: validators.number,
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    styleClasses: '',
                                    required: true
                                    //hint: 'Min: ' + parameterDescription.min + '  Max: ' + parameterDescription.max
                                }

                                conditionalValueSchema.fields.push(fieldObject)
                            } else if (parameterDescription.type === 'integer') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    min: parameterDescription.min,
                                    max: parameterDescription.max,
                                    validator: [validators.number, validators.integer],
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    styleClasses: '',
                                    required: true
                                    //hint: 'Min: ' + parameterDescription.min + '  Max: ' + parameterDescription.max
                                }

                                conditionalValueSchema.fields.push(fieldObject)
                            }

                            if (this.$data.observedCommandId !== null) {
                                let vals = [{name: 'equal to', id: 'EQ'},
                                    {name: 'not equal to', id: 'NE'},
                                    {name: 'less than', id: 'LT'},
                                    {name: 'less than or equal to', id: 'LE'},
                                    {name: 'greater than', id: 'GT'},
                                    {name: 'greater than or equal to', id: 'GE'}]


                                let conditionalSignSchemaFieldObject = {
                                    type: "select",
                                    label: "Select comparison sign",
                                    model: index.toString(),
                                    values: vals,
                                    selectOptions: {
                                        hideNoneSelectedText: true,
                                        value: "id",
                                        name: "name"
                                    },
                                    disabled: () => {
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    styleClasses: 'control field',
                                    required: true
                                }

                                conditionalSignSchema.fields.push(conditionalSignSchemaFieldObject)
                            }

                            break

                        case 'NO_LIMIT':
                            if (parameterDescription.type === 'string') {
                                let inputType = parameterDescription.specialType
                                if (inputType === null) {
                                    inputType = "text"
                                }

                                let fieldObject = {
                                    type: "input",
                                    inputType: inputType,
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    validator: validators.string,
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    required: true
                                }

                                conditionalValueSchema.fields.push(fieldObject)

                                if (this.$data.observedCommandId !== null) {
                                    let vals = [{name: 'equal to', id: 'EQ'},
                                        {name: 'not equal to', id: 'NE'}]


                                    let conditionalSignSchemaFieldObject = {
                                        type: "select",
                                        label: "Select comparison sign",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.conditionalCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: true
                                    }

                                    conditionalSignSchema.fields.push(conditionalSignSchemaFieldObject)
                                }

                            } else if (parameterDescription.type === 'number') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    validator: validators.number,
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    required: true

                                }

                                conditionalValueSchema.fields.push(fieldObject)

                                if (this.$data.observedCommandId !== null) {
                                    let vals = [{name: 'equal to', id: 'EQ'},
                                        {name: 'not equal to', id: 'NE'},
                                        {name: 'less than', id: 'LT'},
                                        {name: 'less than or equal to', id: 'LE'},
                                        {name: 'greater than', id: 'GT'},
                                        {name: 'greater than or equal to', id: 'GE'}]


                                    let conditionalSignSchemaFieldObject = {
                                        type: "select",
                                        label: "Select comparison sign",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.conditionalCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: true
                                    }

                                    conditionalSignSchema.fields.push(conditionalSignSchemaFieldObject)
                                }
                            } else if (parameterDescription.type === 'integer') {
                                let fieldObject = {
                                    type: "input",
                                    inputType: "number",
                                    label: parameterDescription.name,
                                    model: index.toString(),
                                    validator: [validators.number, validators.integer],
                                    disabled: () => {
                                        this.fixFormCSS()
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    required: true
                                }

                                conditionalValueSchema.fields.push(fieldObject)

                                if (this.$data.observedCommandId !== null) {
                                    let vals = [{name: 'equal to', id: 'EQ'},
                                        {name: 'not equal to', id: 'NE'},
                                        {name: 'less than', id: 'LT'},
                                        {name: 'less than or equal to', id: 'LE'},
                                        {name: 'greater than', id: 'GT'},
                                        {name: 'greater than or equal to', id: 'GE'}]


                                    let conditionalSignSchemaFieldObject = {
                                        type: "select",
                                        label: "Select comparison sign",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.conditionalCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: true
                                    }

                                    conditionalSignSchema.fields.push(conditionalSignSchemaFieldObject)
                                }
                            }

                            break

                        case 'UNDEFINED':
                            if (parameterDescription.type === 'boolean') {
                                let fieldObject = {
                                    type: "select",
                                    label: "Select value",
                                    model: index.toString(),
                                    values: [true, false],
                                    selectOptions: {
                                        hideNoneSelectedText: true
                                    },
                                    disabled: () => {
                                        return !this.$data.conditionalCheckModel[index.toString()]
                                    },
                                    styleClasses: 'control field',
                                    required: true
                                }

                                conditionalValueSchema.fields.push(fieldObject)

                                if (this.$data.observedCommandId !== null) {
                                    let vals = [{name: 'equal to', id: 'EQ'},
                                        {name: 'not equal to', id: 'NE'}]

                                    let conditionalSignSchemaFieldObject = {
                                        type: "select",
                                        label: "Select comparison sign",
                                        model: index.toString(),
                                        values: vals,
                                        selectOptions: {
                                            hideNoneSelectedText: true,
                                            value: "id",
                                            name: "name"
                                        },
                                        disabled: () => {
                                            return !this.$data.conditionalCheckModel[index.toString()]
                                        },
                                        styleClasses: 'control field',
                                        required: true
                                    }

                                    conditionalSignSchema.fields.push(conditionalSignSchemaFieldObject)
                                }
                            }

                            break

                        default:
                            return
                    }

                })

                this.$data.conditionalCheckSchema = conditionalCheckSchema
                this.$data.conditionalSignSchema = conditionalSignSchema
                this.$data.conditionalValueSchema = conditionalValueSchema

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
        }
    }
</script>

<style>
    #subscriptionsRoot {
        padding-bottom: 20px;
        height: 100%;
        background-color: #ededed;
    }

    #subscriptionsHeader {
        margin-bottom: 0;
    }

    #content-holder {
        background-color: #ededed;
        padding-top: 20px;
    }

    #observedLeftBorder {
        float: left;
        background-color: #4272db;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #observerLeftBorder {
        float: left;
        background-color: #3fd2b4;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #observerOptionalLeftBorder {
        float: left;
        background-color: #a855dd;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #observedDeviceSelectLeftBorder {
        float: left;
        background-color: #a5dcdd;
        width: 6px;
        height: 27px;
        margin-right: 5px;
    }

    #observedCommandSelectLeftBorder {
        float: left;
        background-color: #ddd4b2;
        width: 6px;
        height: 27px;
        margin-right: 5px;
    }

    #observedParameterSelectLeftBorder {
        float: left;
        background-color: #ddbcd6;
        width: 6px;
        height: 27px;
        margin-right: 5px;
    }


    #observedReturnsSelectLeftBorder {
        float: left;
        background-color: #acafdd;
        width: 6px;
        height: 27px;
        margin-right: 5px;
    }

    #observerDeviceSelectLeftBorder {
        float: left;
        background-color: #a5dcdd;
        width: 6px;
        height: 27px;
        margin-right: 5px;
    }

    #observerCommandSelectLeftBorder {
        float: left;
        background-color: #ddd4b2;
        width: 6px;
        height: 27px;
        margin-right: 5px;
    }

    #observerParameterSelectLeftBorder {
        float: left;
        background-color: #ddbcd6;
        width: 6px;
        height: 27px;
        margin-right: 5px;
    }

    #subscriptionsLeftBorder {
        float: left;
        background-color: #ddbc1b;
        width: 12px;
        height: 36px;
        margin-right: 10px;
    }

    #observedCommandLeftBorder {
        float: left;
        background-color: #ddd4b2;
        width: 6px;
        height: 22px;
        margin-right: 5px;
    }

    #observersLeftBorder {
        float: left;
        background-color: #ddb2cb;
        width: 6px;
        height: 22px;
        margin-right: 5px;
    }

    #descParametersLeftBorder {
        float: left;
        background-color: #97b3dd;
        width: 6px;
        height: 22px;
        margin-right: 5px;
    }

    #descConditionsLeftBorder {
        float: left;
        background-color: #83ddbe;
        width: 6px;
        height: 22px;
        margin-right: 5px;
    }

    #subscribeButton {
        margin-right: 0;
        margin-top: 10px;
    }

    #observed-section {
        margin-bottom: 20px;
    }

    #observed-section > * {
        margin-left: 10px;
        margin-right: 10px;
    }


    #observedColumns {
        margin-top: 5px;
    }


    #observer-section {
        margin-bottom: 20px;
    }

    #observer-section > * {
        margin-left: 10px;
        margin-right: 10px;
    }


    #observerColumns {
        margin-top: 5px;
    }

    .selectors {
        margin-top: 8px;
    }

    .select {
        margin-bottom: 10px;
    }

    .descriptionBox {
        margin-top: 8px;
    }
</style>