<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card class="pa-4" elevation="3" width="500px">
      <v-card-title>Zmiana hasła</v-card-title>
      <v-card-subtitle>
        <v-alert
          density="compact"
          closable
          :model-value="passwordChangeResponse.show"
          :text="passwordChangeResponse.message"
          :title="passwordChangeResponse.title"
          :type="passwordChangeResponse.type"
        ></v-alert>
      </v-card-subtitle>
      <v-card-text>
        <v-text-field
          v-model="password.newPassword"
          label="Nowe hasło"
          type="password"
          outlined
          dense
          :error="passwordErrors.newPassword"
          :rules="[newPasswordRules]"
        ></v-text-field>
        <v-text-field
          v-model="password.confirmPassword"
          label="Potwierdź nowe hasło"
          type="password"
          outlined
          dense
          :error="passwordErrors.confirmPassword"
          :rules="[() => password.newPassword === password.confirmPassword || 'Hasła nie są zgodne']"
        ></v-text-field>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="changePassword">Zapisz</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>
<script>
export default {
  data() {
    return {
      loading: false,
      password: {
        newPassword: '',
        confirmPassword: '',
      },
      passwordErrors: {
        newPassword: false,
        confirmPassword: false,
      },
      passwordChangeResponse: {
        show: false,
        message: '',
        title: '',
        type: 'info',
      },
    };
  },
  methods: {
    async changePassword() {
      this.passwordErrors = { newPassword: false, confirmPassword: false };

      if (this.password.newPassword !== this.password.confirmPassword) {
        this.passwordErrors.confirmPassword = true;
        return;
      }

      try {
        const response = await this.$axios.post('/api/public/reset-password', {
          newPassword: this.password.newPassword,
          resetToken: this.$route.params.token
        });
        this.passwordChangeResponse.show = true;
        const success = response.status === 200;
        this.passwordChangeResponse.message = success ? 'Hasło zostało zmienione' : 'Operacja nie powiodła się';
        this.passwordChangeResponse.title = success ? 'Sukces' : 'Błąd';
        this.passwordChangeResponse.type = success ? 'success' : 'error';
      } catch (error) {
        console.log(error);
        throw error;
      }
    },
    newPasswordRules(value) {
      const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,}$/;
      return pattern.test(value) || 'Hasło musi mieć co najmniej 8 znaków, w tym dużą literę, małą literę, cyfrę i znak specjalny';
    },
  }
}


</script>

<style scoped>
.fill-height {
  height: 100vh;
}
</style>
