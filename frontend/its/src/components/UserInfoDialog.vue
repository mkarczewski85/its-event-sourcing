<template>
  <v-dialog v-model="localOpen" :max-width="maxWidth">
    <v-card>
      <v-card-title>
        <span class="text-h6">Informacje o użytkowniku</span>
        <v-spacer></v-spacer>
      </v-card-title>

      <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12" sm="6">
              <v-text-field
                  v-model="userData.firstName"
                  label="Imię"
                  readonly
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6">
              <v-text-field
                  v-model="userData.lastName"
                  label="Nazwisko"
                  readonly
              ></v-text-field>
            </v-col>
            <v-col cols="12">
              <v-text-field
                  v-model="userData.email"
                  label="Email"
                  readonly
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6">
              <v-text-field
                  v-model="userData.department"
                  label="Jednostka organizacyjna"
                  readonly
              ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6">
              <v-text-field
                  v-model="userData.location"
                  label="Lokalizacja"
                  readonly
              ></v-text-field>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="close">Zamknij</v-btn>
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
    userData: {
      type: Object,
      required: true
    },
    maxWidth: {
      type: String,
      default: "600px",
    }
  },
  emits: ["update:isOpen"],
  computed: {
    localOpen: {
      get() {
        return this.isOpen;
      },
      set(value) {
        this.$emit("update:isOpen", value);
      },
    },
  },
  methods: {
    close() {
      this.localOpen = false;
    }
  }
}

</script>
