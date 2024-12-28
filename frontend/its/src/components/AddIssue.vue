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

      <v-file-input label="Załączniki" show-size=1024
                    :error-messages="errors.attachments"
                    v-model="fields.attachments.value.value" multiple></v-file-input>

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
import {inject} from "vue";

export default {
  computed: {
    Dictionary() {
      return Dictionary;
    },
  },
  data() {
    return {
      // attachments: [],
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
      attachments: yup
        .array()
        .of(
          yup.mixed().test(
            'fileSize',
            'Plik nie może przekraczać 2 MB.',
            (file) => !file || file.size <= 2 * 1024 * 1024 // 2 MB
          )
        )
        .nullable(), // Allows null or undefined attachments
    });

    const { handleSubmit, errors, values } = useForm({
      validationSchema,
    });

    const fields = {
      title: useField('title'),
      description: useField('description'),
      severity: useField('severity'),
      type: useField('type'),
      attachments: useField('attachments')
    };

    const axios = inject('$axios');
    const router = inject('$router');

    const submitForm = async (values) => {
      try {
        const response = await axios.post('/api/secured/issues', values);
        const id = response.data.uuid;
        router.push(`/issues/${id}`);
      } catch (error) {
        console.error('Failed to add issue:', error);
      }
    };

    return {
      fields,
      errors,
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
