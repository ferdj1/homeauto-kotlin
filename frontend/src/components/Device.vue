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

        <section>
            <div class="container">
                <div class="box" v-for="command in device.commands">
                    <form method="post">
                        <!-- Name, description -->
                        <h1 class="title is-3">{{command.name}}</h1>
                        <h3 class="subtitle is-5">{{command.description}}</h3>

                        <!-- GET type command -->
                        <h4 class="subtitle is-5" v-if="command.getSetType === 'GET'">{{command.displayText}}</h4>
                        <h4 class="subtitle is-5" v-if="command.getSetType === 'GET'">
                            <!-- TODO: Value from device --></h4>
                        <button class="button is-link" v-if="command.getSetType === 'GET'"><i
                                class="fas fa-sync-alt"></i></button>

                        <!-- SET type command -->

                        <div class="control"
                             v-if="command.parameterDescriptions.length === 0 && command.getSetType !== 'GET'">
                            <button class="button is-link">
                                <i class="fas fa-check-circle"></i>
                            </button>
                        </div>

                        <div class="field" v-for="paramDesc in command.parameterDescriptions">
                            <!-- NO_LIMIT STRING -->
                            <div class="control">
                                <input class="input" type="text"
                                       v-if="paramDesc.type === 'string' && paramDesc.limitType === 'NO_LIMIT'">
                            </div>
                            <!-- LIMITED_SET STRING -->
                            <div class="control">
                                <div class="select"
                                     v-if="paramDesc.type === 'string' && paramDesc.limitType === 'LIMITED_SET'">
                                    <select>
                                        <option v-for="value in paramDesc.values">{{value}}</option>
                                    </select>
                                </div>
                            </div>

                            <!-- NO_LIMIT NUMBER -->
                            <div class="control">
                                <input class="input" type="text"
                                       v-if="paramDesc.type === 'number' && paramDesc.limitType === 'NO_LIMIT'">
                            </div>
                            <!-- LIMITED_SET NUMBER -->
                            <div class="control">
                                <div class="select"
                                     v-if="paramDesc.type === 'number' && paramDesc.limitType === 'LIMITED_SET'">
                                    <select>
                                        <option v-for="value in paramDesc.values">{{value}}</option>
                                    </select>
                                </div>
                            </div>

                            <!-- MIN_MAX NUMBER -->
                            <div class="field" v-if="paramDesc.type === 'number' && paramDesc.limitType === 'MIN_MAX'">
                                <label class="label">Minimum: {{paramDesc.min === null ? 'None' :
                                    paramDesc.min}}</label>
                                <label class="label">Maximum: {{paramDesc.max === null ? 'None' :
                                    paramDesc.max}}</label>
                                <input class="input" type="text"
                                       v-if="paramDesc.type === 'number' && paramDesc.limitType === 'MIN_MAX'">
                            </div>

                            <!-- BOOLEAN -->
                            <div class="control" v-if="paramDesc.type === 'boolean'">
                                <label class="radio">
                                    <input type="radio" name="answer">
                                    <i class="fas fa-check"></i>
                                </label>
                                <label class="radio">
                                    <input type="radio" name="answer">
                                    <i class="fas fa-times"></i>
                                </label>
                            </div>

                            <!-- Submit button -->
                            <div class="control">
                                <button class="button is-link">
                                    <i class="fas fa-check-circle"></i>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import {AXIOS} from '../http-common'

    export default {
        name: "Device",
        data() {
            return {
                device: null,
                sessions: null
            }
        },
        created: function () {
            this.getDevice()
            this.getSessions()
        },
        methods: {
            getDevice() {
                let url = 'http://localhost:8080/api/devices/' + this.$route.params.id

                AXIOS.get(url)
                    .then((response) => {
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
            }
        },
        mounted: function () {
            this.getSessions();

            setInterval(() => {
                this.getSessions();
            }, 3000);
        }
    }
</script>

<style scoped>
    #titleHero {
        margin-bottom: 0;
    }

    #titleHero .hero-body {
        padding-bottom: 24px;
    }

    #infoHero {
        margin-top: 0;
    }

    #infoHeroBody {
        padding-bottom: 12px;
        padding-top: 12px;
    }

    #infoHeroBody .container div {
        display: inline-block;
        margin: 10px 10px 10px 0;
    }
</style>