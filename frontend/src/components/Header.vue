<template>
    <div id="headerRoot">
        <nav class="navbar is-dark" role="navigation" aria-label="main navigation">
            <div class="navbar-brand">
                <router-link class="navbar-item" to="/">
                    <img id="logo" src="../assets/images/logo-27px.png" alt="SmartHome">
                    <span id="logoText" class="subtitle is-5 has-text-white">SmartHome</span>
                </router-link>
            </div>

            <div id="mainNavbar" class="navbar-menu">
                <div class="navbar-end">
                    <div class="level">
                        <div class="navbar-item">
                        </div>
                        <div class="navbar-item">
                            <!--<button class="button is-warning" v-on:click="getDevices">-->
                                <!--<i class="fas fa-sync-alt" id="update-button-icon"></i>-->
                                <!--<span id="update-button-text" class="is-marginless">Check device updates</span>-->
                            <!--</button>-->
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    </div>
</template>

<script>
    export default {
        name: 'Header',
        data() {
            return {
                devices: []
            }
        },
        methods: {
        }
    }
</script>

<style>
    .navbar {
        padding-left: 20px;
        padding-right: 20px;
    }

    #logo {
        margin-right: 10px;
    }

    #logoText {
        margin-bottom: 0px;
        font-family: "Roboto", Arial, sans-serif;
    }

    #update-button-icon {
        margin-right: 5px;
    }

    #update-button-text {
        font-weight: bold;
    }
</style>