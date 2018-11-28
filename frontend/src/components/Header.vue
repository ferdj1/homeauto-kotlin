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
                            <p class="is-6 has-text-white">
                                Signed in as: Mark Smith
                            </p>
                        </div>
                        <div class="navbar-item">
                            <a class="button is-warning">
                                Log out
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    </div>
</template>

<script>
    export default {
        name: 'Header'
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
</style>