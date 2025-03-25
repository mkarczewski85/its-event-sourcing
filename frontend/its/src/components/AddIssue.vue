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
    <v-form @submit.prevent="handleSubmit">
      <v-text-field
        label="Tytuł zgłoszenia"
        v-model="fields.title.value.value"
        :error-messages="errors.title"
        variant="outlined"
        counter="150"
      ></v-text-field>

      <v-textarea
        label="Opis zgłoszenia"
        v-model="fields.description.value.value"
        :error-messages="errors.description"
        variant="outlined"
        counter="2500"
      ></v-textarea>

      <v-file-input label="Załączniki" v-model="attachments" multiple show-size></v-file-input>

      <v-sheet class="d-flex flex-wrap justify-space-evenly">
        <v-select
          label="Priorytet"
          class="ma-2 pa-2"
          :items="Dictionary.severityOptions"
          clearable
          v-model="fields.severity.value.value"
          :error-messages="errors.severity"
          variant="outlined"
        ></v-select>

        <v-select
          label="Rodzaj"
          class="ma-2 pa-2"
          :items="Dictionary.typeOptions"
          clearable
          v-model="fields.type.value.value"
          :error-messages="errors.type"
          variant="outlined"
        ></v-select>
      </v-sheet>

      <v-btn prepend-icon="mdi-send" type="submit" color="primary">Wyślij</v-btn>
    </v-form>
  </v-container>
  <v-dialog v-model="dialog" width="auto">
    <v-card max-width="400" prepend-icon="mdi-progress-close" text="Wprowadzone dane zostaną utracone." title="Jesteś pewien?">
      <template v-slot:actions class="justify-center">
        <v-btn class="ms-auto" text="OK" @click="dialogFunc"></v-btn>
        <v-btn class="ms-auto" text="Anuluj" @click="dialog = false"></v-btn>
      </template>
    </v-card>
  </v-dialog>
</template>

<script>
import { useField, useForm } from 'vee-validate';
import * as yup from 'yup';
import Dictionary from '@/components/utils/dictionary.js';
import {inject, ref} from "vue";

export default {
  computed: {
    Dictionary() {
      return Dictionary;
    },
  },
  data() {
    return {
      dialog: false,
      dialogFunc: null,
    };
  },
  setup() {
    const validationSchema = yup.object({
      title: yup.string().required('Tytuł jest wymagany').max(150, 'Tytuł nie może przekraczać 150 znaków'),
      description: yup.string().required('Opis jest wymagany').max(2500, 'Opis nie może przekraczać 2500 znaków'),
      severity: yup.string().required('Priorytet jest wymagany'),
      type: yup.string().required('Rodzaj jest wymagany'),
    });

    const attachments = ref(null);

    const { handleSubmit, errors, values } = useForm({
      validationSchema,
    });

    const fields = {
      title: useField('title'),
      description: useField('description'),
      severity: useField('severity'),
      type: useField('type'),
    };

    const axios = inject('$axios');
    const router = inject('$router');

    const submitForm = async (values) => {
      try {
        // Create FormData to include both the form fields and the attachment
        const formData = new FormData();
        formData.append('json', new Blob([JSON.stringify(values)], { type: 'application/json' })); // Add the form's JSON data

        // Attach the single file if it exists
        if (attachments.value) {
          formData.append('attachment', attachments.value); // Use a consistent key for the single file
        }

        // Send the FormData payload
        const response = await axios.post('/api/secured/issues', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        const id = response.data.uuid;
        router.push(`/issues/${id}`);
      } catch (error) {
        console.error('Failed to add issue:', error);
      }
    };

    return {
      fields,
      errors,
      attachments,
      handleSubmit: handleSubmit(submitForm),
    };
  },
  methods: {
    formIsEmpty() {
      return (
        !this.fields.title.value &&
        !this.fields.description.value &&
        !this.fields.severity.value &&
        !this.fields.type.value
      );
    },
    logout() {
      this.dialogFunc = () => {
        localStorage.clear();
        this.$router.push('/');
      };
      if (this.formIsEmpty()) {
        this.dialogFunc();
        return;
      }
      this.dialog = true;
    },
    goBack() {
      this.dialogFunc = () => {
        this.$router.push('/issues');
      };
      if (this.formIsEmpty()) {
        this.dialogFunc();
        return;
      }
      this.dialog = true;
    },
  },
};
</script>
