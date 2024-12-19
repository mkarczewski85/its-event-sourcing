<template>

  <v-toolbar density="compact">
    <v-spacer></v-spacer>
    <v-btn @click="openAddUserDialog" variant="outlined" prepend-icon="mdi-account-plus">
      Utwórz konto
    </v-btn>
    <v-btn icon @click="logout">
      <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-toolbar>

  <v-container>
    <v-card>
      <v-card-title>Panel Administracyjny</v-card-title>
      <v-card-subtitle>Zarządzanie użytkownikami</v-card-subtitle>
      <v-data-table-server
        :headers='headers'
        :items='users'
        :items-per-page='pageSize'
        :items-length='totalItems'
        :loading='loading'
        loading-text='Ładuję dane...'
        items-per-page-text='Pozycji na stronie'
        item-value='id'
        density='compact'
        class='elevation-1'
        hover
        @update:options='fetchUsers'>

        <template v-slot:top>
          <v-toolbar rounded>
            <v-toolbar-title>Filtry</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-text-field
              v-model="filters.id"
              clearable
              label="ID"
              density="compact"
              class="mx-2"
              @update:modelValue="loadIssues"
            ></v-text-field>
            <v-text-field
              v-model="filters.namePhrase"
              clearable
              label="Imię lub naziwsko"
              density="compact"
              class="mx-2"
              @update:modelValue="loadIssues"
            ></v-text-field>
            <v-select
              v-model="filters.role"
              :items="roleOptions"
              label="Rola"
              density="compact"
              clearable
              class="mx-2"
              @update:modelValue="fetchUsers"
            ></v-select>
            <v-select
              v-model="filters.department"
              :items="departmentOptions"
              label="Jednostka"
              density="compact"
              :loading="departmentsLoading"
              clearable
              class="mx-2"
              @update:modelValue="fetchUsers"
            ></v-select>
          </v-toolbar>
        </template>

        <template #item.id="{ item }">
          <span class='truncate'>{{ item.id }}</span>
        </template>
        <template #item.role="{ item }">
          <v-chip :text="Dictionary.getTitleByValue(item.role, roleOptions)"
                  class="centered-chip"
                  color="primary"
                  size="small"/>
        </template>
        <template v-slot:[`item.action`]='{ item }'>
          <v-btn variant='text' icon='mdi-pencil' @click='editUser(item)'>
          </v-btn>
          <v-btn variant='text' icon='mdi-lock-reset' @click='confirmReset(item)'>
          </v-btn>
        </template>
        <template #item.isActive="{ item }">
          <v-icon>{{ item.isActive ? "mdi-check-circle-outline" : "mdi-minus-circle-outline" }}</v-icon>
        </template>
      </v-data-table-server>
    </v-card>

    <v-dialog v-model='editDialog' max-width='600'>
      <v-card>
        <v-card-title>Edycja Użytkownika</v-card-title>
        <v-card-text>
          <v-form ref='editForm'>
            <v-text-field v-model='editedUser.firstName' :rules="[notBlankRule]" required="true" label='Imię'></v-text-field>
            <v-text-field v-model='editedUser.lastName' :rules="[notBlankRule]" required="true" label='Nazwisko'></v-text-field>
            <v-text-field v-model='editedUser.email' required="true" :rules="[notBlankRule, emailRules]" label='Email'></v-text-field>
            <v-switch v-model='editedUser.isActive' color="primary" label='Aktywne konto'></v-switch>
            <v-select
              v-model='editedUser.department.uuid'
              :items='departmentOptions'
              label='Departament'
            ></v-select>
            <v-select
              v-model='editedUser.role'
              :items='roleOptions'
              label='Rola'
            ></v-select>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn color='primary' prepend-icon="mdi-content-save" @click='patchUser'>Zapisz</v-btn>
          <v-btn color='secondary' prepend-icon="mdi-cancel" @click='closeEditDialog'>Anuluj</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model='addDialog' max-width='600'>
      <v-card>
        <v-card-title>Tworzenia konta użytkownika</v-card-title>
        <v-card-text>
          <v-form ref='editForm'>
            <v-text-field v-model='addUser.firstName' :rules="[notBlankRule]" required="true" label='Imię'></v-text-field>
            <v-text-field v-model='addUser.lastName' :rules="[notBlankRule]" required="true" label='Nazwisko'></v-text-field>
            <v-text-field v-model='addUser.email' required="true" :rules="[notBlankRule, emailRules]" label='Email'></v-text-field>
            <v-select
              v-model='addUser.departmentId'
              :items='departmentOptions'
              label='Departament'
            ></v-select>
            <v-select
              v-model='addUser.role'
              :items='roleOptions'
              label='Rola'
            ></v-select>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn color='primary' prepend-icon="mdi-content-save" @click='createUser'>Zapisz</v-btn>
          <v-btn color='secondary' prepend-icon="mdi-cancel" @click='closeAddUserDialog'>Anuluj</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model='resetDialog' max-width='400'>
      <v-card>
        <v-card-title>Potwierdzenie</v-card-title>
        <v-card-text>Czy na pewno zresetować hasło tego użytkownika?</v-card-text>
        <v-card-actions>
          <v-btn color='primary' :loading="resetLoading" @click='resetUserPassword'>Tak</v-btn>
          <v-btn color='secondary' @click='closeResetDialog'>Nie</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import Dictionary from "@/components/utils/dictionary.js";
export default {
  data() {
    return {
      headers: [
        {title: 'ID', value: 'id'},
        {title: 'Imię', value: 'firstName'},
        {title: 'Nazwisko', value: 'lastName'},
        {title: 'Departament', value: 'department.name'},
        {title: 'Rola', value: 'role'},
        {title: 'Aktywne', value: 'isActive'},
        {title: '', value: 'action', sortable: false},
      ],
      users: [],
      page: 1,
      totalPages: 0,
      totalItems: 0,
      pageSize: 25,
      options: {},
      loading: false,
      notBlankRule: (v) => !!v || "Pole wymagane",
      filters: {
        id: '',
        namePhrase: '',
        department: '',
        role: ''
      },
      emptyAddUser: {
        firstName: '',
        lastName: '',
        emailName: '',
        departmentId: ''
      },
      addUser: {
        firstName: '',
        lastName: '',
        emailName: '',
        departmentId: ''
      },
      editedUser: {},
      departmentOptions: [],
      departmentsLoading: false,
      resetLoading: false,
      editDialog: false,
      resetDialog: false,
      addDialog: false,
      roleOptions: Dictionary.userRoleOptions
    };
  },
  computed: {
    Dictionary() {
      return Dictionary
    },
  },
  methods: {
    async fetchDepartments() {
      this.departmentsLoading = true;
      try {
        const response = await this.$axios.get('/api/secured/users/departments');
        this.departmentOptions = response.data.map(({uuid, name}) => ({
          title: name,
          value: uuid
        }));
      } catch (error) {
        console.error('Failed to fetch issue details:', error);
      } finally {
        this.departmentsLoading = false;
      }
    },
    async fetchUsers() {
      this.loading = true;
      try {
        const response = await this.$axios.get('/api/secured/users', {
          params: {
            offset: this.pageSize * (this.page - 1),
            limit: this.pageSize,
            ...Object.fromEntries(Object.entries(this.filters).filter(([, value]) => value)),
          },
        });
        const {data, totalPages, totalCount} = response.data;
        this.users = data;
        this.totalPages = totalPages;
        this.totalItems = totalCount;
      } catch (error) {
        console.error('Failed to fetch issues:', error);
        throw error;
      } finally {
        this.loading = false;
      }
    },
    async createUser() {
      try {
        const response = await this.$axios.post(`/api/secured/users`, {
          firstName: this.addUser.firstName,
          lastName: this.addUser.lastName,
          email: this.addUser.email,
          departmentId: this.addUser.departmentId,
          role: this.addUser.role
        });
        await this.fetchUsers();
        this.closeAddUserDialog();
      } catch (error) {
        console.error('Failed to create user:', error);
        throw error;
      }
    },
    async patchUser() {
      try {
        const response = await this.$axios.patch(`/api/secured/users/${this.editedUser.id}`, {
            firstName: this.editedUser.firstName,
            lastName: this.editedUser.lastName,
            email: this.editedUser.email,
            departmentId: this.editedUser.departmentId,
            role: this.editedUser.role,
            isActive: this.editedUser.isActive
          }
        );
        await this.fetchUsers();
        this.closeEditDialog();
      } catch (error) {
        console.error('Failed to patch user:', error);
        throw error;
      }
    },
    async resetUserPassword() {
      try {
        this.resetLoading = true;
        const response = await this.$axios.post(`/api/secured/users/${this.editedUser.id}/reset-password`);
        this.resetDialog = false;
      } catch (error) {
        console.error('Failed to reset user password:', error);
        throw error;
      } finally {
        this.resetLoading = false;
      }
    },
    emailRules(value) {
      const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      return pattern.test(value) || 'Nieprawidłowy adres email.';
    },
    editUser(user) {
      this.editedUser = {...user};
      this.editDialog = true;
    },
    closeEditDialog() {
      this.editDialog = false;
    },
    confirmReset(user) {
      this.editedUser = {...user};
      this.resetDialog = true;
    },
    closeResetDialog() {
      this.resetDialog = false;
    },
    logout() {
      console.log('logout');
      localStorage.clear();
      this.$router.push('/');
    },
    openAddUserDialog() {
      this.addDialog = true;
    },
    closeAddUserDialog() {
      this.addDialog = false;
      this.addUser = this.emptyAddUser;
    }
  },
  watch: {
    filters: {
      handler: 'fetchUsers',
      deep: true,
    },
    page: 'fetchUsers',
    pageSize: 'fetchUsers',
  },
  created() {
    this.fetchUsers();
    this.fetchDepartments();
  }
};
</script>
<style>
.truncate {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
  display: inline-block;
}
</style>
