import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueFormGenerator from "vue-form-generator"
import VueNativeSock from 'vue-native-websocket'

Vue.use(VueFormGenerator)
Vue.use(VueNativeSock, 'ws://localhost:8080/cws', {
    reconnection: true, // (Boolean) whether to reconnect automatically (false)
    reconnectionAttempts: 5, // (Number) number of reconnection attempts before giving up (Infinity),
    reconnectionDelay: 3000, // (Number) how long to initially wait before attempting a new (1000)
    format: 'json'
})

Vue.config.productionTip = false

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
