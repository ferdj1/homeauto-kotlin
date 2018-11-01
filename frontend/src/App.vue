<template>
    <div id="app">
        <Header></Header>
        <div class="columns">
            <Sidebar></Sidebar>
            <!-- Content of index page -->
            <div class="column is-paddingless">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script>
    import Header from "./components/Header";
    import Sidebar from "./components/Sidebar";

    export default {
        name: 'app',
        components: {Header, Sidebar}
    }
</script>

<style lang="css">
    @import "../node_modules/bulma/css/bulma.css";
    @import url("https://use.fontawesome.com/releases/v5.4.1/css/all.css");
    @import url("https://fonts.googleapis.com/css?family=Roboto+Condensed");
</style>