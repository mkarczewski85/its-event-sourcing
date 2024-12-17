<template>
  <v-toolbar density="compact">
    <v-btn icon @click="toggleDrawer">
      <v-icon>mdi-account-details</v-icon>
    </v-btn>

    <v-spacer></v-spacer>
    <v-btn icon @click="logout">
      <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-toolbar>
  <navigation-drawer v-model:drawer="drawer" />
  <v-container max-width="70%">
    <v-card class="mb-6" title="Podstawowe informacje">
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field
                label="Imię i nazwisko"
                v-model="user.fullName"
                readonly
                outlined
                dense
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
                label="Email"
                v-model="user.username"
                readonly
                outlined
                dense
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
                label="Jednostka organizacyjna"
                v-model="user.department"
                readonly
                outlined
                dense
            ></v-text-field>
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field
                label="Lokalizacja"
                v-model="user.location"
                readonly
                outlined
                dense
            ></v-text-field>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <v-card class="mb-6" title="Zarządzanie hasłem">
      <v-card-text>
        <v-row align="center">
          <v-col cols="12" md="6">
            <v-btn color="primary" prepend-icon="mdi-lock-reset" @click="openPasswordDialog">Zmień hasło</v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <v-card title="Ustawienia powiadomień">
      <v-card-text>
        <v-switch
            v-model="notifications.updates"
            label="Chcę dostawać powiadomienia o aktualizacji zgłoszeń"
            color="primary"
        ></v-switch>
        <v-switch
            v-model="notifications.comments"
            label="Chcę dostawać powiadomienia o dodawanych komentarzach"
            color="primary"
        ></v-switch>
      </v-card-text>
    </v-card>

    <v-dialog v-model="isPasswordDialogOpen" max-width="500">
      <v-card>
        <v-card-title>Zmiana hasła</v-card-title>
        <v-card-subtitle>
          <v-alert
              density="compact"
              closable
              :model-value="passwordErrors.oldPassword"
              :text="oldPasswordErrorMsg"
              title="Błąd"
              type="error"
          ></v-alert>
        </v-card-subtitle>
        <v-card-text>
          <v-text-field
              v-model="password.oldPassword"
              label="Stare hasło"
              type="password"
              outlined
              dense
              :rules="oldPasswordRules"
              :error="passwordErrors.oldPassword"
          ></v-text-field>
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
          <v-btn color="grey" variant="text" @click="closePasswordDialog">Anuluj</v-btn>
          <v-btn color="primary" @click="changePassword">Zapisz</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import NavigationDrawer from "@/components/NavigationDrawer.vue";

export default {
  components: {NavigationDrawer},
  data() {
    return {
      drawer: false,
      user: {},
      loading: false,
      isPasswordDialogOpen: false,
      password: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
      },
      passwordErrors: {
        oldPassword: false,
        newPassword: false,
        confirmPassword: false,
      },
      notifications: {
        updates: true,
        comments: true,
      },
      oldPasswordErrorMsg: 'Niepoprawne stare hasło.',
      oldPasswordRules: [
        value => !!value || 'Wymagane.',
        value => (value && value.length >= 3) || 'Min 3 znaki.',
      ],
    };
  },
  methods: {
    async fetchUserDetails() {
      try {
        this.loading = true;
        const response = await this.$axios.get('/api/secured/me');
        this.user = response.data;
        this.user.fullName = this.user.firstName + ' ' + this.user.lastName;
      } catch (error) {
        console.error('Failed to fetch issue details:', error);
      } finally {
        this.loading = false;
      }
    },
    async changePassword() {
      this.passwordErrors = { oldPassword: false, newPassword: false, confirmPassword: false };

      if (this.password.newPassword !== this.password.confirmPassword) {
        this.passwordErrors.confirmPassword = true;
        return;
      }

      try {
        await this.$axios.post('/api/secured/me/password-change', {
          oldPassword: this.password.oldPassword,
          newPassword: this.password.newPassword,
        });
        this.closePasswordDialog();
      } catch (error) {
        if (error.response && error.response.status === 401) {
          this.passwordErrors.oldPassword = true;
        }
      }
    },
    newPasswordRules(value) {
      const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,}$/;
      return pattern.test(value) || 'Hasło musi mieć co najmniej 8 znaków, w tym dużą literę, małą literę, cyfrę i znak specjalny';
    },
    openPasswordDialog() {
      this.isPasswordDialogOpen = true;
    },
    closePasswordDialog() {
      this.isPasswordDialogOpen = false;
      this.password = { oldPassword: '', newPassword: '', confirmPassword: '' };
      this.passwordErrors = { oldPassword: false, newPassword: false, confirmPassword: false };
    },
    toggleDrawer() {
      this.drawer = !this.drawer
    },
    logout() {
      console.log('logout');
      localStorage.clear();
      this.navigateTo('/');
    },
  },
  created() {
    this.fetchUserDetails();
  }
};
</script>
