<template>
  <v-container>
    <v-form @submit.prevent="handleReset">
      <v-text-field label="Email" v-model="email" required></v-text-field>
      <v-btn type="submit" color="primary">Send Reset Link</v-btn>
    </v-form>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      email: ''
    };
  },
  methods: {
    async handleReset() {
      try {
        const response = await this.$axios.post('/auth/reset-password', {
          email: this.email
        });
        console.log('Reset link sent:', response.data);
      } catch (error) {
        console.error('Failed to send reset link:', error);
      }
    }
  }
};
</script>
