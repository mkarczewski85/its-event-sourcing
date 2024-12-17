<template>
  <v-toolbar density="compact">
    <v-btn icon @click="goBack">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>
    <v-spacer></v-spacer>
    <v-btn icon @click="refresh">
      <v-icon>mdi-refresh</v-icon>
    </v-btn>
    <v-btn icon @click="logout">
      <v-icon>mdi-logout</v-icon>
    </v-btn>
  </v-toolbar>
  <v-container class="justify-center" max-width="700">
    <v-card :loading="loading">
      <v-card-title>
      </v-card-title>

      <v-card-subtitle class="mt-4">
        <v-row>
          <v-col cols="12">
            <div class="text-h6 text-wrap">{{ issue?.title }}</div>
          </v-col>
        </v-row>
      </v-card-subtitle>

      <v-card-subtitle class="mt-4 mb-4">
        <v-row justify="space-between">
          <v-col cols="6">
            <div>
              <strong>Utworzone przez:</strong> {{ issue?.reportedBy?.firstName }}
              {{ issue?.reportedBy?.lastName }}
              <v-icon
                  v-if="role === 'TECHNICIAN'"
                  @click="handleUserInfo"
                  size="x-small"
              >mdi-information-outline</v-icon>
            </div>
            <div>
              <strong>Data utworzenia:</strong> {{ Formatter.formatDate(issue?.reportedAt) }}
            </div>
          </v-col>
          <v-col cols="6">
            <div>
              <strong>Przypisane do:</strong> {{ issue?.assignedTo?.firstName }}
              {{ issue?.assignedTo?.lastName }}
              <v-icon
                  v-if="role === 'TECHNICIAN' && ['ASSIGNED', 'IN_PROGRESS'].includes(issue?.status)"
                  size="x-small"
                  @click="handleAssignmentUpdate"
              >mdi-file-document-edit-outline</v-icon>
              <v-icon
                  v-else-if="role === 'REPORTER'"
                  size="x-small"
                  @click="handleUserInfo"
              >mdi-information-outline</v-icon>
            </div>
            <div v-if="issue?.updatedAt">
              <strong>Data aktualizacji:</strong>{{ Formatter.formatDate(issue?.updatedAt) }}
            </div>
            <div v-else>
              <strong>Data aktualizacji:</strong> -
            </div>
          </v-col>
        </v-row>
      </v-card-subtitle>

      <v-card-subtitle class="mt-4 mb-4">ID zgłoszenia: {{ issue?.uuid }}</v-card-subtitle>

      <v-divider></v-divider>

      <v-card-subtitle class="mt-4 mb-4" opacity="100%">
        <v-row justify="space-evenly" align="center">
          <v-col class="d-flex align-center" cols="auto">
            <v-label class="mr-2">Status:</v-label>
            <v-chip
                v-if="role === 'TECHNICIAN' && ['ASSIGNED', 'IN_PROGRESS'].includes(issue?.status)"
                class="centered-chip justify-center"
                style="min-width: 100px"
                color="primary"
                @click="handleStatusUpdate"
                append-icon="mdi-file-document-edit-outline"
                outlined>{{ Dictionary.getStatusTitleByValue(issue?.status) }}
            </v-chip>
            <v-chip
                v-else-if="role === 'REPORTER' || ['REJECTED', 'CANCELLED', 'RESOLVED'].includes(issue?.status)"
                class="centered-chip justify-center"
                style="min-width: 100px"
                color="primary"
                outlined>{{ Dictionary.getStatusTitleByValue(issue?.status) }}
            </v-chip>
          </v-col>

          <v-col class="d-flex align-center" cols="auto">
            <v-label class="mr-2">Priorytet:</v-label>
            <v-chip
                v-if="role === 'TECHNICIAN' && ['ASSIGNED', 'IN_PROGRESS'].includes(issue?.status)"
                class="centered-chip justify-center"
                style="min-width: 100px"
                @click="handleSeverityUpdate"
                :color="Formatter.formatSeverityChipColor(issue?.severity)"
                append-icon="mdi-file-document-edit-outline"
                outlined>{{ Dictionary.getSeverityTitleByValue(issue?.severity) }}
            </v-chip>
            <v-chip
                v-else-if="role === 'REPORTER' || ['REJECTED', 'CANCELLED', 'RESOLVED'].includes(issue?.status)"
                class="centered-chip justify-center"
                style="min-width: 100px"
                :color="Formatter.formatSeverityChipColor(issue?.severity)"
                outlined>{{ Dictionary.getSeverityTitleByValue(issue?.severity) }}
            </v-chip>
          </v-col>

          <v-col class="d-flex align-center" cols="auto">
            <v-label class="mr-2">Rodzaj:</v-label>
            <v-chip
                v-if="role === 'TECHNICIAN' && ['ASSIGNED', 'IN_PROGRESS'].includes(issue?.status)"
                class="centered-chip justify-center"
                style="min-width: 100px"
                color="default"
                @click="handleTypeUpdate"
                append-icon="mdi-file-document-edit-outline"
                outlined>{{ Dictionary.getTypeTitleByValue(issue?.type) }}
            </v-chip>

            <v-chip
                v-else-if="role === 'REPORTER' || ['REJECTED', 'CANCELLED', 'RESOLVED'].includes(issue?.status)"
                class="centered-chip justify-center"
                style="min-width: 100px"
                color="default"
                outlined>{{ Dictionary.getTypeTitleByValue(issue?.type) }}
            </v-chip>
          </v-col>
        </v-row>
      </v-card-subtitle>

      <v-divider></v-divider>

      <v-card-text>
        <v-row>
          <v-col cols="12">
            <div class="mt-4">
              <strong>Opis zgłoszenia:</strong>
              <p>{{ issue?.description }}</p>
            </div>
          </v-col>
        </v-row>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-text>
        <v-row>
          <v-col cols="12">
            <strong>Załączniki:</strong>
            <v-list v-if="issue?.attachments" dense>
              <v-list-item
                  v-for="(attachment, index) in issue?.attachments"
                  :key="index"
                  link>
                <v-list-item-content>
                  <v-list-item-title>
                    <a
                        :href="generateDownloadLink(attachment.id)"
                        target="_blank"
                    >{{ attachment.name }}</a>
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list>
            <v-banner-text v-else class="mt-4">brak</v-banner-text>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
    <issue-comment-section ref="issueCommentSection"
                           :issue-id="issue?.uuid"
                           :is-issue-closed="['RESOLVED', 'CANCELLED', 'REJECTED'].includes(issue?.status)">
    </issue-comment-section>
  </v-container>
  <edit-issue-dialog :isOpen="editDialogOpen"
                     :updated-element="selectedChip"
                     :current-state="issue"
                     @confirm="fetchIssueDetails"
                     @update:isOpen="editDialogOpen = $event">
  </edit-issue-dialog>

  <edit-assignment-dialog :is-open="editAssignmentDialog"
                          :issue-id="issue?.uuid"
                          @confirm="goBack"
                          @update:isOpen="editAssignmentDialog = $event">

  </edit-assignment-dialog>

  <user-info-dialog :is-open="userInfoDialogOpen"
                    :user-data="role === 'TECHNICIAN' ? issue?.reportedBy : issue?.assignedTo"
                    @update:isOpen="userInfoDialogOpen = $event">
  </user-info-dialog>

</template>

<script>
import Dictionary from "@/components/utils/dictionary.js";
import Formatter from "@/components/utils/formatter.js";
import EditIssueDialog from "@/components/EditIssueDialog.vue";
import EditAssignmentDialog from "@/components/EditAssignmentDialog.vue";
import UserInfoDialog from "@/components/UserInfoDialog.vue";
import IssueCommentSection from "@/components/IssueCommentSection.vue";

export default {
  components: {
    EditIssueDialog,
    UserInfoDialog,
    EditAssignmentDialog,
    IssueCommentSection
  },
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
      issue: {},
      selectedChip: '',
      currentView: "IssueDetails",
      loading: false,
      editDialogOpen: false,
      editAssignmentDialog: false,
      userInfoDialogOpen: false,
      role: localStorage.getItem('role'),
      department: localStorage.getItem('department'),
      userName: localStorage.getItem('fullName')
    };
  },
  methods: {
    async fetchIssueDetails() {
      this.selectedChip = null;
      this.loading = true;
      try {
        const response = await this.$axios.get(`/api/secured/issues/${this.$route.params.id}`);
        this.issue = response.data;
      } catch (error) {
        console.error('Failed to fetch issue details:', error);
      } finally {
        this.loading = false;
      }
    },
    handleSeverityUpdate() {
      this.selectedChip = 'severity';
      this.editDialogOpen = true;
    },
    handleStatusUpdate() {
      this.selectedChip = 'status';
      this.editDialogOpen = true;
    },
    handleTypeUpdate() {
      this.selectedChip = 'type';
      this.editDialogOpen = true;
    },
    handleAssignmentUpdate() {
      this.editAssignmentDialog = true;
    },
    handleUserInfo() {
      this.userInfoDialogOpen = true;
    },
    generateDownloadLink(id) {
      return `/api/public/attachments/download/${id}`;
    },
    goBack() {
      this.$router.push("/issues");
    },
    refresh() {
      this.fetchIssueDetails();
      this.$refs.issueCommentSection.refreshComments();
    },
    logout() {
      console.log('logout');
      localStorage.clear();
      this.navigateTo('/');
    },
  },
  created() {
    this.fetchIssueDetails();
  }
};
</script>
