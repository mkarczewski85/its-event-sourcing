<template>
  <v-container class="fill-height d-flex justify-center align-center">
    <v-card class="pa-4" elevation="3" width="500px">
      <v-card-title class="text-h5 text-center">
        Resetowanie hasła
      </v-card-title>
      <v-card-text>
        <v-form @submit.prevent="handleReset">
          <v-text-field
              label="Email"
              :rules="[notBlankRule, emailRules]"
              v-model="email"
              required
              outlined
          ></v-text-field>
          <v-btn type="submit" color="primary" block class="mt-2">
            Wyślij
          </v-btn>
        </v-form>
        <v-alert
            v-if="alertVisible"
            type="info"
            class="mt-4"
            dismissible
            @input="alertVisible = false">
          Na podany adres email został wysłany link do resetowania hasła.
        </v-alert>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      alertVisible: false,
      notBlankRule: (v) => !!v || "Pole wymagane",
      email: '',
    };
  },
  methods: {
    async handleReset() {
      try {
        const response = await this.$axios.post('/api/public/send-reset-token', {
          email: this.email
        });
        this.alertVisible = true;
      } catch (error) {
        console.error('Failed to send reset link:', error);
      }
    },
    emailRules(value) {
      const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      return pattern.test(value) || 'Nieprawidłowy adres email.';
    }
  }
};
</script>

<style scoped>
.fill-height {
  height: 100vh;
}
</style>
