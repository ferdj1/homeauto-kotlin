import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueFormGenerator from "vue-form-generator"

Vue.use(VueFormGenerator)

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
