<template>
  <v-container class="d-flex justify-center">
    <v-sheet
      elevation="2"
      color="primary"
      rounded
      class="pa-4 mt-10"
      width="400"
    >
      <v-sheet
        elevation="4"
        color="white"
        rounded
        class="pa-6 mb-2"
      >
        <div class="d-flex justify-center">
          <v-img
            max-width="200"
            aspect-ratio="16/9"
            cover
            src="../assets/logo.png"></v-img>
        </div>
        <h1 class="text-gray py-5 font-weight-bold text-h4 text-center">Logowanie</h1>
        <v-alert
          v-if="errors"
          type="error"
          :value="true"
          class="mb-4"
        >
          {{ errors }}
        </v-alert>

        <v-form @submit.prevent="handleLogin">
          <v-text-field
            label="Email"
            v-model="form.login"
            :rules="emailRules"
            type="email"
            required
            outlined
            class="mb-4"
          />

          <v-text-field
            label="Hasło"
            v-model="form.password"
            :rules="passwordRules"
            type="password"
            required
            outlined
            class="mb-4"
          />

          <v-btn color="primary" type="submit" block>Zaloguj</v-btn>

          <p class="mt-4">
            Nie pamiętasz hasła?
            <router-link to="/reset-password" class="link">Resetuj hasło</router-link>
          </p>
        </v-form>
      </v-sheet>
    </v-sheet>
  </v-container>
</template>

<script>
import {getCurrentInstance, reactive, ref} from 'vue';
import {useRouter} from 'vue-router';

export default {
  setup() {
    const errors = ref(null);
    const router = useRouter();
    const {proxy} = getCurrentInstance();
    const axios = proxy.$axios;

    const form = reactive({
      login: '',
      password: '',
    });

    const emailRules = [
      value => !!value || 'Pole wymagane.',
      value => /.+@.+\..+/.test(value) || 'Nieprawidłowy adres email.',
    ];

    const passwordRules = [
      value => !!value || 'Pole wymagane.',
    ];

    const handleLogin = async () => {
      try {
        const response = await axios.post('/api/public/authenticate', {
          login: form.login,
          password: form.password
        });
        localStorage.setItem('token', response.data.token);

        const userResponse = await axios.get('/api/secured/me');
        localStorage.setItem('role', userResponse.data.role);
        localStorage.setItem('department', userResponse.data.department);
        localStorage.setItem('fullName', [userResponse.data.firstName, userResponse.data.lastName].join(' '));

        if (userResponse.data.role === 'ADMINISTRATOR') {
          await router.push('/administration');
        } else {
          await router.push('/issues');
        }
      } catch (e) {
        console.log(e);
        if (e.response?.data?.errors) {
          errors.value = Object.values(e.response.data.errors).join(' ');
        } else if (e.response.status === 403) {
          errors.value = 'Nieprawidłowy login lub hasło';
        } else {
          errors.value = e.response?.data?.message || 'Wystąpił błąd.';
        }
      }
    };

    return {
      form,
      errors,
      emailRules,
      passwordRules,
      handleLogin,
    };
  },
};
</script>

<style scoped>
.link {
  color: blue;
  text-decoration: underline;
}
</style>
