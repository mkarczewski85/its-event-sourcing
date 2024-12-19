
/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from 'vue-router/auto'
// import { routes } from 'vue-router/auto-routes'
import Login from '../components/Login.vue';
import ResetPassword from '../components/ResetPassword.vue';
import ResetPasswordByToken from "@/components/ResetPasswordByToken.vue";
import AccountSettings from '../components/AccountSettings.vue';
import AddIssue from '../components/AddIssue.vue';
import IssueList from '../components/IssueList.vue';
import IssueDetails from '../components/IssueDetails.vue';
import PageNotFound from "../components/PageNotFound.vue";
import AdministrationPanel from "@/components/AdministrationPanel.vue";

const routes = [
  {path: '/', name: 'Login', component: Login},
  {path: '/administration', name: 'Administration', component: AdministrationPanel},
  {path: '/reset-password', name: 'ResetPassword', component: ResetPassword},
  {path: '/reset-password/:token', name: 'ResetPasswordByToken', component: ResetPasswordByToken},
  {path: '/settings', name: 'AccountSettings', component: AccountSettings},
  {path: '/add-issue', name: 'AddIssue', component: AddIssue},
  {path: '/issues', name: 'IssueList', component: IssueList},
  {path: '/issues/:id', name: 'IssueDetails', component: IssueDetails},
  {path: '/page-not-found', name: 'PageNotFound', component: IssueDetails},
  {path: '/:pathMatch(.*)*', name: 'NotFound', component: PageNotFound }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})


// Workaround for https://github.com/vitejs/vite/issues/11804
router.onError((err, to) => {
  if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
    if (!localStorage.getItem('vuetify:dynamic-reload')) {
      console.log('Reloading page to fix dynamic import error')
      localStorage.setItem('vuetify:dynamic-reload', 'true')
      location.assign(to.fullPath)
    } else {
      console.error('Dynamic import error, reloading page did not fix it', err)
    }
  } else {
    console.error(err)
  }
})

router.isReady().then(() => {
  localStorage.removeItem('vuetify:dynamic-reload')
})

export default router
