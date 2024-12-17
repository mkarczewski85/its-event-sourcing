<template>
  <v-navigation-drawer v-model="localDrawer" temporary>
    <v-list-item prepend-icon="mdi-account">
      <v-list-item-title>{{ this.fullName }}</v-list-item-title>
      <v-list-item-subtitle class="text-caption">{{ this.department }}</v-list-item-subtitle>
    </v-list-item>

    <v-divider></v-divider>

    <v-list density="compact" nav>
      <v-list-item
          prepend-icon="mdi-view-dashboard"
          title="Moje zgłoszenia"
          to="/issues"
      ></v-list-item>
      <v-list-item
          prepend-icon="mdi-cog"
          title="Ustawienia konta"
          to="/settings"
      ></v-list-item>
        <v-list-item
            title="Instrukcja obsługi"
            prepend-icon="mdi-help-circle"
            href="https://www.atlassian.com/software/jira/guides/getting-started/introduction"
            target="_blank"
            rel="noopener noreferrer"
        >
          <template #append>
            <v-icon size="x-small" class="custom-icon">mdi-open-in-new</v-icon>
          </template>
      </v-list-item>
      <v-list-item
          prepend-icon="mdi-information"
          title="O programie"
          @click="openAboutDialog"
      ></v-list-item>
    </v-list>
  </v-navigation-drawer>

  <v-dialog v-model="aboutDialog" max-width="500">
    <v-card>
      <v-card-title class="text-h5">O programie</v-card-title>
      <v-card-text>
        <p><strong class="text-center">Issue Tracking System</strong></p>
        <p><strong>Wersja:</strong> 1.0.0</p>
        <p><strong>Autor:</strong> Maciej Karczewski</p>
        <p>
          Aplikacja do zarządzania zgłoszeniami, wykonana w ramach projektu inżynierskiego.
        </p>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" variant="text" @click="this.aboutDialog = false">Zamknij</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  props: {
    drawer: {
      type: Boolean,
      required: true,
    },
  },
  emits: ["update:drawer"],
  data() {
    return {
      fullName: localStorage.getItem('fullName'),
      department: localStorage.getItem('department'),
      aboutDialog: false,
    }
  },
  methods: {
    openAboutDialog() {
      this.localDrawer = false;
      this.aboutDialog = true;
    },
  },
  computed: {
    localDrawer: {
      get() {
        return this.drawer
      },
      set(value) {
        this.$emit("update:drawer", value)
      },
    },
  },
}
</script>

