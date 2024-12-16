/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'
import router from './router';
import axios from 'axios';

// Configure Axios globally
axios.defaults.baseURL = 'http://localhost:8080';
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// Composables


import { createApp } from 'vue'
const app = createApp(App)

app.config.globalProperties.$axios = axios

registerPlugins(app)

app.mount('#app')
