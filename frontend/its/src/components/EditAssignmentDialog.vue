<template>
  <v-dialog v-model="localOpen" :max-width="maxWidth">
    <v-card>
      <v-card-title class="text-h5">
        Przepisanie zgłoszenia
        <v-spacer></v-spacer>
      </v-card-title>
      <v-card-text>
        <v-select
            label="Wybierz osobę"
            class="ma-2 pa-2"
            :items="options"
            variant="outlined"
            :loading="loading"
            v-model="selectedUserId"
            required></v-select>
        <slot />
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" :disabled="isConfirmBtnDisabled" prepend-icon="mdi-content-save" @click="handleUpdate">Zapisz</v-btn>
        <v-btn color="secondary" prepend-icon="mdi-cancel" @click="close">Anuluj</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>

export default {
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
    issueId: {
      type: String,
      required: true
    },
    maxWidth: {
      type: String,
      default: "600px",
    }
  },
  emits: ["update:isOpen", "confirm"],
  data() {
    return {
      loading: false,
      selectedUserId: null,
      options: []
    };
  },
  computed: {
    localOpen: {
      get() {
        return this.isOpen;
      },
      set(value) {
        this.$emit("update:isOpen", value);
      },
    },
    isConfirmBtnDisabled: {
      get() {
        return this.selectedUserId === null;
      }
    }
  },
  methods: {
    async fetchUsers() {
      this.loading = true;
      try {
        const response = await this.$axios.get('/api/secured/users/technicians');
        this.options = response.data.map(({id, firstName, lastName}) => ({
          title: firstName + ' ' + lastName,
          value: id
        }));
      } catch (error) {
        console.error('Failed to fetch issue details:', error);
      } finally {
        this.loading = false;
      }
    },
    async handleUpdate() {
      try {
        const response = await this.$axios.post(`/api/secured/issues/${this.issueId}/assign`, {
          uuid: this.issueId,
          assignTo: this.selectedUserId
        });
        console.log('Issue assignment updated:', response.data);
        this.confirm();
      } catch (error) {
        console.error('Failed to assign issue:', error);
      }
    },
    close() {
      this.selectedUserId = null;
      this.localOpen = false;
    },
    confirm() {
      this.$emit("confirm");
      this.close();
    },
  },
  watch: {
    localOpen: 'fetchUsers'
  }
};

</script>
