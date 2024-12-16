<template>
  <v-toolbar density="compact">
    <v-btn icon @click="showAccountDetails">
      <v-icon>mdi-account-details</v-icon>
    </v-btn>
    <v-toolbar-title>{{ userName }} ({{ department }})</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn v-if="role === 'REPORTER'" @click="goToAddIssue" variant="outlined" prepend-icon="mdi-bug">
      Zgłoszenie
    </v-btn>
    <v-btn icon @click="logout">
      <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-toolbar>
  <v-container class="d-flex justify-center">
    <v-data-table-server
        :headers="headers"
        :items="issues"
        :items-per-page="pageSize"
        :items-length="totalItems"
        :loading="loading"
        loading-text="Ładuję dane..."
        items-per-page-text="Pozycji na stronie"
        item-value="id"
        density="compact"
        class="elevation-1"
        hover
        @update:options="loadIssues"
    >
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
              v-model="filters.titlePhrase"
              clearable
              label="Tytuł"
              density="compact"
              class="mx-2"
              @update:modelValue="loadIssues"
          ></v-text-field>
          <v-select
              v-model="filters.status"
              :items="statusOptions"
              label="Status"
              density="compact"
              clearable
              class="mx-2"
              @update:modelValue="loadIssues"
          ></v-select>
          <v-select
              v-model="filters.severity"
              :items="severityOptions"
              label="Priorytet"
              density="compact"
              class="mx-2"
              clearable
              @update:modelValue="loadIssues"
          ></v-select>
          <v-select
              v-model="filters.type"
              :items="typeOptions"
              label="Rodzaj"
              clearable
              density="compact"
              class="mx-2"
              @update:modelValue="loadIssues"
          ></v-select>
        </v-toolbar>
      </template>
      <template v-slot:[`item.action`]="{ item }">
        <v-btn variant="text" icon="mdi-import" size="small" @click="viewDetails(item.id)"/>
      </template>
      <template #item.id="{ item }">
        <span class="truncate">{{ item.id }}</span>
      </template>
      <template #item.reportedAt="{ item }">
        <span>{{ Formatter.formatDate(item.reportedAt) }}</span>
      </template>
      <template #item.severity="{ item }">
        <v-chip :text="Dictionary.getTitleByValue(item.severity, severityOptions)"
                class="centered-chip justify-center"
                style="min-width: 60px"
                :color="Formatter.formatSeverityChipColor(item.severity)"
                size="small"/>
      </template>
      <template #item.status="{ item }">
        <v-chip :text="Dictionary.getTitleByValue(item.status, statusOptions)"
                class="centered-chip"
                color="primary"
                size="small"/>
      </template>
      <template #item.type="{ item }">
        <v-chip :text="Dictionary.getTitleByValue(item.type, typeOptions)"
                class="centered-chip"
                size="small"/>
      </template>
      </v-data-table-server>
  </v-container>
</template>

<script>
import Dictionary from '@/components/utils/dictionary.js'
import Formatter from '@/components/utils/formatter.js'

const API = {
  async fetchIssues({ page, itemsPerPage, sortBy, filters, path, axios }) {
    try {
      return await axios.get(`/api/secured/issues/${path}`, {
        params: {
          offset: itemsPerPage * (page - 1),
          limit: itemsPerPage,
          ...Object.fromEntries(Object.entries(filters).filter(([, value]) => value)),
        },
      });
    } catch (error) {
      console.error('Failed to fetch issues:', error);
      throw error;
    }
  },
};

export default {
  computed: {
    Dictionary() {
      return Dictionary
    },
    Formatter() {
      return Formatter
    }
  },
  data() {
    return {
      filters: {
        id: '',
        titlePhrase: '',
        status: '',
        severity: '',
        type: '',
      },
      headers: [
        { title: 'ID', value: 'id' },
        { title: 'Tytuł', value: 'title' },
        { title: 'Status', value: 'status'},
        { title: 'Priorytet', value: 'severity' },
        { title: 'Rodzaj', value: 'type' },
        { title: 'Data zgłoszenia', value: 'reportedAt', sortable: true },
        { title: '', value: 'action', sortable: false },
      ],
      severityOptions: Dictionary.severityOptions,
      statusOptions: Dictionary.statusOptions,
      typeOptions: Dictionary.typeOptions,
      issues: [],
      page: 1,
      totalPages: 0,
      totalItems: 0,
      pageSize: 25,
      sortBy: 'reportedAt',
      loading: false,
      role: localStorage.getItem('role'),
      userName: localStorage.getItem('fullName'),
      department: localStorage.getItem('department')
    };
  },
  methods: {
    async loadIssues() {
      this.loading = true;
      const { filters, role, page, pageSize, sortBy, $axios } = this;
      const path = role === 'TECHNICIAN' ? 'assigned' : 'reported';

      try {
        const response = await API.fetchIssues({ page, itemsPerPage: pageSize, sortBy, filters, path, axios: $axios });
        const { data, totalPages, totalCount } = response.data;
        this.issues = data;
        this.totalPages = totalPages;
        this.totalItems = totalCount;
      } catch (error) {
        console.error('Error loading issues:', error);
      } finally {
        this.loading = false;
      }
    },
    navigateTo(path) {
      this.$router.push(path);
    },
    viewDetails(id) {
      this.navigateTo(`/issues/${id}`);
    },
    goToAddIssue() {
      this.navigateTo('/add-issue');
    },
    showAccountDetails() {
      console.log('show account');
    },
    logout() {
      console.log('logout');
      localStorage.clear();
      this.navigateTo('/');
    },
  },
  watch: {
    filters: {
      deep: true,
      handler: 'loadIssues',
    },
    page: 'loadIssues',
    pageSize: 'loadIssues',
  },
  created() {
    this.loadIssues();
  },
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
