import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios, axios)

axios.defaults.baseURL = 'http://localhost:' + window.location.port

// axios.defaults.baseURL = 'http://localhost:8080'

Vue.config.productionTip = false

new Vue({
  render: (h) => h(App),
}).$mount('#app')
