<template>
    <div class="column is-one-fifth sidebar is-fullheight hero" id="sidebarRoot">
        <ul class="menu-list">
            <li><router-link to="/devices"><span class="icon is-large"><i class="fas fa-lg fa-desktop"></i></span><span class="is-size-4">Devices</span></router-link>
            </li>
            <li><router-link to="/subscriptions"><span class="icon is-large"><i class="fas fa-lg fa-list-ul"></i></span><span
                    class="is-size-4">Subscriptions</span></router-link></li>
            <li><router-link to="/scheduler"><span class="icon is-large"><i class="fas fa-lg fa-calendar-alt"></i></span><span
                    class="is-size-4">Scheduler</span></router-link></li>
            <li><router-link to="/rooms"><span class="icon is-large"><i class="fas fa-lg fa-bed"></i></span><span class="is-size-4">Rooms</span></router-link>
            </li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: 'Sidebar',
        data() {
            return {
                selectedRoute: '/'
            }
        }
    }
</script>

<style>
    .sidebar {
        background-color: #3d3d3d;
    }

    .sidebar * {
        font-family: "Roboto", Arial, sans-serif;
    }

    .sidebar span {
        color: #fcfcfc;
    }

    #sidebarRoot .router-link-active {
        background: #4272db;
        border-bottom-left-radius: 0;
        border-top-left-radius: 0;
    }

    #sidebarRoot .router-link-active:hover {
        background: #4272db;
    }

    #sidebarRoot {
        padding-top: 25px;
        margin-bottom: 0;
    }

    #sidebarRoot a:hover {
        background-color: #606060;
    }

    #sidebarRoot li span {
        margin-bottom: 0;
    }
</style>