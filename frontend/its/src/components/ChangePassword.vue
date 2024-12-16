<template>
  <v-container>
    <v-form @submit.prevent="handleChange">
      <v-text-field label="Current Password" v-model="currentPassword" type="password" required></v-text-field>
      <v-text-field label="New Password" v-model="newPassword" type="password" required></v-text-field>
      <v-btn type="submit" color="primary">Change Password</v-btn>
    </v-form>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      currentPassword: '',
      newPassword: ''
    };
  },
  methods: {
    async handleChange() {
      try {
        const response = await this.$axios.post('/auth/change-password', {
          currentPassword: this.currentPassword,
          newPassword: this.newPassword
        });
        console.log('Password changed:', response.data);
      } catch (error) {
        console.error('Failed to change password:', error);
      }
    }
  }
};
</script>
