<template>
  <v-toolbar density="compact">
    <v-btn icon @click="goBack">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-spacer></v-spacer>
    <v-btn icon @click="logout">
      <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-toolbar>
  <v-container class="justify-center" max-width="700">
    <v-form @submit.prevent="handleAddIssue">
      <v-text-field label="Tytuł zgłoszenia" v-model="title" variant="outlined" counter="150" required></v-text-field>
      <v-textarea label="Opis zgłoszenia" v-model="description" variant="outlined" counter="2500" required></v-textarea>
      <v-file-input label="Załączniki" v-model="attachments" multiple show-size></v-file-input>
      <v-sheet class="d-flex flex-wrap justify-space-evenly">
          <v-select label="Priorytet" class="ma-2 pa-2" :items="Dictionary.severityOptions" clearable variant="outlined" v-model="severity" required></v-select>
          <v-select label="Rodzaj" class="ma-2 pa-2" :items="Dictionary.typeOptions" clearable variant="outlined" v-model="type" required></v-select>
      </v-sheet>
      <v-btn prepend-icon="mdi-send" type="submit" color="primary">Wyślij</v-btn>
    </v-form>
  </v-container>
  <v-dialog
      v-model="dialog"
      width="auto">
    <v-card
        max-width="400"
        prepend-icon="mdi-progress-close"
        text="Wprowadzone dane zostaną utracone."
        title="Jesteś pewien?">
      <template v-slot:actions class="justify-center">
        <v-btn
            class="ms-auto"
            text="OK"
            @click="dialogFunc"></v-btn>
        <v-btn
            class="ms-auto"
            text="Anuluj"
            @click="dialog = false"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>

import {isEmpty} from "lodash";
import Dictionary from "@/components/utils/dictionary.js";

export default {
  computed: {
    Dictionary() {
      return Dictionary
    }
  },
  data() {
    return {
      title: '',
      description: '',
      attachments: [],
      severity: null,
      type: null,
      severities: [
        { title: 'Niski', value: 'LOW' },
        { title: 'Średni', value: 'MEDIUM' },
        { title: 'Wysoki', value: 'HIGH' },
      ],
      types: [
        { title: 'Problem', value: 'PROBLEM' },
        { title: 'Incydent', value: 'INCIDENT' },
        { title: 'Wprowadzenie zmiany', value: 'CHANGE_REQUEST' },
        { title: 'Usługa serwisowa', value: 'SERVICE_REQUEST' }
      ],
      role: localStorage.getItem('role'),
      userName: localStorage.getItem('fullName'),
      department: localStorage.getItem('department'),
      dialog: false,
      dialogFunc: null
    };
  },
  methods: {
    async handleAddIssue() {
      try {
        const response = await this.$axios.post('/api/secured/issues', {
          title: this.title,
          description: this.description,
          severity: this.severity,
          type: this.type
        });
        console.log('Issue added:', response.data);
        let id = response.data.uuid;
        this.$router.push(`/issues/${id}`);
      } catch (error) {
        console.error('Failed to add issue:', error);
      }
    },
    formIsEmpty() {
      return  isEmpty(this.title) &&
              isEmpty(this.description) &&
              isEmpty(this.severity) &&
              isEmpty(this.type);
    },
    logout() {
      this.dialogFunc = () => {
        console.log('logout');
        localStorage.clear();
        this.$router.push('/');
      };
      if (this.formIsEmpty()) {
        this.dialogFunc();
        return
      }
      this.dialog = true;
    },
    goBack() {
      this.dialogFunc = () => {
        this.$router.push('/issues');
      };
      if (this.formIsEmpty()) {
        this.dialogFunc();
        return
      }
      this.dialog = true;
    }
  }
};
</script>
